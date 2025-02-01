# 빌드 과정
FROM openjdk:17-slim AS build

RUN apt-get update && apt-get install -y bash

WORKDIR /app

COPY ../build.gradle .
COPY ../settings.gradle .
COPY ../gradle gradle/
COPY ../gradlew .
RUN chmod +x gradlew

RUN ./gradlew dependencies

COPY ../src src

RUN ./gradlew clean build -x test

# 실행 환경
FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/build/libs/chat-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]