FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD quick-docker-1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]


#注意里面的ADD xxx.jar是你使用maven打包之后的jar包的名称，其他的不变
mvn package docker:build