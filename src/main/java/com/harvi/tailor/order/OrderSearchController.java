package com.harvi.tailor.order;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin
@RepositoryRestController
@RequiredArgsConstructor
@Slf4j
public class OrderSearchController {

  private final OrderService service;

  // We cannot use @RequestMapping("/orders") at Class level:
  // https://stackoverflow.com/questions/69825704/spring-data-rest-controller-must-not-use-requestmapping-on-class-level-as-this
  @GetMapping("/orders/customSearch")
  ResponseEntity<CollectionModel<PersistentEntityResource>> customSearch(
      @RequestParam Map<String, String> requestParams,
      Pageable pageable,
      PersistentEntityResourceAssembler resourceAssembler) {
    log.debug("Order search request - params: {}, page: {}", requestParams, pageable);
    List<Order> orders = service.findAllOrderByDeliveryDateDesc(requestParams, pageable);
    log.debug("Order search completed - returned {} results", orders.size());
    return ResponseEntity.ok(resourceAssembler.toCollectionModel(orders));
  }
}
