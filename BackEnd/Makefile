all: run

run: start

start: app-build-image docker-start

app-build-image:
	mvn clean install

docker-start:
	docker compose up --detach --force-recreate

docker-stop:
	docker compose down

docker-clean:
	docker compose down --rmi all --volumes

docker-prune:
	docker system prune --force --all
	docker volume prune --force


