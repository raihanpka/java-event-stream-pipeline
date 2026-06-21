package io.bromo.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Bromo event pipeline entry point.
 *
 * <p>One Spring Boot application hosting the REST API, Kafka producer, Kafka Streams topology,
 * Cassandra/PostgreSQL/Redis persistence, and export scheduler. See {@code docs/ARCHITECTURE.md}.
 */
@SpringBootApplication(scanBasePackages = "io.bromo")
@ConfigurationPropertiesScan(basePackages = "io.bromo.bootstrap.config")
public class BromoApplication {

  public static void main(String[] args) {
    SpringApplication.run(BromoApplication.class, args);
  }
}
