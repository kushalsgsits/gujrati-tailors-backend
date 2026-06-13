# Gujrati Tailors — Backend

Spring Boot REST API for a tailor shop's order management, deployed on Google App Engine
(Standard) with Google Cloud Datastore as the database. This document is a permanent,
tool-agnostic guide to the project for any developer or AI assistant working in this repo.

## Tech stack

- **Java**: 21
- **Framework**: Spring Boot 3.3.4 (Web on Jetty, Data REST, Security, Actuator)
- **Cloud / DB**: Google Cloud Datastore via `spring-cloud-gcp-starter-data-datastore` 5.5.0
- **Auth**: Stateless JWT (`jjwt` 0.12.3)
- **Build**: Maven (wrapper `./mvnw`), `appengine-maven-plugin`, google-java-format via `fmt-maven-plugin`
- **Other**: Lombok, Apache POI (offline Excel export)

## How to run

```bash
# 1. Start Datastore emulator (separate terminal; uses ./db_data if present)
./start-datastore-emulator.sh

# 2. Run the app (profile "local" is default in application.yaml)
source set_vars.sh && mvn spring-boot:run     # serves on :8080

# From IntelliJ: run GujratiTailorsApplication with DATASTORE_EMULATOR_HOST=localhost:8081
# (see set_vars.sh for the full set of emulator env vars)

# Deploy to Google App Engine (profile "prod")
./deploy.sh                                    # mvn clean package + appengine:deploy
```

- Default profile is `local` (Datastore emulator). `prod` profile is set by `app.yaml` on GAE.
- Prod URL: `https://gujrati-tailors-backend.el.r.appspot.com`

## Package map

```
com.harvi.tailor
├── GujratiTailorsApplication       # Spring Boot entry point
├── GlobalExceptionHandler          # 400 for IllegalArgumentException; 404 ResourceNotFound; else 500
├── item/                           # Item catalog + self-service rate overrides
│   ├── Item                        # POJO: id, name, groupName, type, comboItemIds, rate
│   ├── ItemsGroup                  # record(groupName, List<Item>)
│   ├── ItemService                 # Hardcoded catalog (structure + default rates) + overlay
│   ├── ItemController              # GET /items/groupedItems (Cache-Control: no-store)
│   ├── ItemRate                    # @Entity (kind "itemRate"): id, rate
│   ├── ItemRateRepository          # DatastoreRepository<ItemRate, String>
│   ├── RateController              # POST /rates
│   └── RatesUpdateRequest          # record(Map<String, Integer> rates)
├── order/                          # Orders
│   ├── Order                       # @Entity (kind "order") + nested Customer, OrderItem
│   ├── OrderRepository             # DatastoreRepository (Spring Data REST -> /orders)
│   ├── OrderService                # Raw Datastore query for filtered search
│   ├── OrderSearchController       # GET /orders/customSearch
│   └── OrderRepositoryEventHandler # Before-create: compute/validate order id, set status
├── security/                       # JWT auth (single hardcoded Admin user)
├── commons/CommonConfig            # Datastore bean (prod vs local emulator)
└── utils/                          # ConverterUtils, BackupUtil (offline JSON->Excel)
```

## Data model & persistence

Datastore kinds:

| Kind | Purpose |
|------|---------|
| `order` | Orders (`Customer`, `OrderItem` embedded). Only domain with full CRUD via Spring Data REST. |
| `itemRate` | **Rate overrides only** — one entity per item id (`@Id` = item id, `rate` int). |

**Not persisted:** item structure (ids, names, groups, combos) and users — hardcoded in Java.

- Project id and namespace: `gujrati-tailors-backend`
- Composite indexes for order search: `src/main/webapp/WEB-INF/index.yaml`
- No composite index needed for `itemRate` (`findAll()` only)

### Order id format

`{yyyy-MM-}{C|R}-{orderNumber}` (e.g. `2024-01-R-1119`), computed in `OrderRepositoryEventHandler`.

## REST API

| Method | Path | Notes |
|--------|------|-------|
| POST | `/authenticate` | Public. `{username,password}` -> `{jwtToken}` |
| GET | `/items/groupedItems` | Catalog grouped by category with **effective rates** (defaults + overrides). `Cache-Control: no-store`. |
| POST | `/rates` | Upsert rate overrides. Body `{ "rates": { "shirt": 400, "pant": 500, ... } }`. Validates known ids, rate 0–9999. |
| GET/POST/PUT/PATCH/DELETE | `/orders`, `/orders/{id}` | Spring Data REST (HAL/HATEOAS) |
| GET | `/orders/customSearch` | Filtered search + paging |

All endpoints except `/authenticate` require `Authorization: Bearer <jwt>`.
CORS is open (`@CrossOrigin` + security `cors`).

Bad rate input returns **HTTP 400** with `{ errorMessage, internalErrorMessage }` (via `GlobalExceptionHandler`).

## Items & rates flow (important)

### Catalog (structure + defaults)

- `ItemService` defines ~25 items in a **static block**: ids, names, groups, combo refs, and **default rates**.
- Groups (display order): Coat, Shirt-Pant, Kurta-Payjama, Jacket, Miscellaneous.
- Combos reference component item ids via `comboItemIds`.

### Self-service rate overrides (runtime updates)

**Overlay model** — no migration/seeding required:

1. Hardcoded list = structural source of truth + default rates.
2. `itemRate` entities in Datastore store **overrides only** (item id -> rate).
3. `getGroupedItems()` overlays overrides on defaults (`override wins`, else default).
4. `POST /rates` upserts `ItemRate` rows via `ItemRateRepository.saveAll()`.

```
GET /items/groupedItems  -> defaults merged with saved overrides
POST /rates              -> { rates: { itemId: rate, ... } }  (JWT-protected)
```

**Resilience:** if reading overrides from Datastore fails, `loadRateOverrides()` logs a warning and falls back to hardcoded defaults so order creation never breaks.

**Deployment note:** backend can be deployed before the UI — with no overrides saved, `GET /items/groupedItems` returns the same default rates as before. `POST /rates` is additive.

### Orders vs catalog rates

- **Rates are snapshotted into orders**: each `OrderItem` stores its own `rate` at creation time.
- The backend does **not** validate order line rates against the catalog.
- Changing a catalog rate does **not** affect existing orders (historical pricing preserved).

## Security model

- Single hardcoded user `Admin` (BCrypt password in `UserDetailsServiceImpl`).
- No roles/authorities; any authenticated caller can use all endpoints including `POST /rates`.

## Conventions & gotchas

- Lombok used heavily. `OrderConverterConfig` is dead code (commented out).
- `spring.main.allow-circular-references: true`.
- Datastore bean built manually in `CommonConfig` (prod vs emulator).
- New order search filters likely need a composite index in `index.yaml`.
- Temporary task notes may exist in `rate-self-service-intent.md` (delete after merge).
