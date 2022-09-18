package com.harvi.tailor.item;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.harvi.tailor.item.Item.ItemGroup;
import static com.harvi.tailor.item.Item.ItemType;

@Service
public class ItemService {

    private static final List<Item> ITEMS = new ArrayList<>();

    private static final Map<String, Item> ITEM_ID_TO_ITEM_MAP = new HashMap<>();

    private static final List<ItemsGroup> GROUPED_ITEMS;

    static {
        // All item names:
        // 'Kurta', 'Payjama', 'Pant Payjama', 'Pathani', 'Kurti', 'Jacket', 'Safari',
        // 'Waist Coat', 'Others'
        ITEMS.add(new Item("blazerOrCoat", "Blazer/Coat", 3000, ItemGroup.COAT, ItemType.COAT));
        ITEMS.add(new Item("suit2p", "2 Piece Suit", 3500, ItemGroup.COAT, ItemType.COMBO, List.of("blazerOrCoat", "pant")));
        ITEMS.add(new Item("suit3p", "3 Piece Suit", 4500, ItemGroup.COAT, ItemType.COMBO,
                        List.of("blazerOrCoat", "pant", "waistCoat")));
        ITEMS.add(new Item("jodhpuriSuit", "Jodhpuri Suit", 3800, ItemGroup.COAT, ItemType.COMBO,
                        List.of("blazerOrCoat", "pant")));
        ITEMS.add(new Item("achkan", "Achkan", 4000, ItemGroup.COAT, ItemType.COAT));

        ITEMS.add(new Item("shirtPant", "Shirt-Pant", 750, ItemGroup.SHIRT_PANT, ItemType.COMBO,
                        List.of("shirt", "pant")));
        ITEMS.add(new Item("shirt", "Shirt", 350, ItemGroup.SHIRT_PANT, ItemType.SHIRT));
        ITEMS.add(new Item("pant", "Pant", 450, ItemGroup.SHIRT_PANT, ItemType.PANT));
        ITEMS.add(new Item("jeans", "Jeans", 500, ItemGroup.SHIRT_PANT, ItemType.PANT));
        ITEMS.add(new Item("kurti", "Kurti", 300, ItemGroup.SHIRT_PANT, ItemType.SHIRT));

        ITEMS.add(new Item("kurta", "Kurta", 400, ItemGroup.KURTA_PAYJAMA, ItemType.KURTA));
        ITEMS.add(new Item("payjama", "Payjama", 300, ItemGroup.KURTA_PAYJAMA, ItemType.PAYJAMA));
        ITEMS.add(new Item("pantPayjama", "Pant Payjama", 400, ItemGroup.KURTA_PAYJAMA, ItemType.PANT));
        ITEMS.add(new Item("pathaniSuit", "Pathani Suit", 800, ItemGroup.KURTA_PAYJAMA, ItemType.COMBO,
                        List.of("kurta", "payjama")));

        ITEMS.add(new Item("jacket", "Jacket", 1500, ItemGroup.JACKET, ItemType.JACKET));
        ITEMS.add(new Item("waistCoat", "Waist Coat", 1000, ItemGroup.JACKET, ItemType.JACKET));

        // safariShirt wont be visible in UI
        ITEMS.add(new Item("safariShirt", "Safari Shirt", 400, ItemGroup.MISCELLANEOUS, ItemType.SAFARI_SHIRT));
        ITEMS.add(new Item("safariSuit", "Safari Suit", 800, ItemGroup.MISCELLANEOUS, ItemType.COMBO,
                        List.of("safariShirt", "pant")));
        ITEMS.add(new Item("others", "Others", 0, ItemGroup.MISCELLANEOUS, ItemType.OTHERS));

        ITEMS.forEach(item -> ITEM_ID_TO_ITEM_MAP.put(item.getId(), item));

        Map<String, List<Item>> groupNameToItemsMap = ITEMS.stream().collect(Collectors.groupingBy(Item::getGroupName));
        GROUPED_ITEMS = ItemGroup.ORDERED_GROUPS.stream()
                        .map(groupName -> new ItemsGroup(groupName, groupNameToItemsMap.get(groupName)))
                        .collect(Collectors.toList());
    }

    public List<Item> getAllItems() {
        return ITEMS;
    }

    public Map<String, Item> getItemIdToItemMap() {
        return ITEM_ID_TO_ITEM_MAP;
    }

    public List<ItemsGroup> getGroupedItems() {
        return GROUPED_ITEMS;
    }
}
