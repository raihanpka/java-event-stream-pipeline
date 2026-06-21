package io.bromo.domain.event;

import java.util.Map;
import java.util.Objects;

/**
 * CloudEvents 1.0 envelope. Carries the required context attributes plus the opaque {@code data}
 * payload. Use {@link #data()} as-is for serialisation; do not wrap or transform the user payload.
 */
public record CloudEvent(
    String specversion,
    String id,
    String source,
    String type,
    String time,
    String datacontenttype,
    String subject,
    Map<String, Object> data) {

  public CloudEvent {
    Objects.requireNonNull(specversion, "specversion");
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(source, "source");
    Objects.requireNonNull(type, "type");
    Objects.requireNonNull(time, "time");
    if (!"1.0".equals(specversion)) {
      throw new IllegalArgumentException("Unsupported CloudEvents specversion: " + specversion);
    }
  }
}
