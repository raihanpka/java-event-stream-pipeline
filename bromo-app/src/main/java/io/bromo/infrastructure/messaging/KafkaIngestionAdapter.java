package io.bromo.infrastructure.messaging;

import io.bromo.application.service.IngestionService;
import io.bromo.domain.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Ingestion adapter: publishes validated events to Kafka topic {@code raw.events.v1}.
 * Uses {@code KafkaTemplate} directly per AGENTS.md §5 (do not wrap the template).
 *
 * <p>Producer config (defined in {@code application.yml}): {@code acks=all},
 * {@code enable.idempotence=true}.
 */
@Service
public class KafkaIngestionAdapter implements IngestionService {

    private static final Logger log = LoggerFactory.getLogger(KafkaIngestionAdapter.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    public KafkaIngestionAdapter(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${bromo.kafka.topics.raw:raw.events.v1}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public String ingest(Event event) {
        kafkaTemplate.send(topic, event.id(), event);
        log.debug("Published event id={} to topic={}", event.id(), topic);
        return event.id();
    }
}
