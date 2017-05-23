FROM java:8

COPY gradlew build.gradle settings.gradle /project/
COPY gradle /project/gradle
COPY build/classes /project/build/classes
COPY build/resources /project/build/resources

WORKDIR /project

ENTRYPOINT ["./gradlew"]




