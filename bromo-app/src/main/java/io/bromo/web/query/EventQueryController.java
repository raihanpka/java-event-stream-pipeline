package io.bromo.web.query;

import io.bromo.application.service.QueryService;
import io.bromo.domain.model.Event;
import io.bromo.web.dto.EventResponse;
import java.time.Instant;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Read-side REST API for stored events. */
@RestController
@RequestMapping("/api/v1/events")
public class EventQueryController {

  private final QueryService queryService;

  public EventQueryController(QueryService queryService) {
    this.queryService = queryService;
  }

  @GetMapping
  public ResponseEntity<List<EventResponse>> findBySource(
      @RequestParam String source,
      @RequestParam(required = false) String since,
      @RequestParam(required = false) String until,
      @RequestParam(defaultValue = "100") int limit) {

    Instant sinceTs = since == null ? Instant.EPOCH : Instant.parse(since);
    Instant untilTs = until == null ? Instant.now().plusSeconds(60) : Instant.parse(until);

    List<Event> events = queryService.findEventsBySource(source, sinceTs, untilTs, limit);
    List<EventResponse> response =
        events.stream()
            .map(e -> new EventResponse(e.id(), e.source(), e.type(), e.time(), e.data()))
            .toList();
    return ResponseEntity.ok(response);
  }
}
