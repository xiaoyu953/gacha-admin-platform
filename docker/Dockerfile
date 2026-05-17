# Multi-stage build for all Java services
# Usage: docker build --build-arg SERVICE_NAME=auth-service -t gacha/auth-service:latest -f docker/Dockerfile .

FROM maven:3.9-eclipse-temurin-17-alpine AS builder
ARG SERVICE_NAME
WORKDIR /build

COPY pom.xml .
COPY common-lib/pom.xml common-lib/
COPY ${SERVICE_NAME}/pom.xml ${SERVICE_NAME}/

RUN mvn dependency:go-offline -pl common-lib,${SERVICE_NAME} -q || true

COPY common-lib/src common-lib/src/
COPY ${SERVICE_NAME}/src ${SERVICE_NAME}/src/

RUN mvn package -pl common-lib,${SERVICE_NAME} -DskipTests -q

FROM eclipse-temurin:17-jre-alpine
ARG SERVICE_NAME
WORKDIR /app

RUN addgroup -S gacha && adduser -S gacha -G gacha

RUN apk add --no-cache curl

COPY --from=builder /build/${SERVICE_NAME}/target/${SERVICE_NAME}-*.jar app.jar

USER gacha
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
