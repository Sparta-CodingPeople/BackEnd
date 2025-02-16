package com.server.delivery.common.exception.customException;

import com.server.delivery.common.exception.DeliveryException;
import com.server.delivery.common.exception.ExceptionCode;

public class S3Exception extends DeliveryException {
    public S3Exception(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
