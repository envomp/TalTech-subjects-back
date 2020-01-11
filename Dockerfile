# Docker multi-stage build


# Just using the build artifact and then removing the build-container
FROM openjdk:11-jdk

ADD . /backend
WORKDIR /backend

# Just echo so we can see, if everything is there :)
RUN ls -l

# Run Maven build
RUN chmod +x mvnw && ./mvnw clean install

MAINTAINER Agonaudid

VOLUME /tmp
FROM openjdk:11-jdk

# Add Spring Boot app.jar to Container
COPY --from=0 "/backend/target/backend-0.0.1-SNAPSHOT.jar" back.jar

ENV JAVA_OPTS=""

# Fire up our Spring Boot app by default
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /back.jar" ]

EXPOSE 8098:8098 9001:9001
