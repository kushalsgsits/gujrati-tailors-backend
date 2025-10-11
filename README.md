# Gujrati Tailors Backend

Spring Boot REST API for managing tailor orders with Google Cloud Datastore.

## Tech Stack
- **Spring Boot**: 3.3.4
- **Java**: 21
- **Database**: Google Cloud Datastore
- **Security**: JWT Authentication

## Prerequisites
- Java 21
- Maven 3.6+
- Google Cloud Datastore Emulator (for local development)

## Running Locally

### From Command Line

**macOS/Linux**:
```bash
source set_vars.sh && mvn spring-boot:run
```

**Windows**:
```cmd
set_vars.cmd && mvn spring-boot:run
```

### From IntelliJ IDEA

1. Go to Run → Edit Configurations
2. Add environment variable:
   ```
   DATASTORE_EMULATOR_HOST=localhost:8081
   ```
3. Run the application

> **Note**: IntelliJ only needs the environment variable. The `set_vars.sh` script sets multiple variables, but only `DATASTORE_EMULATOR_HOST` is required.

## Deployment

**Deploy to Google App Engine**:
```bash
./deploy.sh
```
