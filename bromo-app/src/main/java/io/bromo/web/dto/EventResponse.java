package io.bromo.web.dto;

import java.time.Instant;

/**
 * Response shape for ingested and queried events. Returned with HTTP 202 on
 * ingestion and as JSON arrays on query.
 */
public record EventResponse(
        String id,
        String source,
        String type,
        Instant time,
        Object data
) {
}
