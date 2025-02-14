# GlobalExceptionHandler 플로우

이 문서는 `GlobalExceptionHandler`의 예외 처리 플로우를 설명합니다.

## 1. CustomException 처리 흐름

`DeliveryException`이 발생하면 다음과 같은 흐름으로 처리됩니다:

1. **예외 발생**: `DeliveryException`이 발생합니다. 예외 객체에는 `ExceptionCode`와 관련된 상태 코드, 메시지 등이 포함됩니다.
2. **`@ExceptionHandler` 호출**: `DeliveryException`을 처리하는 메서드인 `handleDeliveryException`이 호출됩니다.
3. **로깅**: 예외 메시지 및 예외 스택을 `warn` 레벨로 로그에 기록합니다.
4. **응답 생성**: `ExceptionResponse.fromException(ex.getExceptionCode())`를 사용하여 응답 객체를 생성하고, 해당 예외의 상태 코드에 맞는 HTTP 응답을 반환합니다.
5. **응답 반환**: `ResponseEntity.status()`로 상태 코드를 설정하고 `body()`로 예외 응답을 클라이언트에 전달합니다.

## 2. 기본 예외 처리 흐름

예기치 않은 예외가 발생하면, `handleAllException` 메서드가 호출됩니다:

1. **예외 발생**: `DeliveryException` 외의 예외가 발생합니다.
2. **`@ExceptionHandler` 호출**: `Exception` 타입의 예외를 처리하는 `handleAllException`이 호출됩니다.
3. **로깅**: 예외 메시지와 예외 스택을 `error` 레벨로 기록합니다.
4. **응답 생성**: `ExceptionResponse.fromError(ex)`를 사용하여 예기치 않은 예외에 대한 응답 객체를 생성합니다.
5. **응답 반환**: 기본적으로 `500 Internal Server Error` 상태 코드와 함께 응답을 반환합니다.

## 3. 예외 처리 흐름 다이어그램

```plaintext
+-------------------------+         +-------------------------+
|     예외 발생 (Delivery)    | -----> |  handleCustomException  |
+-------------------------+         +-------------------------+
                                                |
                                                v
                                +--------------------------------+
                                |   ExceptionResponse 생성 및 반환  |
                                +--------------------------------+
                                                |
                                                v
                                    +-------------------------+
                                    |   ResponseEntity 반환    |
                                    +-------------------------+