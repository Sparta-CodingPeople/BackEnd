package com.server.delivery.common.exception.customException;

import com.server.delivery.common.exception.DeliveryException;
import com.server.delivery.common.exception.ExceptionCode;

public class CustomStoreException extends DeliveryException {
    public CustomStoreException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
