FROM amazoncorretto:21-alpine-jdk
RUN apk add --no-cache ffmpeg
COPY build/libs/DOCKin-spring-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Xmx512M", "-Xms256M", "-jar", "/app.jar"]