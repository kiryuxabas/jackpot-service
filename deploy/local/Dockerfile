FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml settings.xml ./
RUN mvn -s settings.xml dependency:go-offline -DskipTests || true

COPY src ./src
RUN mvn -s settings.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache curl \
    && addgroup -S spring \
    && adduser -S spring -G spring

WORKDIR /app

COPY --from=builder /build/target/jackpot-0.0.1-SNAPSHOT.jar app.jar

RUN chown spring:spring app.jar

USER spring:spring

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-jar", "app.jar"]
