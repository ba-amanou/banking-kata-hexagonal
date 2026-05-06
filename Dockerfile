FROM eclipse-temurin:21 AS builder
WORKDIR /app

COPY pom.xml .
COPY domain/pom.xml domain/
COPY application/pom.xml application/
COPY infrastructure/pom.xml infrastructure/
COPY bootstrap/pom.xml bootstrap/
COPY coverage/pom.xml coverage/

COPY .mvn .mvn
COPY mvnw .

RUN ./mvnw dependency:go-offline -B

COPY . .

RUN ./mvnw package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/bootstrap/target/bootstrap-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]