# Permission

## Overview
This is a Spring Boot application that currently runs with `./gradlew bootRun`. It includes a simple route `/ping` that responds with "pong" for testing purposes.

## Prerequisites
- Java 21
- Gradle 8.10.1

## Running the Application
Before running the Spring Boot application, deploy the database using Docker Compose:
```sh
docker-compose up --build
```
The database will be accessible via jdbc:postgresql://localhost:5433/postgres.

To run the application, use the following command:
```sh
./gradlew bootRun
```