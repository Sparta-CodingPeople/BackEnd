# 개발 컨벤션 정리

# GIt Convention

## 1\. Commit Message Convention

1\.1\. Type (커밋 유형)

| Type              | 설명                                      |
|------------------|-----------------------------------------|
| `fix`           | 버그를 수정한 경우                        |
| `feat`          | 새로운 기능을 추가한 경우                 |
| `BREAKING CHANGE` | 큰 API 변경이 있는 경우                 |
| `HOTFIX`        | 치명적인 버그를 급하게 수정해야 하는 경우 |
| `Style`        | 코드 포맷 변경, 세미콜론 누락 등 코드 수정이 없는 경우 |
| `Refactor`      | 프로덕션 코드 리팩토링                    |
| `Comment`      | 필요한 주석 추가 및 변경                   |
| `Docs`         | 문서를 수정한 경우                         |
| `Test`         | 테스트를 추가한 경우                       |
| `Chore`        | 빌드 테스트 업데이트, 패키지 매니저를 설정하는 경우 |
| `Rename`       | 파일 혹은 폴더명을 수정하거나 옮기는 작업을 한 경우 |
| `Remove`       | 파일을 삭제하는 작업을 한 경우             |

1\.2\. Subject (제목)
	•	영문 기준 50자 이내로 작성한다.
	•	첫 글자는 대문자로 작성하며 끝에 마침표를 찍지 않는다.

1\.3\. Body (본문)
	•	한 줄 당 72자 이내로 작성한다.
	•	전체 내용은 양과 상관없이 최대한 상세하게 작성한다.
	•	‘어떻게’ 보다는 ‘무엇을’, ‘왜’ 변경했는지를 설명한다.

- 기능이 간편하면 한줄
- 길어지면 아래 예시처럼 정리

예시

### 내용이 간단한 경우
[커밋유형] : 설명

### 내용이 길어질 경우
[커밋유형] : 제목
1. 내용
2. 내용

## 2\. Branch Naming Convention

| 브랜치명     | 설명 |
|------------|--------------------------------------|
| main     | 사용자에게 배포 가능한 상태를 관리하는 브랜치 |
| develop    | 다음 출시 버전을 개발하는 브랜치 |
| feature    | 기능을 개발하는 브랜치 (develop에서 분기) |


# Develop Convention

## 1. 네이밍(Naming) 규칙

| 요소          | 규칙 |
|--------------|--------------------------------------|
| 변수명        | camelCase (`userName`, `orderList`) |
| 상수명        | UPPER_SNAKE_CASE (`MAX_LIMIT`) |
| 클래스명      | PascalCase (`UserService`, `OrderController`) |
| 메서드명      | camelCase (`getUserInfo()`, `processOrder()`) |
| 패키지명      | 소문자 및 점(`.`) 구분 (`com.example.project`) |
| 파일명        | 클래스명과 동일 (Java: `UserService.java`) |

## 2. 패키지 구조 컨벤션

패키지를 domain, model , common , util로 분리
- domain : Controller, Service, DTO
- model : Entity, Repository
- common : 전역 설정
- util : Service간 공동으로 사용하는 메서드 분리하여 만든 클래스 (FileUtil, FileHolder, FileHelper 등...)
```shell
com.example.project
 ├── domain
 │   ├── user
 │   │   ├── UserController
 │   │   ├── UserService
 │   │   ├── dto
 │   │   │   ├── UserRequestDto
 │   │   │   ├── UserResponseDto
 │   ├── order
 │   │   ├── OrderController
 │   │   ├── OrderService
 │   │   ├── dto
 │   │   │   ├── OrderRequestDto
 │   │   │   ├── OrderResponseDto
 ├── model
 │   ├── entity
 │   ├── repository
 ├── common
 │   ├── exception
 │   ├── filter
 ├── util
```


## 3. 예외 처리(Exception Handling) 컨벤션
	•	모든 예외는 사용자 친화적인 메시지로 변환하여 응답
	•	공통 예외 응답 형식:

{
  "status": "error",
  "message": "유효하지 않은 요청입니다.",
  "errorCode": "9999"
}

	•	HTTP 상태 코드 기준

| 상태 코드  | 의미         |
|--------|------------|
| 200 OK | 정상 응답      |
| 1000번대 | 인증 및 권한 관련 |
| 4000번대 | 클라이언트 오류   |
| 5000번대 | 서버 오류      |
| 9999   | 예기치 않은 예외  |

## 4. 로그(Log) 컨벤션
	•	INFO: 일반적인 서비스 동작 (로그인 성공, 주문 생성)
	•	DEBUG: 디버깅용 (요청 파라미터, 쿼리 결과)
	•	ERROR: 예외 발생 (DB 오류, NullPointerException)

로그 예시
```shell
2024-02-12 14:30:15 [INFO] UserService - User login successful: userId=123
2024-02-12 14:31:45 [ERROR] OrderService - Order creation failed: orderId=456, reason=OutOfStockException
```

## 5.  DTO 관련 컨벤션
	•	엔티티가 아닌 DTO에 from 또는 of 메서드 생성하여 변환
	•	서비스 로직에서 직접 엔티티를 생성하지 않고, DTO를 통해 변환

## 6. Stream 우선 사용(for은 후순위)

	•	컬렉션을 다룰 때 가능하면 Stream API를 우선적으로 사용
	•	불필요한 for 반복문을 지양하고 가독성을 높임
## 7. 페이징 처리
	•	PageModel을 사용한 데이터 반환
## 8. 객체 생성 방식
	•	생성자를 통한 객체 생성을 지양하고, Builder 패턴 사용
	•	setter 메서드를 사용하지 않고 불변 객체를 유지
