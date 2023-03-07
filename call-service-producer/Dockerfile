FROM openjdk:11
MAINTAINER denis_elsukov
COPY target/call-service-producer-0.0.1-SNAPSHOT.jar producer-service-0.0.1-SNAPSHOT.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","/producer-service-0.0.1-SNAPSHOT.jar"]