FROM amazoncorretto:21-alpine-jdk
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]