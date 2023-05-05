FROM eclipse-temurin:17
ARG JAR_FILE=build/libs/log-state-pipeline.jar
COPY ${JAR_FILE} log-state-pipeline.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=cloud", "/log-state-pipeline.jar"]