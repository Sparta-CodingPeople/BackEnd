package com.server.delivery.common.exception.customException;

import com.server.delivery.common.exception.DeliveryException;
import com.server.delivery.common.exception.ExceptionCode;

public class CustomUserException extends DeliveryException {
    public CustomUserException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
