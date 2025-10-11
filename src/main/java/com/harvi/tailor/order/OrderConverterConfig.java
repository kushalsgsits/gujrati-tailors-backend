package com.harvi.tailor.order;

import com.google.cloud.spring.data.datastore.core.convert.DatastoreCustomConversions;
import com.harvi.tailor.order.Order.Customer;
import com.harvi.tailor.order.Order.OrderItem;
import com.harvi.tailor.utils.ConverterUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

// @Configuration
@RequiredArgsConstructor
public class OrderConverterConfig {

  private final ConverterUtils converterUtils;

  @Bean
  public DatastoreCustomConversions datastoreCustomConversions() {
    // NOTE: Do not convert converters to lambda or method reference bcz of this
    // issue:
    // https://github.com/arangodb/spring-data/issues/120
    Converter<Customer, String> CUSTOMER_TO_STRING_CONVERTER =
        new Converter<Customer, String>() {
          @Override
          public String convert(Customer customer) {
            return converterUtils.objectToJsonString(customer);
          }
        };
    Converter<String, Customer> STRING_TO_CUSTOMER_CONVERTER =
        new Converter<String, Customer>() {
          @Override
          public Customer convert(String jsonString) {
            return converterUtils.jsonStringToObject(jsonString, Customer.class);
          }
        };

    Converter<OrderItem, String> ORDER_ITEM_TO_STRING_CONVERTER =
        new Converter<OrderItem, String>() {
          @Override
          public String convert(OrderItem orderItem) {
            return converterUtils.objectToJsonString(orderItem);
          }
        };
    Converter<String, OrderItem> STRING_TO_ORDER_ITEM_CONVERTER =
        new Converter<String, OrderItem>() {
          @Override
          public OrderItem convert(String jsonString) {
            return converterUtils.jsonStringToObject(jsonString, OrderItem.class);
          }
        };

    return new DatastoreCustomConversions(List.of(/*
						 * CUSTOMER_TO_STRING_CONVERTER, STRING_TO_CUSTOMER_CONVERTER,
						 * ORDER_ITEM_TO_STRING_CONVERTER, STRING_TO_ORDER_ITEM_CONVERTER
						 */ ));
  }
}
