package com.harvi.tailor.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rates")
@CrossOrigin
@RequiredArgsConstructor
public class RateController {

  private final ItemService itemService;

  @PostMapping
  public void updateRates(@RequestBody RatesUpdateRequest request) {
    itemService.updateRates(request.rates());
  }
}
