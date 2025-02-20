# Liberica OpenJDK 17 사용
FROM bellsoft/liberica-openjdk-alpine:17

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 빌드 실행 (JAR 파일을 build/libs에 생성)
RUN ./gradlew clean build -x test

# JAR 파일 복사 (빌드 후 생성된 JAR 파일을 복사)
COPY build/libs/*.jar app.jar

EXPOSE 8080

# 실행 시 환경 변수에 따라 프로파일 적용 가능하게 설정
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-dev}","/app.jar"]