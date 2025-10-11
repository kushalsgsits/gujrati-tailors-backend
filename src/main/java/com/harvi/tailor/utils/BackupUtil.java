package com.harvi.tailor.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harvi.tailor.order.Order.Customer;
import com.harvi.tailor.order.Order.OrderItem;
import com.harvi.tailor.order.Order.OrderStatus;
import com.harvi.tailor.order.Order.OrderType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BackupUtil {

  public static void main(String[] args) throws URISyntaxException, IOException {
    String allOrdersString = readFileAsString("all-orders-22-Sept-2024.json");
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode allOrdersJson = objectMapper.readTree(allOrdersString);
    List<CustomOrder> orders =
        StreamSupport.stream(allOrdersJson.spliterator(), false)
            .map(jsonNode -> objectMapper.convertValue(jsonNode, CustomOrder.class))
            .collect(Collectors.toList());

    Set<CustomOrder> uniqueCoatOrders =
        orders.stream()
            .filter(order -> order.getOrderType() == OrderType.COAT)
            .collect(
                Collectors.groupingBy(
                    o -> o.getCustomer().getMobile(), Collectors.toCollection(TreeSet::new)))
            .values()
            .stream()
            .map(TreeSet::first)
            .collect(Collectors.toSet());

    Set<CustomOrder> uniqueRegularOrders =
        orders.stream()
            .filter(order -> order.getOrderType() == OrderType.REGULAR)
            .filter(order -> !uniqueCoatOrders.contains(order))
            .collect(Collectors.toSet());

    System.out.println("Coat Orders: " + uniqueCoatOrders.size());
    System.out.println("Regular Orders: " + uniqueRegularOrders.size());

    writeOrdersToExcel(uniqueCoatOrders);
    writeOrdersToExcel(uniqueRegularOrders);
    writeAllOrdersToExcel(orders);
  }

  public static void writeOrdersToExcel(Set<CustomOrder> orders) throws IOException {
    OrderType orderType = orders.iterator().next().getOrderType();
    String fileName = "Orders-" + orderType + ".xlsx";
    // Create a workbook and sheet
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Orders-" + orderType);

    // Create header row
    String[] columns = {"Name", "Mobile"};
    Row headerRow = sheet.createRow(0);
    for (int i = 0; i < columns.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(columns[i]);
    }

    // Stream through orders and write them to Excel rows
    AtomicInteger rowIdx = new AtomicInteger(1);
    orders.forEach(
        order -> {
          Row row = sheet.createRow(rowIdx.getAndIncrement());
          row.createCell(0).setCellValue(order.getCustomer().getName());
          row.createCell(1).setCellValue(order.getCustomer().getMobile());
        });

    // Resize columns to fit content
    for (int i = 0; i < columns.length; i++) {
      sheet.autoSizeColumn(i);
    }

    // Write the workbook to file
    try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
      workbook.write(fileOut);
    }

    // Close the workbook
    workbook.close();
    System.out.println("Excel file created: " + fileName);
  }

  public static void writeAllOrdersToExcel(List<CustomOrder> orders) throws IOException {
    String fileName = "AllOrders.xlsx";
    // Create a workbook and sheet
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("AllOrders");

    // Create header row
    String[] columns = {"OrderType", "OrderDate", "Name", "Mobile", "OrderItems"};
    Row headerRow = sheet.createRow(0);
    for (int i = 0; i < columns.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(columns[i]);
    }

    // Stream through orders and write them to Excel rows
    AtomicInteger rowIdx = new AtomicInteger(1);
    orders.forEach(
        order -> {
          Row row = sheet.createRow(rowIdx.getAndIncrement());
          row.createCell(0).setCellValue(order.getOrderType().name());
          row.createCell(1).setCellValue(order.getOrderDate());
          row.createCell(2).setCellValue(order.getCustomer().getName());
          row.createCell(3).setCellValue(order.getCustomer().getMobile());
          row.createCell(4)
              .setCellValue(
                  order.getOrderItems().stream()
                      .map(OrderItem::getId)
                      .collect(Collectors.joining(",")));
        });

    // Resize columns to fit content
    for (int i = 0; i < columns.length; i++) {
      sheet.autoSizeColumn(i);
    }

    // Write the workbook to file
    try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
      workbook.write(fileOut);
    }

    // Close the workbook
    workbook.close();
    System.out.println("Excel file created: " + fileName);
  }

  public static String readFileAsString(String... path) throws URISyntaxException, IOException {
    InputStreamReader isReader = new InputStreamReader(readFile(path));
    BufferedReader reader = new BufferedReader(isReader);
    StringBuilder sb = new StringBuilder();
    String str;
    while ((str = reader.readLine()) != null) {
      sb.append(str);
    }
    return sb.toString();
  }

  public static InputStream readFile(String... path) throws URISyntaxException, IOException {

    java.net.URL url = MethodHandles.lookup().lookupClass().getResource("/");
    java.nio.file.Path folderPath = java.nio.file.Paths.get(url.toURI());
    java.nio.file.Path resPath = Paths.get(folderPath.toString(), path);
    return new FileInputStream(new File(resPath.toUri()));
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class CustomOrder implements Comparable<CustomOrder> {

    private String id;
    private OrderType orderType;
    private int orderNumber;
    private OrderStatus orderStatus = OrderStatus.CREATED;
    private Date orderDate;
    private Date deliveryDate;
    private List<OrderItem> orderItems;
    private Customer customer;
    private int advance;
    private String notes;

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      CustomOrder that = (CustomOrder) o;
      return Objects.equals(customer.getMobile(), that.customer.getMobile());
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(customer.getMobile());
    }

    @Override
    public int compareTo(CustomOrder o) {
      int mob = Long.compare(this.customer.getMobile(), o.customer.getMobile());
      if (mob == 0) {
        return CharSequence.compare(this.customer.getName(), o.customer.getName()) * -1;
      } else {
        return mob;
      }
    }
  }
}
