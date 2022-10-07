all: run

run: start

start: docker-start app-package
	make app-start -j2

app-start: app-package app-start-authentication app-start-appointment

app-start-authentication:
	java -jar ./authentication/target/authentication-0.0.0-alpha.1.jar

app-start-appointment:
	java -jar ./appointment/target/appointment-0.0.0-alpha.1.jar

app-package:
	mvn clean
	mvn package spring-boot:repackage -Dmaven.test.skip

docker-start:
	docker compose up --detach --force-recreate

docker-stop:
	docker compose down

docker-clean:
	docker compose down --rmi all --volumes

docker-prune:
	docker system prune --force --all
	docker volume prune --force


