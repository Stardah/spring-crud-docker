FROM openjdk:17-alpine
#ARG JAR_FILE=build/*.jar
ARG JAR_FILE=./build/libs/app.jar
#RUN mkdir /app
COPY ${JAR_FILE} /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]