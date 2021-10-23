package com.harvi.tailor.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class Item {

    private String id;
    private String name;
    private String groupName;
    private String type;
    private List<String> comboItemIds;
    private int rate;

    public Item(String id, String name, String groupName, String type, List<String> comboItemIds) {
        this.id = id;
        this.name = name;
        this.groupName = groupName;
        this.type = type;
        this.comboItemIds = comboItemIds;
    }

    public Item(String id, String name, String groupName, String type) {
        this(id, name, groupName, type, null);
    }

    public static class ItemType {
        public static final String COAT = "Coat";
        public static final String SHIRT = "Shirt";
        public static final String PANT = "Pant";
        public static final String KURTA = "Kurta";
        public static final String PAYJAMA = "Payjama";
        public static final String JACKET = "Jacket";
        public static final String SAFARI_SHIRT = "Safari Shirt";
        public static final String OTHERS = "Others";
        public static final String COMBO = "Combo";
    }


    public static class ItemGroup {
        public static final String COAT = "Coat";
        public static final String SHIRT_PANT = "Shirt-Pant";
        public static final String KURTA_PAYJAMA = "Kurta-Payjama";
        public static final String JACKET = "Jacket";
        public static final String MISCELLANEOUS = "Miscellaneous";

        public static final List<String> ORDERED_GROUPS =
                        List.of(COAT, SHIRT_PANT, KURTA_PAYJAMA, JACKET, MISCELLANEOUS);
    }
}
