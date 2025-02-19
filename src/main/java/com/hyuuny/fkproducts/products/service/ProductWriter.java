package com.hyuuny.fkproducts.products.service;

import com.hyuuny.fkproducts.products.domain.ProductEntity;
import com.hyuuny.fkproducts.products.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductWriter {

    private final ProductRepository productRepository;

    public ProductEntity save(ProductEntity entity) {
        return productRepository.save(entity);
    }

    public void delete(ProductEntity entity) {
        productRepository.deleteById(entity.getId());
    }

}
