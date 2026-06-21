# ScyllaDB as Cassandra Alternative

Bromo's persistence layer targets Apache Cassandra 4.x by default. ScyllaDB is a fully wire-compatible alternative that can be used as a drop-in replacement without code changes.

## Why ScyllaDB

ScyllaDB is a wide-column store written in C++ that implements the Cassandra Query Language (CQL) protocol. The same Java driver and Spring Data Cassandra starter work for both databases.

| Concern | Apache Cassandra | ScyllaDB |
|---|---|---|
| Protocol | CQL native | CQL compatible |
| Java driver | `com.datastax.oss:java-driver-core` | Same driver, same API |
| Spring Boot starter | `spring-boot-starter-data-cassandra` | Same starter |
| Default port | 9042 | 9042 |
| Connection config | `spring.cassandra.*` | `spring.cassandra.*` |
| CQL support | Full | Full (with shard-aware extensions) |
| Performance | Baseline | Higher throughput, lower p99 (C++ implementation, shard-aware) |

## When to Pick Each

**Apache Cassandra** is the default. Pick this when:
- You want the canonical, most widely deployed option
- You have existing Cassandra expertise on the team
- You need the broadest tooling and operator familiarity

**ScyllaDB** is the alternative. Pick this when:
- You need higher throughput or lower p99 latency on the same hardware
- You want shard-aware drivers for better connection distribution
- You prefer a C++ implementation for operational consistency

## Switching the Backend

### 1. Docker Compose (local dev)

Start ScyllaDB instead of Cassandra:

```bash
docker compose --profile scylladb up -d
```

This starts ScyllaDB on port 9042 instead of Cassandra. The other services (Kafka, PostgreSQL, Redis) are unchanged.

### 2. Application Profile

Activate the ScyllaDB profile when starting the app:

```bash
SPRING_PROFILES_ACTIVE=prod,scylladb ./gradlew bootRun
```

Or set `spring.profiles.active=prod,scylladb` in your environment.

The `application-scylladb.yml` profile points `spring.cassandra.contact-points` at the `scylladb` service in the docker network. No Java code changes are required.

### 3. Kubernetes / Helm

In `infra/helm/bromo/values.yaml`, set:

```yaml
cassandra:
  contactPoints: my-scylla-release.scylla.svc.cluster.local
  useScylla: true
```

And activate the `scylladb` Spring profile on the deployment.

## Code-Level Handling

Because the two databases share the CQL protocol, the application code does not branch on the backend. The `domain.repository.*` ports and `infrastructure.persistence.cassandra.*` adapters work against both.

The single place where the choice shows up is configuration:
- `spring.cassandra.contact-points` points to either the Cassandra or ScyllaDB service
- `spring.cassandra.local-datacenter` must match the deployed cluster's datacenter name (ScyllaDB uses the same naming convention as Cassandra)

## ScyllaDB-Specific Notes

- **Sharding**: ScyllaDB shards data per CPU core. The standard Cassandra driver works but is not shard-aware. For maximum performance, use the [ScyllaDB Java Driver](https://github.com/scylladb/scylla-driver) instead. Bromo's current code uses the standard driver; switching to the ScyllaDB-specific driver is a future enhancement.
- **CQL extensions**: ScyllaDB supports some CQL extensions (per-partition rate limiting, workload attributes) that Cassandra does not. Bromo does not use these.
- **Version support**: ScyllaDB follows a calendar versioning scheme (`2026.1`, `2025.1`, etc.). The two most recent LTS releases are supported. `2026.1` is the current LTS as of June 2026.

## References

- [ScyllaDB Documentation](https://docs.scylladb.com/)
- [ScyllaDB Docker Hub](https://hub.docker.com/r/scylladb/scylla)
- [ScyllaDB Version Support Policy](https://docs.scylladb.com/stable/versioning/version-support.html)
- [Spring Data Cassandra](https://spring.io/projects/spring-data-cassandra)
