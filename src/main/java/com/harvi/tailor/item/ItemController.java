package com.harvi.tailor.item;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("items")
@CrossOrigin
public class ItemController {

  @Autowired private ItemService itemService;

  @GetMapping("/groupedItems")
  public List<ItemsGroup> getGroupedItems() {
    return itemService.getGroupedItems();
  }
}
