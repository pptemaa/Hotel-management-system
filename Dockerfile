FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .
COPY hotel-core/pom.xml hotel-core/
COPY hotel-dao/pom.xml hotel-dao/
COPY hotel-ui/pom.xml hotel-ui/

COPY hotel-core/src hotel-core/src
COPY hotel-dao/src hotel-dao/src
COPY hotel-ui/src hotel-ui/src

RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk17
WORKDIR /usr/local/tomcat/webapps/

RUN rm -rf ROOT*
COPY --from=build /app/hotel-ui/target/hotel-ui-1.0-SNAPSHOT.war ./ROOT.war

EXPOSE 8080