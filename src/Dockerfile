# jdk 17 image
FROM eclipse-temurin:17-jdk-jammy
LABEL authors="Kareem.AlQutob"
WORKDIR /app

COPY target/SmartHealthcareAppointmentSystem-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]