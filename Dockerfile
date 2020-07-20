FROM openjdk:14-jdk as base
WORKDIR /tmp
COPY . .
RUN export MAILER_VERSION=$(./gradlew -q printVersion) \
        && ./gradlew build && mv /tmp/build/libs/mailer-${MAILER_VERSION}.jar /tmp/mailer.jar

FROM openjdk:14
WORKDIR /usr/local/lib
COPY --from=base "/tmp/mailer.jar" .
ENTRYPOINT java -jar -Dspring.profiles.active=prod ./mailer.jar
EXPOSE 80
