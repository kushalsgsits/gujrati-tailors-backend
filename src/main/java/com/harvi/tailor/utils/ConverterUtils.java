package com.harvi.tailor.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConverterUtils {

  @Autowired private ObjectMapper objectMapper;

  public <T> String objectToJsonString(T obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      // TODO
      e.printStackTrace();
      return null;
    }
  }

  public <T> T jsonStringToObject(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }
}
