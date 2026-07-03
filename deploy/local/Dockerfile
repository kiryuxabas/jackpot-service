FROM eclipse-temurin:25-jre-alpine

RUN apk add --no-cache curl \
    && addgroup -S spring \
    && adduser -S spring -G spring

WORKDIR /app

ARG JAR_FILE=target/jackpot-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

RUN chown spring:spring app.jar

USER spring:spring

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-jar", "app.jar"]
