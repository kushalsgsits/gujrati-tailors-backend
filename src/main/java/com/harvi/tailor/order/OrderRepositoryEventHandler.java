package com.harvi.tailor.order;

import com.google.cloud.spring.data.datastore.core.DatastoreTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
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
        // If orderId is present as path param then Spring data rest automatically
        // 1. sets in Order object created from payload
        // 2. checks existence of Order in DB for given orderId

        // If we have reached here then means either
        // 1. orderId does not exist as path param or
        // 2. order does not exist for given orderId
        // and hence this request will be considered as CREATE request rather than SAVE request

        if (Objects.nonNull(order.getId())) {
            // This is Case 2 i.e order does not exist for given orderId
            // Only thing that needs to be checked is that given orderId should match with computed orderId from payload
            validateOrderId(order);
        } else {
            // This is Case 1 i.e. orderId is not given
            computeAndSetOrderId(order);
        }
    }

    @HandleBeforeSave
    public void beforeSave(Order order) {
        // If orderId is present as path param then Spring data rest automatically
        // 1. sets in Order object created from payload
        // 2. checks existence of Order in DB for given orderId

        // If we have reached here then means order exists for given orderId
        // and hence this request will be considered as SAVE (UPDATE) request

        // Only thing that needs to be checked is that given orderId should match with computed orderId from payload
        validateOrderId(order);
    }

    private void computeAndSetOrderId(Order order) {
        String id = computeId(order);
        order.setId(id);
    }

    /**
     * If orderId is present in path param then it should match with computed orderId from payload
     *
     * @param order
     */
    private void validateOrderId(Order order) {
        String computedId = computeId(order);
        if (!Objects.equals(computedId, order.getId())) {
            throw new IllegalArgumentException(
                            String.format("orderId(=%s) from path param and computed orderId(=%s) from payload does not match",
                                            order.getId(), computedId));
        }
    }

    private String computeId(Order order) {
        return order.getOrderDate().toString().substring(0, "yyyy-MM-".length()) + order.getOrderType()
                        .getShortName() + "-" + order.getOrderNumber();
    }

}
