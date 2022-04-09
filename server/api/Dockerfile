FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY survey.mv.db survey.mv.db 
RUN mkdir /surveyking
VOLUME /surveyking
ENTRYPOINT ["java","-jar","/app.jar"]