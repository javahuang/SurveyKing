FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY surveyking.mv.db surveyking.mv.db 
RUN mkdir /surveyking
VOLUME /files
VOLUME /logs
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]