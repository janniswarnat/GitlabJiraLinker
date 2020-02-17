FROM maven:3-jdk-11 as mvnbuild

ADD ./src /opt/GitlabJiraLinker/src
ADD ./pom.xml /opt/GitlabJiraLinker/
WORKDIR /opt/GitlabJiraLinker

RUN mvn clean install

FROM openjdk:8-jre-slim

WORKDIR /opt/app

COPY --from=mvnbuild /opt/GitlabJiraLinker/target/GitlabJiraLinker-1.0.jar /opt/app/

CMD ["java","-jar", "GitlabJiraLinker-1.0.jar"]