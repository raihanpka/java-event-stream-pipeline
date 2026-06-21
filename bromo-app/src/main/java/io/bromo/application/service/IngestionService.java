package io.bromo.application.service;

import io.bromo.domain.model.Event;

/**
 * Use case: accept a CloudEvent from the ingestion API, validate, enrich, and publish to Kafka. The
 * actual persistence happens downstream via Kafka Streams writing to Cassandra.
 */
public interface IngestionService {

  /**
   * Ingest a single event. Returns the assigned event id (echoed for the client to use in
   * subsequent queries).
   *
   * @throws IllegalArgumentException if the event fails validation
   */
  String ingest(Event event);
}
