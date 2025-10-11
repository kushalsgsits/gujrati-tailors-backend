package com.harvi.tailor.commons;

import com.google.cloud.NoCredentials;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import jakarta.annotation.PreDestroy;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class CommonConfig {

  private final Environment environment;

  @Bean
  public Datastore createDatastore() {
    return isProd() ? createDatastoreForProdEnv() : createDatastoreForLocalEnv();
  }

  private Datastore createDatastoreForProdEnv() {
    log.info("Creating Datastore for Prod env");
    return DatastoreOptions.getDefaultInstance().getService();
  }

  private Datastore createDatastoreForLocalEnv() {
    log.info("Creating Datastore for Local env");
    return DatastoreOptions.newBuilder()
        .setProjectId("gujrati-tailors-backend")
        .setNamespace("gujrati-tailors-backend")
        .setHost("http://localhost:8081")
        .setCredentials(NoCredentials.getInstance())
        .build()
        .getService();
  }

  private boolean isProd() {
    // Check if Active profiles contains "prod"
    return Arrays.stream(environment.getActiveProfiles()).anyMatch("prod"::equalsIgnoreCase);
  }

  @PreDestroy
  private void shutdownHook() {
    log.warn("Shutdown hook called in GujratiTailorsApplication");
  }
}
