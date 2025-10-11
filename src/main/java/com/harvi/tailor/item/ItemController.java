package com.harvi.tailor.item;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("items")
@CrossOrigin
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @GetMapping("/groupedItems")
  public List<ItemsGroup> getGroupedItems() {
    return itemService.getGroupedItems();
  }
}
