package com.harvi.tailor.order;

import com.google.cloud.spring.data.datastore.repository.DatastoreRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface OrderRepository extends DatastoreRepository<Order, String> {}
