package com.harvi.tailor.order;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OrderSearchController {

  @Autowired
  private OrderService service;

  @GetMapping("searchOne")
  ResponseEntity<CollectionModel<PersistentEntityResource>> searchParticular(
      @RequestParam(name = "property") String property,
      @RequestParam(name = "value") String value,
      PersistentEntityResourceAssembler resourceAssembler) {
    List<Order> orders = service.searchParticularOrder(property, value);
    return ResponseEntity.ok(resourceAssembler.toCollectionModel(orders));
  }
}
