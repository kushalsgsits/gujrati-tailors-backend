package com.harvi.tailor.order;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;

  public List<Order> searchParticularOrder(String property, String value) {
    switch (property) {
      case "orderNumber":
        return orderRepository.findAllByOrderNumberOrderByOrderDateDesc(Integer.parseInt(value));
      case "mobile":
        return orderRepository.findAllByCustomer_MobileOrderByOrderDateDesc(Long.parseLong(value));
      default:
        throw new RuntimeException(
            String.format("searchParticularOrder by property {} is not supported", property));
    }
  }

}
