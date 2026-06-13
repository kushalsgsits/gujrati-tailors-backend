package com.harvi.tailor.item;

import static com.harvi.tailor.item.Item.ItemGroup;
import static com.harvi.tailor.item.Item.ItemType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

  static final int MIN_RATE = 0;
  static final int MAX_RATE = 9999;

  /** Hardcoded catalog: defines item structure (ids, names, groups, combos) and default rates. */
  private static final List<Item> ITEMS = new ArrayList<>();

  private static final Map<String, Item> ITEMS_BY_ID;

  private final ItemRateRepository itemRateRepository;

  static {
    ITEMS.add(new Item("blazerOrCoat", "Blazer/Coat", 3200, ItemGroup.COAT, ItemType.COAT));
    ITEMS.add(
        new Item(
            "suit2p",
            "2 Piece Suit",
            4000,
            ItemGroup.COAT,
            ItemType.COMBO,
            List.of("blazerOrCoat", "pant", "shirt")));
    ITEMS.add(
        new Item(
            "suit3p",
            "3 Piece Suit",
            5000,
            ItemGroup.COAT,
            ItemType.COMBO,
            List.of("blazerOrCoat", "pant", "waistCoat", "shirt")));
    ITEMS.add(
        new Item(
            "jodhpuriSuit",
            "Jodhpuri Suit",
            4000,
            ItemGroup.COAT,
            ItemType.COMBO,
            List.of("blazerOrCoat", "pant")));
    ITEMS.add(new Item("achkan", "Achkan/Sherwani", 5000, ItemGroup.COAT, ItemType.COAT));
    ITEMS.add(
        new Item(
            "indoWesternSet",
            "Indo Western Set",
            5000,
            ItemGroup.COAT,
            ItemType.COMBO,
            List.of("achkan", "jacket", "pantPayjama")));

    ITEMS.add(
        new Item(
            "shirtPant",
            "Shirt-Pant",
            900,
            ItemGroup.SHIRT_PANT,
            ItemType.COMBO,
            List.of("shirt", "pant")));
    ITEMS.add(new Item("shirt", "Shirt", 400, ItemGroup.SHIRT_PANT, ItemType.SHIRT));
    ITEMS.add(new Item("pant", "Pant", 500, ItemGroup.SHIRT_PANT, ItemType.PANT));
    ITEMS.add(new Item("kurti", "Kurti", 400, ItemGroup.SHIRT_PANT, ItemType.SHIRT));
    ITEMS.add(new Item("patternShirt", "Pattern Shirt", 500, ItemGroup.SHIRT_PANT, ItemType.SHIRT));
    ITEMS.add(new Item("hunterShirt", "Hunter Shirt", 1200, ItemGroup.SHIRT_PANT, ItemType.SHIRT));

    ITEMS.add(
        new Item(
            "kurtaPant",
            "Kurta-Pant",
            850,
            ItemGroup.KURTA_PAYJAMA,
            ItemType.COMBO,
            List.of("kurta", "pantPayjama")));
    ITEMS.add(
        new Item(
            "kurtaPayjama",
            "Kurta-Payjama",
            800,
            ItemGroup.KURTA_PAYJAMA,
            ItemType.COMBO,
            List.of("kurta", "payjama")));
    ITEMS.add(new Item("kurta", "Kurta", 450, ItemGroup.KURTA_PAYJAMA, ItemType.KURTA));
    ITEMS.add(new Item("payjama", "Payjama", 350, ItemGroup.KURTA_PAYJAMA, ItemType.PAYJAMA));
    ITEMS.add(new Item("pantPayjama", "Pant Payjama", 450, ItemGroup.KURTA_PAYJAMA, ItemType.PANT));
    ITEMS.add(
        new Item(
            "pathaniSuit",
            "Pathani Suit",
            1000,
            ItemGroup.KURTA_PAYJAMA,
            ItemType.COMBO,
            List.of("kurta", "payjama")));

    ITEMS.add(new Item("jacket", "Jacket", 1500, ItemGroup.JACKET, ItemType.JACKET));
    ITEMS.add(new Item("hunterJacket", "Hunter Jacket", 1750, ItemGroup.JACKET, ItemType.JACKET));
    ITEMS.add(new Item("waistCoat", "Waist Coat", 1000, ItemGroup.JACKET, ItemType.JACKET));

    // safariShirt wont be visible in UI
    ITEMS.add(
        new Item(
            "safariShirt", "Safari Shirt", 700, ItemGroup.MISCELLANEOUS, ItemType.SAFARI_SHIRT));
    ITEMS.add(
        new Item(
            "safariSuit",
            "Safari Suit",
            1200,
            ItemGroup.MISCELLANEOUS,
            ItemType.COMBO,
            List.of("safariShirt", "pant")));
    ITEMS.add(
        new Item("accessories", "Accessories", 1, ItemGroup.MISCELLANEOUS, ItemType.ACCESSORIES));
    ITEMS.add(new Item("others", "Others", 0, ItemGroup.MISCELLANEOUS, ItemType.OTHERS));

    ITEMS_BY_ID = ITEMS.stream().collect(Collectors.toMap(Item::getId, Function.identity()));
  }

  /** Returns the catalog grouped by category, with any persisted rate overrides applied. */
  public List<ItemsGroup> getGroupedItems() {
    Map<String, Integer> rateOverrides = loadRateOverrides();
    Map<String, List<Item>> groupNameToItemsMap =
        ITEMS.stream()
            .map(item -> withRate(item, rateOverrides.getOrDefault(item.getId(), item.getRate())))
            .collect(Collectors.groupingBy(Item::getGroupName));
    return ItemGroup.ORDERED_GROUPS.stream()
        .map(groupName -> new ItemsGroup(groupName, groupNameToItemsMap.get(groupName)))
        .toList();
  }

  /**
   * Persists rate overrides for the given items. {@code rates} maps item id to new rate. Validates
   * that ids are known and rates are within [MIN_RATE, MAX_RATE].
   */
  public void updateRates(Map<String, Integer> rates) {
    if (rates == null || rates.isEmpty()) {
      throw new IllegalArgumentException("rates must be non-null and non-empty");
    }
    List<ItemRate> toSave = new ArrayList<>();
    for (Map.Entry<String, Integer> entry : rates.entrySet()) {
      String itemId = entry.getKey();
      Integer rate = entry.getValue();
      if (!ITEMS_BY_ID.containsKey(itemId)) {
        throw new IllegalArgumentException("Unknown item id: " + itemId);
      }
      if (rate == null || rate < MIN_RATE || rate > MAX_RATE) {
        throw new IllegalArgumentException(
            "Rate for '" + itemId + "' must be between " + MIN_RATE + " and " + MAX_RATE);
      }
      toSave.add(new ItemRate(itemId, rate));
    }
    itemRateRepository.saveAll(toSave);
  }

  private Map<String, Integer> loadRateOverrides() {
    Map<String, Integer> overrides = new HashMap<>();
    try {
      itemRateRepository
          .findAll()
          .forEach(itemRate -> overrides.put(itemRate.getId(), itemRate.getRate()));
    } catch (Exception e) {
      // Keep the catalog working exactly as before (hardcoded default rates) even if the rate
      // override store is unavailable. getGroupedItems() must never fail because of overrides.
      log.warn("Failed to load item rate overrides; falling back to default rates", e);
    }
    return overrides;
  }

  private static Item withRate(Item item, int rate) {
    return item.toBuilder().rate(rate).build();
  }
}
