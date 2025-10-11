package com.harvi.tailor.item;

import static com.harvi.tailor.item.Item.ItemGroup;
import static com.harvi.tailor.item.Item.ItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

  private static final List<Item> ITEMS = new ArrayList<>();
  private static final List<ItemsGroup> GROUPED_ITEMS;

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

    Map<String, List<Item>> groupNameToItemsMap =
        ITEMS.stream().collect(Collectors.groupingBy(Item::getGroupName));
    GROUPED_ITEMS =
        ItemGroup.ORDERED_GROUPS.stream()
            .map(groupName -> new ItemsGroup(groupName, groupNameToItemsMap.get(groupName)))
            .collect(Collectors.toList());
  }

  public List<ItemsGroup> getGroupedItems() {
    return GROUPED_ITEMS;
  }
}
