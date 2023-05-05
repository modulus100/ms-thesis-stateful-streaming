FROM eclipse-temurin:17
ARG JAR_FILE=build/libs/stream-generator-on-coroutines.jar
COPY ${JAR_FILE} stream-generator-on-coroutines.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=cloud", "/stream-generator-on-coroutines.jar"]