FROM docker-group.sigma-it.ru/eclipse-temurin:17.0.2_8-jdk-focal
WORKDIR /app
ARG JAR_FILE=build/libs/spring-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]