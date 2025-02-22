package com.hyuuny.fkproducts.support.error;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {

    INVALID_PRODUCT_PRICE(HttpStatus.BAD_REQUEST, ErrorCode.E400, "invalid productPrice", LogLevel.ERROR),
    PRODUCT_NOTFOUND(HttpStatus.BAD_REQUEST, ErrorCode.E404, "product notFound", LogLevel.ERROR),
    INVALID_SELECTED_OPTION_ITEM(HttpStatus.BAD_REQUEST, ErrorCode.E400, "invalid selectedOptionItem", LogLevel.ERROR),
    INVALID_INPUT_OPTION_ITEM(HttpStatus.BAD_REQUEST, ErrorCode.E400, "invalid inputOptionItem", LogLevel.ERROR),
    MAXIMUM_PRODUCT_OPTION_COUNT(HttpStatus.BAD_REQUEST, ErrorCode.E400, "maximum productOptionCount", LogLevel.ERROR),
    PRODUCT_OPTION_NOTFOUND(HttpStatus.BAD_REQUEST, ErrorCode.E404, "productOption notFound", LogLevel.ERROR),
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "An unexpected error has occurred.", LogLevel.ERROR);

    private final HttpStatus status;

    private final ErrorCode code;

    private final String message;

    private final LogLevel logLevel;

    ErrorType(HttpStatus status, ErrorCode code, String message, LogLevel logLevel) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

}
