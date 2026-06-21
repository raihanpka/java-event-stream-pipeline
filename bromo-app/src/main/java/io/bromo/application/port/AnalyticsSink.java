package io.bromo.application.port;

/**
 * Output port for analytics export. Implemented by {@code PostHogSink},
 * {@code WebhookSink}, etc. Add your own - the only requirement is async,
 * non-blocking delivery.
 *
 * <p>Pluggability rule (AGENTS.md §2): only add a port interface when a second
 * implementation exists or is expected. {@code AnalyticsSink} qualifies because
 * PostHog and webhooks are both realistic exports.
 */
public interface AnalyticsSink {

    /**
     * Export an event to the sink. Implementations must be non-blocking from
     * the caller's perspective - use a bounded queue and a worker thread.
     *
     * @return true if accepted for delivery, false if rejected (queue full)
     */
    boolean export(String eventJson);

    /**
     * Sink name for logging and metrics.
     */
    String name();
}
