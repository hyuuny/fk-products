package com.hyuuny.fkproducts.productoptions.service;

import com.hyuuny.fkproducts.productoptions.domain.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductOptionReader {

    private final ProductOptionRepository productOptionRepository;

    public Long getProductOptionCount(Long productId) {
        return productOptionRepository.countByProductId(productId);
    }

}
