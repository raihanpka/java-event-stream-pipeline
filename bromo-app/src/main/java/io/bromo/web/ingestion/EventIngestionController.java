package io.bromo.web.ingestion;

import io.bromo.application.service.IngestionService;
import io.bromo.domain.event.CloudEvent;
import io.bromo.domain.model.Event;
import io.bromo.web.dto.CloudEventRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * CloudEvents ingestion endpoint. Accepts a single event per request and returns
 * 202 Accepted with the assigned id.
 */
@RestController
@RequestMapping("/api/v1/events")
public class EventIngestionController {

    private static final Logger log = LoggerFactory.getLogger(EventIngestionController.class);

    private final IngestionService ingestionService;

    public EventIngestionController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> ingest(@Valid @RequestBody CloudEventRequest request) {
        CloudEvent envelope = toEnvelope(request);
        Event event = toDomain(envelope);
        String assignedId = ingestionService.ingest(event);
        log.info("Ingested event id={} type={} source={}", assignedId, event.type(), event.source());
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(Map.of("id", assignedId, "status", "accepted"));
    }

    private static CloudEvent toEnvelope(CloudEventRequest r) {
        return new CloudEvent(
                r.specversion(),
                r.id(),
                r.source(),
                r.type(),
                r.time(),
                r.datacontenttype(),
                r.subject(),
                r.data());
    }

    private static Event toDomain(CloudEvent env) {
        Instant time = Instant.parse(env.time());
        return new Event(
                env.id() == null || env.id().isBlank() ? Event.newId() : env.id(),
                env.source(),
                env.type(),
                time,
                env.data() == null ? Map.of() : env.data(),
                null);
    }
}
