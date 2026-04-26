# Banking Kata - Hexagonal Architecture

A banking kata built with **Hexagonal Architecture** (Ports & Adapters) and **TDD** in Java / Spring Boot.

![CI](https://github.com/ba-amanou/banking-kata-hexagonal/actions/workflows/ci.yml/badge.svg)

## Purpose

This project implements a banking kata (creation, deposit, withdrawal, balance) as a showcase of clean architecture in Java. The domain has zero framework dependency. Spring, JPA, and HTTP all depend inward on it, never the opposite.

### Key features:
- Account creation, deposit, withdrawal and balance
- Fully tested stack: unit, integration, architecture and E2E tests
- Coverage gate enforced in CI (80% lines / 75% branches)

### Practice and showcase:
- Hexagonal Architecture (clean separation of domain / adapters)
- Domain-Driven Design building blocks
- Test-Driven Development (TDD)
- Multi-module Maven project
- Database migrations with Flyway
- Integration tests with Testcontainers
- Architecture rules enforces with ArchUnit
- API documentation with Swagger (SpringDoc OpenAPI)
- Containerisation with Docker and Docker Compose
- Git best practices (conventional commits, feature branches, pull requests)

## Tech Stack

- Java 21
- Spring Boot 4.x
- PostgreSQL + Spring Data JPA + Flyway
- JUnit 5 + Mockito + AssertJ
- Testcontainers + ArchUnit + Jacoco
- GitHub Actions (CI)

## Getting started

Prerequisites: Java 21, Docker
```
# 1. Clone the repository
git clone https://github.com/ba-amanou/banking-kata-hexagonal.git
cd banking-kata-hexagonal

# 2. Configure environment variables
cp .env.example .env

# 3. Build and run
# macOS / Windows: make sure Docker Desktop is running first
docker compose up --build

# Once running, the API documentation is available at `http://localhost:8080/swagger-ui.html`
```

## Project Structure

```
banking-kata/
├── domain/          # Aggregates, value objects, ports - pure Java, no framework
├── application/     # Use case implementations (DepositMoneyService, WithdrawMoneyService...)
├── infrastructure/  # REST adapters (inbound) + JPA adapters (outbound)
├── bootstrap/       # Spring Boot entry point + E2E tests
└── coverage/        # Aggregated Jacoco report module
```
The project is split into 5 Maven modules, each with a clear responsibility.

**domain** is the heart of the application. It contains the business entities, the business logic, and the port interfaces (in/out). It has zero dependency on any framework — pure Java only.

**application** contains the use case implementations. It orchestrates the domain by implementing the inbound ports and delegating to the outbound ports. It depends only on the domain module.

**infrastructure** contains all the technical adapters. The REST controllers that expose the API, and the persistence layer with JPA repositories. It implements the outbound ports defined in the domain.

**bootstrap** is the application entry point. Its responsibility is to assemble all modules together and start the Spring Boot application.

**coverage** is a dedicated module with no production code. Its sole purpose is to aggregate the Jacoco reports from all other modules into a single consolidated report, used to ensure the coverage gate in CI.