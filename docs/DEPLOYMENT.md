# Deployment Guide

This document covers deployment options for Bromo. The recommended paths, in order of complexity:

1. Docker Compose (local dev, single-node VPS)
2. Kubernetes with Helm (production)
3. Build from source (custom deployments)

## Docker Compose

See `infra/docker/` for compose files. Full stack boots all services and infrastructure.

## Kubernetes with Helm

See `infra/helm/bromo/` for chart templates. Supports HPA, probes, ConfigMap, and Secrets.

## GraalVM Native Image

Build with `./gradlew nativeCompile` (requires GraalVM JDK 21). Startup under 100ms, memory ~50 MB RSS.

Full documentation under construction.
