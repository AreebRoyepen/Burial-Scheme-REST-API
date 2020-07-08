FROM centos
RUN yum install -y java-11-openjdk-devel
VOLUME /temp
ADD /Burial-Scheme-REST-API-1.0.0.jar myapp.jar
RUN sh -c 'touch /myapp.jar'
ENTRYPOINT ["java", "-Djava.security.edg=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","/myapp.jar"]