package com.server.delivery.common.exception.customException;

import com.server.delivery.common.exception.DeliveryException;
import com.server.delivery.common.exception.ExceptionCode;

public class CustomCartException extends DeliveryException {
    public CustomCartException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
