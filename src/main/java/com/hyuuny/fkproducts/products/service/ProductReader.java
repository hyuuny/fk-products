package com.hyuuny.fkproducts.products.service;

import com.hyuuny.fkproducts.products.domain.ProductEntity;
import com.hyuuny.fkproducts.products.domain.ProductRepository;
import com.hyuuny.fkproducts.support.error.ErrorType;
import com.hyuuny.fkproducts.support.error.FkProductsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductReader {

    private final ProductRepository productRepository;

    public ProductEntity read(Long id) {
        return productRepository.findById(id).orElseThrow(() ->
                new FkProductsException(ErrorType.PRODUCT_NOTFOUND, "상품을 찾을 수 없습니다 id:" + id)
        );
    }

}
