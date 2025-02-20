package com.server.delivery.common.exception.customException;

import com.server.delivery.common.exception.DeliveryException;
import com.server.delivery.common.exception.ExceptionCode;

public class CustomMenuException extends DeliveryException {
    public CustomMenuException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
