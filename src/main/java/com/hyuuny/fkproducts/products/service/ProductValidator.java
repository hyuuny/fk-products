package com.hyuuny.fkproducts.products.service;

import com.hyuuny.fkproducts.support.error.ErrorType;
import com.hyuuny.fkproducts.support.error.FkProductsException;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator {

    private final Long ZERO_PRICE = 0L;

    public void validate(Long price) {
        if (price <= ZERO_PRICE) {
            throw new FkProductsException(ErrorType.INVALID_PRODUCT_PRICE, "상품 가격은 0보다 커야 합니다.");
        }
    }

}
