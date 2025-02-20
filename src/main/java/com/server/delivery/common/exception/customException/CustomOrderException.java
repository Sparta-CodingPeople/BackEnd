package com.server.delivery.common.exception.customException;

import com.server.delivery.common.exception.DeliveryException;
import com.server.delivery.common.exception.ExceptionCode;

public class CustomOrderException extends DeliveryException {
    public CustomOrderException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
