package com.server.delivery.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(DeliveryException.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(DeliveryException ex) {
        log.warn("Handled customException: {}", ex.getMessage());
        log.warn("Exception details:", ex);

        // 예외 코드에 맞는 응답 객체 생성
        ExceptionResponse exceptionResponse = ExceptionResponse.fromException(ex.getExceptionCode());

        // 반환하는 HTTP 상태 코드에 따라 응답 생성
        return ResponseEntity.status(exceptionResponse.httpStatus()).body(exceptionResponse);
    }

    // 예외가 추가로 발생할 경우 처리하는 메서드 (기본 예외 처리)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAllException(Exception ex) {
        log.error("Unhandled exception occurred: {}", ex.getMessage());
        log.error("Exception details:", ex);

        // 예기치 않은 예외에 대한 응답 객체 생성
        ExceptionResponse exceptionResponse = ExceptionResponse.fromError(ex);

        // 기본적인 InternalServerError 처리
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }
}