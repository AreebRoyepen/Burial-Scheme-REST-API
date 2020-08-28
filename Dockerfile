FROM centos
RUN yum install -y java-11-openjdk-devel
VOLUME /temp
ADD target/Burial-Scheme-REST-API-1.0.0.jar myapp.jar
ENTRYPOINT ["java", "-Djava.security.edg=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","/myapp.jar"]