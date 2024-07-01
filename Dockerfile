FROM node:18-alpine as builderNode

WORKDIR /project

COPY Frontend/app-woola/ .

RUN npm install

RUN npm run build -- --configuration production

#------------------------------------------------------------------

FROM maven:3-openjdk-11 as builder

WORKDIR /project

COPY Backend/woola/pom.xml /project/

COPY Backend/woola/src /project/src

COPY --from=builderNode /project/dist/app-woola/ /project/src/main/resources/static/

RUN mvn package

#------------------------------------------------------------------

FROM openjdk:11

ENV JAVA_TOOL_OPTIONS="-Xss256K -XX:ReservedCodeCacheSize=64M -XX:MaxMetaspaceSize=100000K -Xmx64M"

WORKDIR /usr/src/app/

COPY --from=builder /project/target/*.jar /usr/src/app/

EXPOSE 8443

CMD ["java", "-jar","woola-0.0.1-SNAPSHOT.jar"]
