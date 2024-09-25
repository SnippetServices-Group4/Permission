FROM gradle:8.10.1-jdk21 AS build
COPY  src /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle assemble
FROM openjdk:21-jre-slim
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production","/app/spring-boot-application.jar"]