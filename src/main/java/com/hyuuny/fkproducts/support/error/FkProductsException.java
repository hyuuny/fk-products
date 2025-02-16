package com.hyuuny.fkproducts.support.error;

import lombok.Getter;

@Getter
public class FkProductsException extends RuntimeException {

    private final ErrorType errorType;

    private final Object data;

    public FkProductsException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = null;
    }

    public FkProductsException(ErrorType errorType, Object data) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.data = data;
    }
}
