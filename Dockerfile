FROM openjdk:11
EXPOSE 8080
ADD ./target/gerproc-0.0.1-SNAPSHOT.war gerproc.war
ENTRYPOINT [ "java", "-jar", "./gerproc.war" ]