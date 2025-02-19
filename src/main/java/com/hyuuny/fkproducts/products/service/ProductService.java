package com.hyuuny.fkproducts.products.service;

import com.hyuuny.fkproducts.products.domain.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductWriter productWriter;
    private final ProductValidator validator;

    @Transactional
    public ProductDto.Response createProduct(ProductDto.Create dto) {
        LocalDateTime now = LocalDateTime.now();
        ProductEntity product = dto.toEntity(now);
        validator.validate(product);
        ProductEntity savedProduct = productWriter.save(product);
        return new ProductDto.Response(savedProduct);
    }
}
