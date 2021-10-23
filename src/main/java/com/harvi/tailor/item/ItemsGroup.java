package com.harvi.tailor.item;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItemsGroup {

    private String groupName;
    private List<Item> groupItems;

    public ItemsGroup(String groupName, List<Item> groupItems) {
        this.groupName = groupName;
        this.groupItems = groupItems;
    }

}
