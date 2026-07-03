.PHONY: build run stop lint test test-unit test-integration

build:
	docker build -f deploy/local/Dockerfile -t jackpot-service:local .

run:
	docker compose --env-file "$(CURDIR)/deploy/local/.env" -f "$(CURDIR)/deploy/local/docker-compose.yml" up --build --remove-orphans

stop:
	docker compose --env-file "$(CURDIR)/deploy/local/.env" -f "$(CURDIR)/deploy/local/docker-compose.yml" down

lint:
	./mvnw -DskipTests compile test-compile

test:
	./mvnw test

test-unit:
	./mvnw test -Dtest='org.sporty.jackpot.unit.**'

test-integration:
	./mvnw test -Dtest='org.sporty.jackpot.integration.**',JackpotApplicationTests
