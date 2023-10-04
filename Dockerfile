FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar /app/app.jar
COPY rds-cert.pem /app/rds-cert.pem

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=${ENV}", "app.jar"]