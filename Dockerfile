FROM adoptopenjdk/openjdk11:ubi

ADD target/streaming_platform_backend.jar streaming_platform_backend.jar

EXPOSE 8082

ENTRYPOINT ["java","-jar","/streaming_platform_backend.jar"]
