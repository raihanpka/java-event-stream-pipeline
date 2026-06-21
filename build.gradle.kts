// Bromo: Root build
// Single deployable unit. Subprojects: bromo-core (shared library) + bromo-app (application).
// The `java` plugin is applied per subproject via its own `plugins { }` block
// (`java-library` for bromo-core, transitively via `spring-boot` for bromo-app).
// Shared configuration is registered as a deferred callback that fires once
// each subproject has applied the Java plugin.

plugins {
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependency.management) apply false
}

subprojects {
    group = "io.bromo"
    version = "0.1.0-SNAPSHOT"

    plugins.withType<JavaPlugin> {
        extensions.configure<JavaPluginExtension> {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(21))
            }
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(
            listOf(
                "-Xlint:all",
                "-Xlint:-processing",
                "-parameters",
            ),
        )
    }
}
