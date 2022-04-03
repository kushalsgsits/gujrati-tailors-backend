package com.harvi.tailor.commons;

import com.google.appengine.api.utils.SystemProperty;
import com.google.appengine.api.utils.SystemProperty.Environment.Value;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CommonConfig {

  @Bean
  public Datastore createDatastore() {
    return isProd() ? createDatastoreForProdEnv() : createDatastoreForLocalEnv();
  }

  private Datastore createDatastoreForProdEnv() {
    return DatastoreOptions.getDefaultInstance().getService();
  }

  private Datastore createDatastoreForLocalEnv() {
    DatastoreOptions datastoreOptions = DatastoreOptions.newBuilder()
        .setProjectId("gujrati-tailors-backend")
        .setNamespace("gujrati-tailors-backend")
        .setHost("http://localhost:8081")
        .build();
    return datastoreOptions.getService();
  }

  private boolean isProd() {
    Value environment = SystemProperty.environment.value();
    log.info("Environment={}", environment);
    return Value.Production == environment;
  }
}
