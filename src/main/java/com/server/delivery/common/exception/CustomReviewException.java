package com.server.delivery.common.exception;

public class CustomReviewException extends DeliveryException {
    public CustomReviewException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
