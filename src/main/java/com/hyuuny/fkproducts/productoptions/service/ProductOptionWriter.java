package com.hyuuny.fkproducts.productoptions.service;

import com.hyuuny.fkproducts.productoptions.domain.ProductOptionEntity;
import com.hyuuny.fkproducts.productoptions.domain.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductOptionWriter {

    private final ProductOptionRepository productOptionRepository;

    public ProductOptionEntity save(ProductOptionEntity entity) {
        return productOptionRepository.save(entity);
    }

    public void delete(ProductOptionEntity entity) {
        productOptionRepository.deleteById(entity.getId());
    }

}
