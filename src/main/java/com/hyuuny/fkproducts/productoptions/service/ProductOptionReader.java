package com.hyuuny.fkproducts.productoptions.service;

import com.hyuuny.fkproducts.productoptions.domain.ProductOptionEntity;
import com.hyuuny.fkproducts.productoptions.domain.ProductOptionRepository;
import com.hyuuny.fkproducts.support.error.ErrorType;
import com.hyuuny.fkproducts.support.error.FkProductsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductOptionReader {

    private final ProductOptionRepository productOptionRepository;

    public Long getProductOptionCount(Long productId) {
        return productOptionRepository.countByProductId(productId);
    }

    public ProductOptionEntity read(Long id) {
        return productOptionRepository.findById(id).orElseThrow(() ->
                new FkProductsException(ErrorType.PRODUCT_OPTION_NOTFOUND, "상품 옵션을 찾을 수 없습니다 id:" + id)
        );
    }

}
