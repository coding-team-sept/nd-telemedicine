all:

run: start

start: start-docker

start-docker:
	docker compose up -d --force-recreate

stop: stop-docker

stop-docker:
	docker compose down

clean: clean-volumes clean-images

clean-volumes:
	docker compose down --volumes

clean-images:
	docker compose down --rmi all --volumes

prune:
	docker system prune --force --all --volumes


