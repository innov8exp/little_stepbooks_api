FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar /app/app.jar
COPY rds-cert.pem /app/rds-cert.pem

EXPOSE 8081

CMD ["java", "-jar", "-Dspring.profiles.active=${ENV}", "app.jar"]