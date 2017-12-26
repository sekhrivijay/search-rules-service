FROM openjdk:8
MAINTAINER com.services.micro
ADD target/*.jar /
ADD src/main/resources /resources
ADD entrypoint.sh /entrypoint.sh
ENV PATH /:$PATH
RUN chmod +x /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]