package com.server.delivery.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

    // 클라이언트 오류
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request.", 4000),
    FILE_IS_EMPTY(HttpStatus.BAD_REQUEST,"File is Empty" , 4001),
    FILE_MISSING_EXTENSION(HttpStatus.BAD_REQUEST,"File does not contain an extension. " ,4002 ),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST,"Invalid File Extension." ,4003 ),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized access.", 4100),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden access.", 4300),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found.", 4400),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "HTTP method not allowed.", 4500),
    CONFLICT(HttpStatus.CONFLICT, "Conflict with current state.", 4900),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable entity.", 4220),

    // 서버 오류,
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.", 5000),
    PUT_OBJECT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR,"Put Object Exception", 5001),
    FILE_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"File read failed." , 5002 ),
    FILE_ON_IMAGE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR,"IOException on Image Delete." , 5003 ),
    FILE_ON_DECODING_KEY(HttpStatus.INTERNAL_SERVER_ERROR,"IOException on decoding key." , 5003 ),
    NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "Not implemented.", 5100),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "Bad gateway.", 5200),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable.", 5300),

    // 인증 및 권한 관련,
    TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "Token expired.", 1000),
    UNSUPPORTED_TOKEN(HttpStatus.FORBIDDEN, "Unsupported token.", 1001),
    NOT_FOUND_TOKEN(HttpStatus.FORBIDDEN, "Token not found.", 1002),
    TOKEN_IS_INVALID(HttpStatus.FORBIDDEN, "Token is invalid." , 1003 ),
    TOKEN_IS_NOT_SUPPORTED(HttpStatus.FORBIDDEN, "Token is not supported." , 1004 ),
    TOKEN_IS_EMPTY(HttpStatus.FORBIDDEN, "Token is empty." , 1005 ),

    // 예기치 않은 예외 (디폴트),
    NOT_HANDLED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unhandled exception.", 9999);


    private final HttpStatus httpStatus;
    private final String message;
    private final Integer code;

}