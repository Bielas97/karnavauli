FROM java:8-jdk-alpine

COPY ./target/app-0.0.1-SNAPSHOT.jar /usr/app/

WORKDIR /usr/app

RUN sh -c 'touch app-0.0.1-SNAPSHOT.jar'

ENTRYPOINT ["java","-jar","app-0.0.1-SNAPSHOT.jar"]