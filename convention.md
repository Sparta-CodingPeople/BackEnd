# 프로젝트 브랜치 및 커밋 컨벤션 정리

## 1\. Commit Message Convention

커밋 메시지는 다음과 같은 구조로 작성한다.




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

1.4. Footer (꼬리말)
	•	꼬리말은 선택적이며 이슈 트래커 ID를 작성한다.
	•	여러 개의 이슈 번호를 적을 때는 쉼표로 구분한다.
	•	이슈 트래커 유형은 다음과 같다.

| 유형        | 설명                             |
|-----------|---------------------------------|
| Fixes     | 이슈 수정 중                     |
| Resolves  | 이슈를 해결했을 때               |
| Ref       | 참고할 이슈가 있을 때             |
| Related to | 해당 커밋에 관련된 이슈 번호 (아직 해결되지 않음) |

예시

Fixes: #45
Related to: #34, #23

## 2\. Branch Naming Convention

| 브랜치명     | 설명 |
|------------|--------------------------------------|
| master     | 사용자에게 배포 가능한 상태를 관리하는 브랜치 |
| develop    | 다음 출시 버전을 개발하는 브랜치 |
| feature    | 기능을 개발하는 브랜치 (develop에서 분기) |
| release    | 이번 출시 버전을 준비하는 브랜치 |
| hotfix     | 출시 버전에서 발생한 버그를 수정하는 브랜치 (master에서 분기) |


개발 컨벤션을 정할 때는 팀원 간의 일관성을 유지하고 협업을 원활하게 하기 위해 다양한 규칙을 정의하는 것이 중요해.
다음은 추가로 정해야 할 주요 개발 컨벤션이야.

# 코드 스타일(Code Style) 컨벤션

코딩 스타일 통일
	•	들여쓰기(Indentation): 공통적으로 사용할 들여쓰기 스타일(Tab 또는 Space, 개수) 정의
	•	중괄호(Braces): {} 사용 방식 (한 줄에 쓰는지, 줄 바꿈하는지)
	•	줄바꿈(Line Breaks): 코드 길이가 일정 길이(예: 100자)를 초과하면 줄바꿈 여부
	•	공백(Whitespace): 연산자(=, +, - 등) 앞뒤 공백 규칙

## 1. 네이밍(Naming) 규칙

| 요소          | 규칙 |
|--------------|--------------------------------------|
| 변수명        | camelCase (`userName`, `orderList`) |
| 상수명        | UPPER_SNAKE_CASE (`MAX_LIMIT`) |
| 클래스명      | PascalCase (`UserService`, `OrderController`) |
| 메서드명      | camelCase (`getUserInfo()`, `processOrder()`) |
| 패키지명      | 소문자 및 점(`.`) 구분 (`com.example.project`) |
| 파일명        | 클래스명과 동일 (Java: `UserService.java`, Python: `user_service.py`) |

## 2. API 컨벤션

2.1. RESTful API 네이밍 규칙

| HTTP Method | 목적 |
|------------|----------------|
| GET        | 리소스 조회     |
| POST       | 새로운 리소스 생성 |
| PUT        | 기존 리소스 전체 수정 |
| PATCH      | 기존 리소스 부분 수정 |
| DELETE     | 리소스 삭제 |

	•	URL은 동사 대신 명사를 사용
	•	GET /users (❌ GET /getUsers)
	•	POST /orders (❌ POST /createOrder)
	•	복수형 사용
	•	/users (❌ /user)

2.2. 응답(Response) 형식
	•	응답 형식은 JSON을 기본으로 사용
	•	응답 형태 예시:

{
  "status": "success",
  "message": "요청이 정상 처리되었습니다.",
  "data": {
    "id": 1,
    "name": "홍길동"
  }
}

## 3. 데이터베이스(DB) 컨벤션

3.1. 테이블 & 컬럼 네이밍

| 요소      | 규칙 |
|----------|--------------------------------------|
| 테이블명  | 소문자 + 복수형 (`users`, `orders`) |
| 컬럼명    | 소문자 + snake_case (`user_name`) |
| PK       | `id` (`user_id`, `order_id`) |
| FK       | `{참조하는 테이블명}_id` (`user_id`) |
| 인덱스명  | `idx_{테이블명}_{컬럼명}` (`idx_users_email`) |

3.2. DB 설계 원칙
	•	정규화(Normalization) 준수
	•	VARCHAR보다는 TEXT 또는 JSON 사용 고려
	•	boolean 대신 TINYINT(1) 사용 (MySQL 기준)
	•	created_at, updated_at 기본 제공

## 4. 예외 처리(Exception Handling) 컨벤션
	•	모든 예외는 사용자 친화적인 메시지로 변환하여 응답
	•	공통 예외 응답 형식:

{
  "status": "error",
  "message": "유효하지 않은 요청입니다.",
  "errorCode": "400_BAD_REQUEST"
}

	•	HTTP 상태 코드 기준

| 상태 코드 | 의미 |
|----------|------------------------------|
| 200 OK    | 정상 응답 |
| 201 Created | 리소스 생성 완료 |
| 400 Bad Request | 잘못된 요청 |
| 401 Unauthorized | 인증 실패 |
| 403 Forbidden | 권한 없음 |
| 404 Not Found | 리소스 없음 |
| 500 Internal Server Error | 서버 내부 오류 |

## 5. 로그(Log) 컨벤션
	•	INFO: 일반적인 서비스 동작 (로그인 성공, 주문 생성)
	•	DEBUG: 디버깅용 (요청 파라미터, 쿼리 결과)
	•	ERROR: 예외 발생 (DB 오류, NullPointerException)

로그 예시

2024-02-12 14:30:15 [INFO] UserService - User login successful: userId=123
2024-02-12 14:31:45 [ERROR] OrderService - Order creation failed: orderId=456, reason=OutOfStockException

