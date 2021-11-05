package com.harvi.tailor.order;

import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Order {
    @Id
    private String id;
    private OrderType orderType;
    private int orderNumber;
    private OrderStatus orderStatus = OrderStatus.CREATED;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private List<OrderItem> orderItems;
    private Customer customer;
    private int advance;
    private String notes;


    public enum OrderType {
        COAT,
        REGULAR;

        public String getShortName() {
            return this.name().substring(0, 1);
        }
    }


    public enum OrderStatus {
        CREATED,
        READY,
        DELIVERED,
        DELIVERED_UNPAID,
    }


    @Getter
    @Setter
    public static class OrderItem {
        private String id;
        private int rate;
        private int quantity;
    }


    @Getter
    @Setter
    public static class Customer {
        private String name;
        private long mobile;
    }

}
