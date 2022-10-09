FROM eclipse-temurin:17-jre
VOLUME /tmp
ARG JAR_FILE
ENV JAR_FILE $JAR_FILE
COPY ${JAR_FILE} /home/app/${JAR_FILE}
EXPOSE 9002
ENTRYPOINT java -jar /home/app/${JAR_FILE}