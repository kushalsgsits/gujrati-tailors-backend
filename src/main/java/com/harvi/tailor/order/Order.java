package com.harvi.tailor.order;

import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Value;
import com.google.cloud.spring.data.datastore.core.mapping.Entity;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Entity
public class Order {

  @Id private String id;
  private OrderType orderType;
  private int orderNumber;
  private OrderStatus orderStatus = OrderStatus.CREATED;
  private Instant orderDate;
  private Instant deliveryDate;
  private List<OrderItem> orderItems;
  private Customer customer;
  private int advance;
  private String notes;

  public static Order createOrderFromEntity(com.google.cloud.datastore.Entity entity) {
    FullEntity<IncompleteKey> customerEntity = entity.getEntity("customer");
    Customer customer = Customer.createCustomerFromEntity(customerEntity);

    List<Value<?>> orderItemEntityValues = entity.getList("orderItems");
    List<OrderItem> orderItems =
        orderItemEntityValues.stream()
            .map(OrderItem::createOrderItemFromEntityValue)
            .collect(Collectors.toList());

    Order order = new Order();
    order.setId(entity.getKey().getName());
    order.setOrderType(OrderType.valueOf(entity.getString("orderType")));
    order.setOrderNumber((int) entity.getLong("orderNumber"));
    order.setOrderStatus(OrderStatus.valueOf(entity.getString("orderStatus")));
    order.setOrderDate(Instant.parse(entity.getTimestamp("orderDate").toString()));
    order.setDeliveryDate(Instant.parse(entity.getTimestamp("deliveryDate").toString()));
    order.setOrderItems(orderItems);
    order.setCustomer(customer);
    order.setAdvance((int) entity.getLong("advance"));
    order.setNotes(entity.getString("notes"));

    return order;
  }

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
  @Entity
  public static class OrderItem {

    private String id;
    private int rate;
    private int quantity;

    public static OrderItem createOrderItemFromEntityValue(Value<?> entityValue) {
      OrderItem orderItem = new OrderItem();
      FullEntity orderItemEntity = (FullEntity) entityValue.get();
      orderItem.setId(orderItemEntity.getString("id"));
      orderItem.setRate((int) orderItemEntity.getLong("rate"));
      orderItem.setQuantity((int) orderItemEntity.getLong("quantity"));
      return orderItem;
    }
  }

  @Getter
  @Setter
  @Entity
  public static class Customer {

    private String name;
    private long mobile;

    public static Customer createCustomerFromEntity(FullEntity<IncompleteKey> entity) {
      Customer customer = new Customer();
      customer.setMobile(entity.getLong("mobile"));
      customer.setName(entity.getString("name"));
      return customer;
    }
  }
}
