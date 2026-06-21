package io.bromo.domain.repository;

import io.bromo.domain.model.Event;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Port for event storage. Implemented by Cassandra in {@code infrastructure.persistence.cassandra}.
 * Pure interface - no Spring, no Cassandra imports.
 */
public interface EventRepository {

  /** Persist a single event. Idempotent on event id. */
  void save(Event event);

  /** Look up a single event by id. */
  Optional<Event> findById(String id);

  /**
   * Query events by source within a time window. Sorted by time descending.
   *
   * @param source event source (exact match)
   * @param since inclusive lower bound on event time
   * @param until exclusive upper bound on event time
   * @param limit maximum number of events to return
   */
  List<Event> findBySource(String source, Instant since, Instant until, int limit);
}
