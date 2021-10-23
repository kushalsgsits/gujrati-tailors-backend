package com.harvi.tailor.order;

import com.google.cloud.spring.data.datastore.core.DatastoreTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RepositoryEventHandler
public class OrderRepositoryEventHandler {

    @Autowired
    private DatastoreTemplate datastoreTemplate;

    @HandleBeforeCreate
    public void beforeCreate(Order order) {
        createAndSetId(order);
        checkDuplicate(order);
    }

    private void createAndSetId(Order order) {
        String id = order.getOrderDate().toString().substring(0, "yyyy-MM-".length()) + order.getOrderType()
                        .getShortName() + "-" + order.getOrderNumber();
        order.setId(id);
    }

    private void checkDuplicate(Order order) {
        Order duplicateOrder = datastoreTemplate.findById(order.getId(), Order.class);
        if (Objects.nonNull(duplicateOrder)) {
            throw new IllegalArgumentException(String.format("Order with id=%s already exists", order.getId()));
        }
    }
}
