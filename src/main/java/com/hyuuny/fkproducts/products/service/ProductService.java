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
    private final ProductReader productReader;
    private final ProductValidator validator;

    @Transactional
    public ProductDto.Response createProduct(ProductDto.Create dto) {
        LocalDateTime now = LocalDateTime.now();
        ProductEntity product = dto.toEntity(now);
        validator.validate(product.getPrice());
        ProductEntity savedProduct = productWriter.save(product);
        return new ProductDto.Response(savedProduct);
    }

    public ProductDto.Response getProduct(Long id) {
        ProductEntity product = productReader.read(id);
        return new ProductDto.Response(product);
    }

    @Transactional
    public ProductDto.Response updateProduct(Long id, ProductDto.Update dto) {
        validator.validate(dto.getPrice());
        ProductEntity product = productReader.read(id);
        dto.update(product);
        return new ProductDto.Response(product);
    }
}
