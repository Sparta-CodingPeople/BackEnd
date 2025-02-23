package com.server.delivery.common.exception.customException;

import com.server.delivery.common.exception.DeliveryException;
import com.server.delivery.common.exception.ExceptionCode;

public class CustomDeliveryException extends DeliveryException {
	public CustomDeliveryException(ExceptionCode exceptionCode) {
		super(exceptionCode);
	}
}
