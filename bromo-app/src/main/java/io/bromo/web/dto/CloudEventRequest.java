package io.bromo.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/**
 * CloudEvents 1.0 JSON envelope accepted by {@code POST /api/v1/events}.
 * {@code data} is opaque - the pipeline does not inspect it.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CloudEventRequest(
        String specversion,
        String id,
        String source,
        String type,
        String time,
        String datacontenttype,
        String subject,
        Map<String, Object> data
) {
}
