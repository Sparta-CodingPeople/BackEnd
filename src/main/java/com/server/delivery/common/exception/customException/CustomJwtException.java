package com.server.delivery.common.exception.customException;

import com.server.delivery.common.exception.DeliveryException;
import com.server.delivery.common.exception.ExceptionCode;

public class CustomJwtException extends DeliveryException {
    public CustomJwtException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
