package io.bromo.infrastructure.config;

import io.bromo.application.service.QueryService;
import io.bromo.domain.model.Event;
import io.bromo.domain.model.Metric;
import io.bromo.domain.repository.EventRepository;
import io.bromo.domain.repository.MetricRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InMemoryConfig {

  @Bean
  public EventRepository eventRepository() {
    return new InMemoryEventRepository();
  }

  @Bean
  public MetricRepository metricRepository() {
    return new InMemoryMetricRepository();
  }

  @Bean
  @ConditionalOnMissingBean(QueryService.class)
  public QueryService queryService(
      EventRepository eventRepository, MetricRepository metricRepository) {
    return new QueryService() {
      @Override
      public List<Event> findEventsBySource(
          String source, Instant since, Instant until, int limit) {
        return eventRepository.findBySource(source, since, until, limit);
      }

      @Override
      public List<Metric> findMetrics(
          String type, String metricName, Instant since, Instant until) {
        return metricRepository.findByTypeAndMetric(type, metricName, since, until);
      }
    };
  }

  static final class InMemoryEventRepository implements EventRepository {
    private final ConcurrentMap<String, Event> store = new ConcurrentHashMap<>();

    @Override
    public void save(Event event) {
      store.put(event.id(), event);
    }

    @Override
    public Optional<Event> findById(String id) {
      return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Event> findBySource(String source, Instant since, Instant until, int limit) {
      return store.values().stream()
          .filter(e -> source.equals(e.source()))
          .filter(e -> !e.time().isBefore(since) && e.time().isBefore(until))
          .sorted((a, b) -> b.time().compareTo(a.time()))
          .limit(limit)
          .toList();
    }
  }

  static final class InMemoryMetricRepository implements MetricRepository {
    private final List<Metric> store = new CopyOnWriteArrayList<>();

    @Override
    public void save(Metric metric) {
      store.add(metric);
    }

    @Override
    public List<Metric> findByTypeAndMetric(
        String type, String metricName, Instant since, Instant until) {
      return store.stream()
          .filter(m -> type.equals(m.type()) && metricName.equals(m.metricName()))
          .filter(m -> !m.bucketStart().isBefore(since) && m.bucketStart().isBefore(until))
          .toList();
    }
  }
}
