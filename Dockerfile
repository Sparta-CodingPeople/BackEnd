# Liberica OpenJDK 17 사용
FROM bellsoft/liberica-openjdk-alpine:17

# 작업 디렉토리 설정
WORKDIR /app

# Git 설치 (Alpine에는 기본적으로 Git이 없음)
RUN apk update && apk add git

# JAR 파일 복사 (GitHub Actions에서 빌드한 파일을 사용)
COPY build/libs/*.jar app.jar

EXPOSE 8080

# 실행 시 환경 변수에 따라 프로파일 적용 가능하게 설정
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-dev}","/app.jar"]