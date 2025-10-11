package com.harvi.tailor.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConverterUtils {

  private final ObjectMapper objectMapper;

  public <T> String objectToJsonString(T obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      log.error("Failed to convert object to JSON string", e);
      throw new RuntimeException("JSON serialization failed", e);
    }
  }

  public <T> T jsonStringToObject(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      log.error("Failed to convert JSON string to object", e);
      throw new RuntimeException("JSON deserialization failed", e);
    }
  }
}
