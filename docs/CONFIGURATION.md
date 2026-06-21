# Configuration Reference

Every Bromo service accepts environment variables and Spring Boot `application.yml` overrides. This document lists all configurable properties.

## Common (all services)

| Variable                   | Default          | Description                    |
|----------------------------|------------------|--------------------------------|
| `KAFKA_BOOTSTRAP_SERVERS`  | `localhost:9092` | Kafka broker connection string |
| `CASSANDRA_CONTACT_POINTS` | `localhost:9042` | Cassandra contact points       |
| `SPRING_PROFILES_ACTIVE`   | `dev`            | Active Spring profile          |

## Per-Service

Documentation under construction. Service-specific properties will be documented as each service is implemented.
