.PHONY: build run stop

build:
	./mvnw -s settings.xml clean package -DskipTests=true

run: build
	docker compose --env-file "$(CURDIR)/deploy/local/.env" -f "$(CURDIR)/deploy/local/docker-compose.yml" up --build --remove-orphans

stop:
	docker compose --env-file "$(CURDIR)/deploy/local/.env" -f "$(CURDIR)/deploy/local/docker-compose.yml" down
