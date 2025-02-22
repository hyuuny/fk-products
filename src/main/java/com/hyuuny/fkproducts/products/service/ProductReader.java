package com.hyuuny.fkproducts.products.service;

import com.hyuuny.fkproducts.products.domain.ProductEntity;
import com.hyuuny.fkproducts.products.domain.ProductRepository;
import com.hyuuny.fkproducts.products.service.ProductDto.ProductSearchCondition;
import com.hyuuny.fkproducts.support.error.ErrorType;
import com.hyuuny.fkproducts.support.error.FkProductsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<ProductEntity> search(ProductSearchCondition searchCondition, Pageable pageable) {
        return productRepository.search(searchCondition, pageable);
    }

    public boolean existsProduct(Long id) {
        return productRepository.existsById(id);
    }

}
