package com.harvi.tailor;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler({Exception.class})
  public ResponseEntity<Map<String, String>> handleException(Exception e) {
    log.error("Caught by GlobalExceptionHandler", e);
    Map<String, String> response = Map.of(
        "errorMessage", "Internal server error, please try again.",
        "internalErrorMessage", e.getMessage()
    );
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
