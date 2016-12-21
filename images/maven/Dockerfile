FROM maven:3.3.9-jdk-8
MAINTAINER Basile Vu <basile.vu@gmail.com>

COPY /gamification /gamification
RUN mvn -f /gamification/pom.xml package

CMD ["java", "-jar", "/gamification/target/gamification-0.0.1-SNAPSHOT.jar"]
