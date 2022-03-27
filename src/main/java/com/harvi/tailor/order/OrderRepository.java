package com.harvi.tailor.order;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface OrderRepository extends DatastoreRepository<Order, String> {

  List<Order> findAllByOrderNumberOrderByOrderDateDesc(int orderNumber);

  List<Order> findAllByCustomer_MobileOrderByOrderDateDesc(long mobile);

}
