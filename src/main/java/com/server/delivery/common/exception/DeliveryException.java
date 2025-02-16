package com.server.delivery.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DeliveryException extends RuntimeException {

    private final ExceptionCode exceptionCode;


    // 예외 코드에 기반한 생성자
    public DeliveryException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    // 예외 코드에 따라 HttpStatus, code, message 값을 반환
    public HttpStatus getHttpStatus() {
        return this.exceptionCode.getHttpStatus();
    }

    public Integer getCode() {
        return this.exceptionCode.getCode();
    }

    public String getErrorMessage() {
        return this.exceptionCode.getMessage();
    }
}
