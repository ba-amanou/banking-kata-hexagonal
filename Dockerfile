FROM eclipse-temurin:21 AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/bootstrap/target/bootstrap-*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]