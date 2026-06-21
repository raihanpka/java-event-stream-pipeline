package io.bromo.infrastructure.persistence.cassandra;

import io.bromo.application.service.QueryService;
import io.bromo.domain.model.Event;
import io.bromo.domain.model.Metric;
import io.bromo.domain.repository.EventRepository;
import io.bromo.domain.repository.MetricRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Read-side query adapter. Wires the application service interfaces to the
 * Cassandra repositories. Spring component scan picks it up once
 * {@code CassandraTemplate} is on the classpath.
 */
@Service
@ConditionalOnBean(name = "cassandraTemplate")
public class CassandraQueryAdapter implements QueryService {

    private final EventRepository eventRepository;
    private final MetricRepository metricRepository;

    public CassandraQueryAdapter(EventRepository eventRepository, MetricRepository metricRepository) {
        this.eventRepository = eventRepository;
        this.metricRepository = metricRepository;
    }

    @Override
    public List<Event> findEventsBySource(String source, Instant since, Instant until, int limit) {
        return eventRepository.findBySource(source, since, until, limit);
    }

    @Override
    public List<Metric> findMetrics(String type, String metricName, Instant since, Instant until) {
        return metricRepository.findByTypeAndMetric(type, metricName, since, until);
    }
}
