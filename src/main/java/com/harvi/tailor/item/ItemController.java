package com.harvi.tailor.item;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<List<ItemsGroup>> getGroupedItems() {
    // Rates can change at runtime, so never let HTTP caches serve a stale catalog.
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(itemService.getGroupedItems());
  }
}
