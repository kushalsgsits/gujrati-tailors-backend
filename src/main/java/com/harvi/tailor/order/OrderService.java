package com.harvi.tailor.order;

import static java.util.stream.StreamSupport.stream;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.EntityQuery;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

  private final Datastore datastore;

  public List<Order> findAllOrderByDeliveryDateDesc(
      Map<String, String> requestParams, Pageable pageable) {
    Query<Entity> query = createQuery(requestParams, pageable);
    QueryResults<Entity> results = datastore.run(query);
    return convertResultsToOrderList(results);
  }

  private EntityQuery createQuery(Map<String, String> requestParams, Pageable pageable) {
    var orderForDeliveryDate = pageable.getSort().getOrderFor("deliveryDate");
    var orderBy =
        null == orderForDeliveryDate || orderForDeliveryDate.getDirection() == Sort.Direction.ASC
            ? OrderBy.asc("deliveryDate")
            : OrderBy.desc("deliveryDate");
    return Query.newEntityQueryBuilder()
        .setKind("order")
        .setFilter(createQueryFilter(requestParams, orderBy.getDirection()))
        .setOrderBy(orderBy)
        .setLimit(pageable.getPageSize())
        .setOffset((int) pageable.getOffset())
        .build();
  }

  private CompositeFilter createQueryFilter(
      Map<String, String> requestParams, OrderBy.Direction sortOrder) {
    CompositeFilter deliveryDateFilter = createDeliveryDateFilter(requestParams, sortOrder);

    List<PropertyFilter> filters = new ArrayList<>();
    createOrderTypeFilter(requestParams, filters);
    createOrderNumberFilter(requestParams, filters);
    createOrderStatusFilter(requestParams, filters);
    createMobileFilter(requestParams, filters);

    return CompositeFilter.and(deliveryDateFilter, filters.toArray(new PropertyFilter[0]));
  }

  private CompositeFilter createDeliveryDateFilter(
      Map<String, String> requestParams, OrderBy.Direction sortOrder) {
    return CompositeFilter.and(
        createDeliveryDateStartFilter(requestParams, sortOrder),
        createDeliveryDateEndFilter(requestParams, sortOrder));
  }

  private PropertyFilter createDeliveryDateStartFilter(
      Map<String, String> requestParams, OrderBy.Direction sortOrder) {
    String deliveryDateStart = requestParams.get("deliveryDateStart");
    Timestamp start;
    if (StringUtils.isNotBlank(deliveryDateStart)) {
      start = Timestamp.parseTimestamp(deliveryDateStart);
    } else {
      start = OrderBy.Direction.ASCENDING == sortOrder ? Timestamp.now() : Timestamp.MIN_VALUE;
    }
    return PropertyFilter.ge("deliveryDate", start);
  }

  private PropertyFilter createDeliveryDateEndFilter(
      Map<String, String> requestParams, OrderBy.Direction sortOrder) {
    String deliveryDateEnd = requestParams.get("deliveryDateEnd");
    Timestamp end;
    if (StringUtils.isNotBlank(deliveryDateEnd)) {
      end = Timestamp.parseTimestamp(deliveryDateEnd);
    } else {
      end = OrderBy.Direction.ASCENDING == sortOrder ? Timestamp.MAX_VALUE : Timestamp.now();
    }
    return PropertyFilter.le("deliveryDate", end);
  }

  private void createOrderTypeFilter(
      Map<String, String> requestParams, List<PropertyFilter> filters) {
    String orderType = requestParams.get("orderType");
    if (StringUtils.isNotBlank(orderType)) {
      filters.add(PropertyFilter.eq("orderType", orderType));
    }
  }

  private void createOrderNumberFilter(
      Map<String, String> requestParams, List<PropertyFilter> filters) {
    String orderNumber = requestParams.get("orderNumber");
    if (StringUtils.isNotBlank(orderNumber)) {
      filters.add(PropertyFilter.eq("orderNumber", Integer.parseInt(orderNumber)));
    }
  }

  private void createOrderStatusFilter(
      Map<String, String> requestParams, List<PropertyFilter> filters) {
    String orderStatus = requestParams.get("orderStatus");
    if (StringUtils.isNotBlank(orderStatus)) {
      filters.add(PropertyFilter.eq("orderStatus", orderStatus));
    }
  }

  private void createMobileFilter(Map<String, String> requestParams, List<PropertyFilter> filters) {
    String mobile = requestParams.get("mobile");
    if (StringUtils.isNotBlank(mobile)) {
      filters.add(PropertyFilter.eq("customer.mobile", Long.parseLong(mobile)));
    }
  }

  private List<Order> convertResultsToOrderList(QueryResults<Entity> results) {
    return stream(Spliterators.spliteratorUnknownSize(results, Spliterator.ORDERED), false)
        .map(Order::createOrderFromEntity)
        .collect(Collectors.toList());
  }
}
