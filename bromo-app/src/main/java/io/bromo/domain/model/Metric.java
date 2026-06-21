package io.bromo.domain.model;

import java.time.Instant;
import java.time.YearMonth;

/**
 * Pre-aggregated metric computed by Kafka Streams. Hourly window per (type, metricName).
 *
 * @param type          event type the metric is computed from
 * @param metricName    metric name (e.g. {@code "count"}, {@code "tokens.total"})
 * @param bucketStart   hour bucket start (UTC)
 * @param value         aggregate value (sum, count, etc. - semantics depend on metricName)
 */
public record Metric(
        String type,
        String metricName,
        Instant bucketStart,
        double value
) {

    /**
     * Compose the Cassandra partition key for {@code metrics_by_hour}.
     * Format: {@code "<type>|<metricName>|<yyyy-MM>"}.
     */
    public String partitionKey() {
        return type + "|" + metricName + "|" + YearMonth.from(bucketStart);
    }
}
