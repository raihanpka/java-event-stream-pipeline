package io.bromo.application.service;

import io.bromo.domain.model.Event;
import io.bromo.domain.model.Metric;
import java.time.Instant;
import java.util.List;

/** Use case: read-side queries for events and metrics. Backed by Cassandra. */
public interface QueryService {

  List<Event> findEventsBySource(String source, Instant since, Instant until, int limit);

  List<Metric> findMetrics(String type, String metricName, Instant since, Instant until);
}
