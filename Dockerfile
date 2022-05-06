FROM openjdk:17.0-jdk

RUN microdnf install curl

ARG JAR_FILE=kotlin-showcase-0.0.1-SNAPSHOT.jar
COPY build/libs/${JAR_FILE} /opt/application.jar

HEALTHCHECK --start-period=10s --timeout=60s --retries=10 --interval=5s CMD curl -f http://localhost:8080/actuator/health || exit 1

ENV JAVA_OPTS="-Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true"

EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -jar /opt/application.jar