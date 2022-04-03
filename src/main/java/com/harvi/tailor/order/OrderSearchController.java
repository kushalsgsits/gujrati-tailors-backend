package com.harvi.tailor.order;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin
@RepositoryRestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderSearchController {

  private final OrderService service;

  @GetMapping("customSearch")
  ResponseEntity<CollectionModel<PersistentEntityResource>> searchMany(
      @RequestParam Map<String, String> requestParams,
      Pageable pageable,
      PersistentEntityResourceAssembler resourceAssembler) {

    List<Order> orders = service.findAllOrderByDeliveryDateDesc(requestParams, pageable);
    return ResponseEntity.ok(resourceAssembler.toCollectionModel(orders));
  }

}
