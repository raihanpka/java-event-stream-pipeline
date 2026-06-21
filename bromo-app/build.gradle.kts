// Bromo: Spring Boot application module
// Single deployable unit. Internal packages: domain, application, infrastructure, web, bootstrap.
// `java` is declared explicitly so the Kotlin DSL can resolve `implementation`,
// `runtimeOnly`, and `testImplementation` at script-compile time. Spring Boot
// would apply it transitively, but the script-compile classpath is resolved
// before that, so an explicit declaration is required.

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    // bromo-core
    implementation(project(":bromo-core"))

    // Web + actuator
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.springdoc.openapi)

    // Data (all four, but each profile enables only what it needs)
    implementation(libs.spring.boot.starter.data.cassandra)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.data.redis)

    // Kafka
    implementation(libs.spring.kafka)

    // Drivers
    runtimeOnly(libs.postgresql.jdbc)
    implementation(libs.lettuce.core)

    // Flyway
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgresql)

    // Observability
    implementation(libs.micrometer.tracing.bridge.otel)
    implementation(libs.opentelemetry.exporter.otlp)

    // CloudEvents
    implementation(libs.cloudevents.core)
    implementation(libs.cloudevents.json.jackson)
    implementation(libs.cloudevents.http.basic)

    // Test
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.kafka.test)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.kafka)
    testImplementation(libs.testcontainers.cassandra)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.archunit.junit5)
}

// Spring Boot Gradle plugin configures the `bootJar` task and a
// `bootBuildImage` task for Paketo/Buildpacks image builds.
springBoot {
    buildInfo()
}
