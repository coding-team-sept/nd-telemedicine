all:

run:
	docker compose up -d --force-recreate

stop:
	docker compose down

clean:
	docker compose down --volumes
	docker compose down --rmi all --volumes

prune:
	docker system prune --force --all --volumes


