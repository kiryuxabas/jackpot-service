.PHONY: build run stop deploy publish deploy-infra destroy-infra tunnel-admin backup sync-dashboard sync-from-posthog youtube-zaliv

TAG := 1.0

build:
	./mvnw -s settings.xml clean package -DskipTests=true

run: build
	docker compose --env-file "$(CURDIR)/deploy/local/.env" -f "$(CURDIR)/deploy/local/docker-compose.yml" up --build --remove-orphans

run-full: build
	docker compose --env-file "$(CURDIR)/deploy/local/.env" -f "$(CURDIR)/deploy/local/docker-compose.full.yml" up --build --remove-orphans

stop:
	docker compose --env-file "$(CURDIR)/deploy/local/.env" -f "$(CURDIR)/deploy/local/docker-compose.yml" down

publish: build
	docker buildx build --platform linux/amd64,linux/arm64 --push \
		-t "registry.gitlab.com/kiryuxabas/nazarov-loh.cc/boosty-tg-chats-joiner:$(TAG)" \
		-f "$(CURDIR)/boosty-tg-chats-joiner/Dockerfile" "$(CURDIR)"

deploy: publish
	@set -ex; \
	host=$$(cd "$(CURDIR)/deploy/vps/terraform" && terraform output -raw droplet_ipv4); \
	TAG=$(TAG) DOCKER_HOST="ssh://root@$$host" docker compose --env-file "$(CURDIR)/deploy/vps/.env" -f "$(CURDIR)/deploy/vps/docker-compose.yml" up -d --remove-orphans

tunnel-admin:
	host=$$(cd "$(CURDIR)/deploy/vps/terraform" && terraform output -raw droplet_ipv4); \
	ssh -N \
		-o BatchMode=yes -o StrictHostKeyChecking=accept-new \
		-L 127.0.0.1:8080:127.0.0.1:8080 \
		"root@$$host"

deploy-infra:
	set -ex && \
	(cd "$(CURDIR)/deploy/vps/terraform" && terraform init -input=false && terraform apply -auto-approve -input=false) && \
	(cd "$(CURDIR)/deploy/vps/terraform" && \
		ansible-playbook \
			-i "$$(terraform output -raw droplet_ipv4)," \
			-e ansible_user=root \
			-e "journal_system_max_use=200M" \
			-e "journal_runtime_max_use=100M" \
			-e "journal_max_retention_sec=2week" \
			-e "docker_log_max_size=10m" \
			-e "docker_log_max_file=3" \
			"$(CURDIR)/deploy/vps/ansible/playbook.yml")

destroy-infra:
	set -ex && \
	(cd "$(CURDIR)/deploy/vps/terraform" && terraform init -input=false && terraform destroy -auto-approve -input=false)
