# Banking Kata - Hexagonal Architecture

A banking kata built with **Hexagonal Architecture** (Ports & Adapters) and **TDD** in Java / Spring Boot.

## Purpose

Practice and showcase:
- Hexagonal Architecture (clean separation of domain / adapters)
- Test-Driven Development (TDD)
- Domain-Driven Design building blocks
- Multi-module Maven project
- Git best practices (conventional commits, feature branches, pull requests)

## Tech Stack

- Java 21
- Spring Boot 4.x
- PostgreSQL + Spring Data JPA
- JUnit 5 + Mockito + AssertJ
- Testcontainers
- ArchUnit

## Project Structure

The project is split into 4 Maven modules, each with a clear responsibility.

**domain** is the heart of the application. It contains the business entities, the business logic, and the port interfaces (in/out). It has zero dependency on any framework — pure Java only.

**application** contains the use case implementations. It orchestrates the domain by implementing the inbound ports and delegating to the outbound ports. It depends only on the domain module.

**infrastructure** contains all the technical adapters. The REST controllers that expose the API, and the persistence layer with JPA repositories. It implements the outbound ports defined in the domain.

**bootstrap** is the application entry point. Its responsibility is to assemble all modules together and start the Spring Boot application.

> Full documentation coming soon.