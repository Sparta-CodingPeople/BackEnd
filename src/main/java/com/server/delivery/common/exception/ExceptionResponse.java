package com.server.delivery.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static com.server.delivery.common.exception.ExceptionCode.NOT_HANDLED_EXCEPTION;

@JsonInclude(JsonInclude.Include.NON_NULL) // NULL 값 필드는 응답에서 제외
@Builder
@Slf4j
public record ExceptionResponse(
        String status,
        @JsonIgnore HttpStatus httpStatus,
        Integer code,
        String message
) {
    public static ExceptionResponse fromException(ExceptionCode errorCode) {
        return new ExceptionResponse(
                "ERROR",
                errorCode.getHttpStatus(),
                errorCode.getCode(),
                errorCode.getMessage()
        );
    }

    public static ExceptionResponse fromError(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        return new ExceptionResponse(
                "ERROR",
                NOT_HANDLED_EXCEPTION.getHttpStatus(),
                NOT_HANDLED_EXCEPTION.getCode(),
                "An unexpected error occurred. Please try again later."
        );
    }
}