package com.example.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized!",HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1002,"User existed",HttpStatus.BAD_REQUEST),
    INVALID_KEY(1001,"Invalid message key",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003,"Username is must be at least 3 characters",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004,"User not existed",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED_EXCEPTION(1006,"Unauthenticated!",HttpStatus.UNAUTHORIZED),
    INVALID_DOB(1008,"Invalid date of birth",HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_EXCEPTION(1007,"You do not have permission!",HttpStatus.FORBIDDEN),
    PASSWORD_INVALID(1005,"Password is must be at least 8 characters",HttpStatus.BAD_REQUEST);
    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = httpStatusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;

}
