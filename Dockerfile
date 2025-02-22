FROM bellsoft/liberica-openjdk-alpine:17

CMD ["./gradlew", "clean", "build"]

VOLUME /tmp

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

COPY resources/delivery-submodule/config-yml/application-dev.yml /app/config/application-dev.yml

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","/app.jar"]