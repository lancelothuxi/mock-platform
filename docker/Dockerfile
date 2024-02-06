FROM adoptopenjdk/openjdk8:alpine-jre
MAINTAINER author
EXPOSE 8081
VOLUME /tmp
ADD weather-api.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]