# Jackpot Service

Backend service for jackpot pool contributions and rewards (home assignment).

## Prerequisites

- Docker and Docker Compose
- Make (optional)
- Java 21 (only for local build without Docker)

## Run

```sh
make run
```

Stop:

```sh
make stop
```

The stack starts Kafka, Kafka UI, and the jackpot service on port `8080`.

## API

Swagger UI: http://localhost:8080/swagger-ui/index.html

### Create a bet (publishes to Kafka topic `jackpot-bets`)

```sh
curl -X POST http://localhost:8080/api/jackpot/bet \
  -H 'Content-Type: application/json' \
  -d '{
    "betId": "bet-1",
    "userId": "user-42",
    "jackpotId": "mega",
    "betAmount": 100
  }'
```

Available jackpot IDs (see `src/main/resources/data.sql`): `mega`, `super`, `medium`, `mini`.

### Evaluate jackpot reward

Wait until the Kafka consumer processes the bet (outbox poll interval ~1s), then:

```sh
curl -X POST http://localhost:8080/api/jackpot/bet/bet-1
```

Response example:

```json
{"winner": false, "rewardAmount": 0}
```

If called before contribution is processed, returns `404 Contribution not found`.

## H2 Console

- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:jackpotdb`
- Username: `sa`
- Password: *(empty)*

## Kafka UI

http://localhost:8090

## GitHub

<!-- Replace with your public repository URL -->
https://github.com/YOUR_USERNAME/jackpot-service
