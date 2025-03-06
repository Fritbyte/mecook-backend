FROM maven:3.8-openjdk-17 AS build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY src src
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency
RUN cd target/dependency && jar -xf ../*.jar

FROM openjdk:17-jdk-slim
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod \
    TZ=Europe/Moscow \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from=build /workspace/app/target/dependency/BOOT-INF/lib /app/lib
COPY --from=build /workspace/app/target/dependency/META-INF /app/META-INF
COPY --from=build /workspace/app/target/dependency/BOOT-INF/classes /app

EXPOSE 8080

ENTRYPOINT ["java", "-cp", ".:lib/*", "com.mecook.mecookbackend.MeCookApplication"]
