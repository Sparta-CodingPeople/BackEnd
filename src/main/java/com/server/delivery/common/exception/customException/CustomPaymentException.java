package com.server.delivery.common.exception.customException;

import com.server.delivery.common.exception.DeliveryException;
import com.server.delivery.common.exception.ExceptionCode;

public class CustomPaymentException extends DeliveryException {
	public CustomPaymentException(ExceptionCode exceptionCode) {
		super(exceptionCode);
	}
}
