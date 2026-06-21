# Event Schema

Bromo does not require specific event types. CloudEvents 1.0 is the only contract. Define your own types for your domain.

## The Only Requirement

Every event must be valid CloudEvents 1.0:

```json
{
  "specversion": "1.0",
  "id": "your-unique-id",
  "source": "your-system-name",
  "type": "your.domain.event.v1",
  "time": "2026-06-19T10:30:00Z",
  "data": {
    "your": "fields here"
  }
}
```

## Example Types (Shipped)

These are examples to make the project immediately runnable. Replace them with your own.

| Type                        | Purpose                | data fields                        |
|-----------------------------|------------------------|------------------------------------|
| `com.example.completion.v1` | LLM completion example | provider, model, tokens, latencyMs |
| `com.example.action.v1`     | Action example         | action, durationMs, status         |
| `com.example.feedback.v1`   | User feedback example  | score, category                    |

## Adding Your Own Types

1. Choose a type name: `{domain}.{name}.v{version}`.
2. Send events with that type to the ingestion API.
3. Add a processor topology branch for it in `bromo-processor`.
4. Add a Cassandra table or reuse the generic `events_by_source` table.

The pipeline infrastructure (Kafka, Cassandra, outbox/inbox) does not change when you add types.
