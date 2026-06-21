// Bromo: Shared library module
// Pure Java types shared between bromo-app and external consumers (Maven Central).
// No Spring, no Kafka, no framework imports. Zero dependencies.

plugins {
    `java-library`
}

dependencies {
    // Intentionally empty. Pure Java types only.
    testImplementation(platform("org.junit:junit-bom:5.11.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}
