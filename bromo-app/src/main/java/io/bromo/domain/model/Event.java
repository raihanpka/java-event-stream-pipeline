package io.bromo.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Immutable event record. The pipeline does not inspect {@code data} - it is opaque JSON,
 * serialized as-is to Kafka and Cassandra.
 *
 * @param id CloudEvents id (globally unique per event)
 * @param source CloudEvents source identifier (e.g. {@code "my-app"})
 * @param type CloudEvents type (e.g. {@code "com.example.completion.v1"})
 * @param time CloudEvents time
 * @param data CloudEvents data payload (opaque)
 * @param tenantId Optional tenant partitioning key (null = public)
 */
public record Event(
    String id,
    String source,
    String type,
    Instant time,
    Map<String, Object> data,
    String tenantId) {

  public Event {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("Event id is required");
    }
    if (source == null || source.isBlank()) {
      throw new IllegalArgumentException("Event source is required");
    }
    if (type == null || type.isBlank()) {
      throw new IllegalArgumentException("Event type is required");
    }
    if (time == null) {
      throw new IllegalArgumentException("Event time is required");
    }
    if (data == null) {
      throw new IllegalArgumentException("Event data is required");
    }
  }

  /** Generate a new event id (UUIDv4). Caller supplies source/type/time/data. */
  public static String newId() {
    return UUID.randomUUID().toString();
  }
}
