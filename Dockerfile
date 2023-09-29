FROM openjdk:22-jdk-slim

WORKDIR /app

COPY build/libs/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=${ENV}", "app.jar"]