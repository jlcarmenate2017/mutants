FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/mutants-0.0.1.jar
ADD ${JAR_FILE} mutants-0.0.1.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=dockers","-jar","/mutants-0.0.1.jar"]
