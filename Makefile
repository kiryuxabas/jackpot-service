.PHONY: build run stop

build:
	docker build -f deploy/local/Dockerfile -t jackpot-service:local .

run:
	docker compose --env-file "$(CURDIR)/deploy/local/.env" -f "$(CURDIR)/deploy/local/docker-compose.yml" up --build --remove-orphans

stop:
	docker compose --env-file "$(CURDIR)/deploy/local/.env" -f "$(CURDIR)/deploy/local/docker-compose.yml" down
