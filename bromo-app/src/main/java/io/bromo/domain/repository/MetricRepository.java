package io.bromo.domain.repository;

import io.bromo.domain.model.Metric;

import java.time.Instant;
import java.util.List;

/**
 * Port for pre-aggregated metric storage. Implemented by Cassandra.
 */
public interface MetricRepository {

    /**
     * Persist (upsert) a metric. Cassandra is idempotent on the primary key
     * (type, metricName, bucketStart).
     */
    void save(Metric metric);

    /**
     * Query metrics within a time window.
     */
    List<Metric> findByTypeAndMetric(String type, String metricName, Instant since, Instant until);
}
