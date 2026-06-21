# Bromo: Agent Guide

Conventions for contributing to Bromo. One Spring Boot application, internally modular. Pragmatic clean architecture: use the library, do not wrap the library.

---

## 1. Architecture Rules (Enforced by ArchUnit)

- `domain` package has ZERO imports from Spring, Kafka, Cassandra, or any framework. Only standard Java library.
- `application` package has ZERO imports from `infrastructure` or `web`. It imports `domain` only.
- `web` depends on `application` and `domain` only.
- No package may import from `bootstrap`.

Violations fail the build.

## 2. Package Structure

```
io.bromo
+-- domain/model/             # Records: Event, Metric, Alert, ApiKey
+-- domain/event/             # CloudEvents wrapper, type registry
+-- domain/repository/        # Repository interfaces
+-- application/service/      # IngestionService, QueryService, ExportService
+-- application/port/         # Output ports (only if pluggable)
+-- infrastructure/persistence/cassandra/
+-- infrastructure/persistence/postgres/
+-- infrastructure/messaging/ # Kafka producer + Streams topology
+-- infrastructure/export/    # PostHogSink, WebhookSink
+-- web/ingestion/            # POST /api/v1/events
+-- web/query/                # GET /api/v1/events, GET /api/v1/metrics
+-- bootstrap/config/         # @ConfigurationProperties
```

### When to Add a Port Interface

Ask: "Will there ever be a second implementation of this?"

- **Yes** → Port interface. Example: `AnalyticsSink` (PostHog, webhook, Mixpanel).
- **No** → Use the library API directly. Example: Kafka Streams DSL. You will not swap it for Flink.
- **Maybe** → Start without it. Extract when the second implementation appears.

## 3. Code Style

- **Google Java Style.** 120-character line width. Enforced by Spotless.
- **No Lombok.** Java 21 records for data carriers. Explicit constructors for services.
- **No Guava.** Standard library covers collections, strings, and IO.
- **Constructor injection.** Always. No `@Autowired` on fields.
- **No wildcard imports.**
- **Javadoc** on public API methods only. Private methods: name them well.

### Typography

- **No em dash** (Unicode character U+2014, the long horizontal dash) in any output: code, comments, javadoc, markdown, terminal, log messages, commit messages, chat.
  - Use comma, period, or hyphen (`-`) for asides and parentheticals.
  - Replace "X, do not Y" with "X. Do not Y" (full stop) or "X, not Y".
- **En dash** (`–`, U+2013) and **hyphen** (`-`) are allowed, including for repeated words in Indonesian ("kupu-kupu", "abu-abu", "berkali-kali") and ranges ("1-5", "2024-2026").
- **Enforcement:** Spotless checkstyle + reviewer's eye. CI grep for `\u2014` in any committed file should return zero matches.

## 4. Spring Boot

- **Use @ConfigurationProperties**, not @Value.
- **Use Spring starters.** If a starter exists (Kafka, Cassandra, JPA, Redis), use it. Do not write custom connection management.
- **Actuator** on /actuator/health, /actuator/prometheus.
- **Custom health indicators** for Kafka, Cassandra, PostgreSQL, Redis connectivity.

## 5. Kafka

- **Use Spring Kafka APIs directly.** Inject `KafkaTemplate`. Use `@KafkaListener`. Use Kafka Streams DSL directly.
- **Topics:** `{domain}.{purpose}.v{version}`. Example: `raw.events.v1`.
- **Producer:** `acks=all`, `enable.idempotence=true`.
- **Consumer:** `DefaultErrorHandler` with exponential backoff. `DeadLetterPublishingRecoverer` for DLQ.
- **Streams topology** runs in-process via `@EnableKafkaStreams` + `@StreamBuilder`.

## 6. Databases

- **Cassandra:** `CassandraRepository` + `CassandraTemplate`. Prepared statements only. TTL on all tables. No ALLOW FILTERING.
- **PostgreSQL:** `JpaRepository` + native @Query for complex queries. Flyway migrations.
- **Redis:** `RedisTemplate`. TTL on every key. Lua scripts for atomic operations (rate limiting, locks).

## 7. Anti-Patterns

| Pattern                                  | Problem                            | Fix                                   |
|------------------------------------------|------------------------------------|---------------------------------------|
| Wrapping KafkaTemplate in EventPublisher | Adds zero value                    | Inject KafkaTemplate directly         |
| Port interface for every repository      | Overengineering                    | Use CassandraRepository directly      |
| Custom JSON on top of Jackson            | Bloat                              | Configure ObjectMapper                |
| Empty catch blocks                       | Data loss, impossible to debug     | Log and rethrow, or handle explicitly |
| ALLOW FILTERING in Cassandra             | Full table scan, production outage | Design the table for the query        |
| @Data on JPA entities                    | Native image issues                | Explicit getters and setters          |

## 8. Testing

- **Domain + application:** JUnit 5 only. No Spring context. Runs in milliseconds.
- **Integration:** Testcontainers for Kafka, Cassandra, PostgreSQL, Redis.
- **Architecture:** ArchUnit in every module. Verifies package dependency rules.
- **Fixtures:** Deterministic values committed to repo. No random data generation.