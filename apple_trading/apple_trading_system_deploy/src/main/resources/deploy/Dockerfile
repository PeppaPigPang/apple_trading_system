FROM openjdk:17-jdk-slim
WORKDIR /app
COPY ../apple_trading_system_deploy-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]