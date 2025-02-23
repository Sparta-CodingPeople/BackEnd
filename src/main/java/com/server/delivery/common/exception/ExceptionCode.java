package com.server.delivery.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionCode {

	// 클라이언트 오류
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request.", 4000),
	FILE_IS_EMPTY(HttpStatus.BAD_REQUEST, "File is Empty", 4001),
	FILE_MISSING_EXTENSION(HttpStatus.BAD_REQUEST, "File does not contain an extension. ", 4002),
	INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "Invalid File Extension.", 4003),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized access.", 4100),
	FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden access.", 4300),
	NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found.", 4400),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "HTTP method not allowed.", 4500),
	CONFLICT(HttpStatus.CONFLICT, "Conflict with current state.", 4900),
	UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable entity.", 4220),

	// 서버 오류,
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.", 5000),
	PUT_OBJECT_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Put Object Exception", 5001),
	FILE_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "File read failed.", 5002),
	FILE_ON_IMAGE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "IOException on Image Delete.", 5003),
	FILE_ON_DECODING_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "IOException on decoding key.", 5003),
	NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "Not implemented.", 5100),
	BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "Bad gateway.", 5200),
	SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Service unavailable.", 5300),

	// 인증 및 권한 관련,
	TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "Token expired.", 1000),
	UNSUPPORTED_TOKEN(HttpStatus.FORBIDDEN, "Unsupported token.", 1001),
	NOT_FOUND_TOKEN(HttpStatus.FORBIDDEN, "Token not found.", 1002),
	TOKEN_IS_INVALID(HttpStatus.FORBIDDEN, "Token is invalid.", 1003),
	TOKEN_IS_NOT_SUPPORTED(HttpStatus.FORBIDDEN, "Token is not supported.", 1004),
	TOKEN_IS_EMPTY(HttpStatus.FORBIDDEN, "Token is empty.", 1005),

	// 예기치 않은 예외 (디폴트),
	NOT_HANDLED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unhandled exception.", 9999),

	// 유저 - 6000
	USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User Not Found.", 6000),
	USERNAME_IS_EXIST(HttpStatus.BAD_REQUEST, "Username is exist.", 6001),
	OWNER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Owner Not Found.", 6002),

	// 주문 - 7000
	ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Order Not Found.", 7000),
	ORDER_CAN_NOT_UPDATE(HttpStatus.BAD_REQUEST, "Order Can Not Update.", 7001),
	ORDER_IS_CANCLED(HttpStatus.BAD_REQUEST, "Order Is Cancled", 7002),
	ORDER_IS_ACCEPTED(HttpStatus.BAD_REQUEST, "Order Is Accepted", 7003),
	ORDER_IS_REJECTED(HttpStatus.BAD_REQUEST, "Order Is Rejected", 7004),
	ORDER_ALREADY_DELIVERING(HttpStatus.BAD_REQUEST, "Order Already Delivering", 7005),
	ORDER_STORE_OWNER_MISMATCH(HttpStatus.BAD_REQUEST, "Order Store Owner Mismatch", 7006),
	ORDER_USER_NOT_MATCHED(HttpStatus.BAD_REQUEST, "Order User Not Matched", 7007),
  OERDER_USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "OERDER_User Not Exist", 7008),


	// 리뷰 - 8000
	REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "Review Not Found.", 8001),
	REVIEW_UPDATE_EXPIRED(HttpStatus.BAD_REQUEST, "Review Update Expired.", 8002),
	REVIEW_NOT_WRITE_DELIVERY_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "Review Not Write Delivery Not Completed.", 8003),

	//카트 - 9000
	CARTS_ORDER_ITEM_EXIST(HttpStatus.BAD_REQUEST, "Carts Order Item Exist.", 9001),
	CARTS_NOT_FOUND(HttpStatus.BAD_REQUEST, "Carts Not Found.", 9002),
	CARTS_ITEM_NOT_MATCHED_TO_STORE(HttpStatus.BAD_REQUEST, "Carts Item Not Matched To Store", 9003),

	//메뉴 - 10000
	MENU_NOT_FOUND(HttpStatus.BAD_REQUEST, "Menu Not Found.", 10001),
	MENU_CART_NOT_FOUND(HttpStatus.BAD_REQUEST, "Menu Cart Not Found.", 10002),
	MENU_IS_EXIST(HttpStatus.BAD_REQUEST, "Menu Is Exist.", 10003),
	MENU_ORDER_IS_EXIST(HttpStatus.BAD_REQUEST, "Menu Order Is Exist.", 10004),

	//매점 - 11000
	STORE_NOT_FOUND(HttpStatus.BAD_REQUEST, "Store Not Found.", 11003),
	STORE_NOT_MATCH(HttpStatus.BAD_REQUEST, "Store Not Match.", 11004),
	STORE_IS_EXIST(HttpStatus.BAD_REQUEST, "Store is exist.", 11005),
	STORE_NOT_GRANTED(HttpStatus.BAD_REQUEST, "Store is not granted.", 11006),
	MANAGER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "Manager Already Exist.", 3335),
	OWNER_IS_NOT_MATCHED(HttpStatus.BAD_REQUEST, "Owner Is Not Matched.", 3336),
	STORE_ORDER_IS_EXIST(HttpStatus.BAD_REQUEST, "Store Order Is Exist.", 3337),

	// 결제 - 3000,
	PAYMENT_SESSION_NOT_FOUND(HttpStatus.BAD_REQUEST, "Payment Session Not Found.", 3000),
	PAYMENT_INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "Payment Invalid Amount", 3001),
	PAYMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "Payment Not Found.", 3002),
	PAYMENT_REQUEST_ALREADY_DONE(HttpStatus.BAD_REQUEST, "Payment Request Already Done", 3003),
	PAYMENT_ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "Payment Already Canceled", 3004),
	PAYMENT_REQUEST_REJECT(HttpStatus.BAD_REQUEST, "Payment Request Reject Error.", 3005),
	PAYMENT_CANCEL_FAILED(HttpStatus.BAD_REQUEST, "Payment Cancel Failed", 3006),
	PAYMENT_REQUEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Payment Request Error.", 3331),
	PAYMENT_CONFIRM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Payment Confirm Error.", 3332),
	PAYMENT_CANCEL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Payment Cancel Error.", 3333),
	PAYMENT_CANCEL_REASON_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "Payment Cancel Reason Not Found.", 3334),

	// 배달 - 2000
	DELIVERY_NOT_FOUND(HttpStatus.BAD_REQUEST, "Delivery Not Found.", 2000),
	DELIVERY_ALREADY_START(HttpStatus.BAD_REQUEST, "Delivery Already Start.", 2001),
	DELIVERY_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "Delivery Not Completed", 2002),
	DELIVERY_ALREADY_DELIVERED(HttpStatus.BAD_REQUEST, "Delivery Already Delivered", 2003),
	DELIVERY_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "Delivery Already Completed", 2004);

	private final HttpStatus httpStatus;
	private final String message;
	private final Integer code;

}
