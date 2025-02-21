package com.hyuuny.fkproducts.products.service;

import com.hyuuny.fkproducts.products.domain.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    public Page<ProductDto.Responses> getProducts(ProductDto.ProductSearchCondition searchCondition, Pageable pageable) {
        Page<ProductEntity> page = productReader.search(searchCondition, pageable);
        List<ProductDto.Responses> content = page.getContent().stream()
                .map(ProductDto.Responses::new)
                .toList();
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    public ProductDto.Response getProduct(Long id) {
        ProductEntity product = productReader.read(id);
        return new ProductDto.Response(product);
    }

    @Transactional
    public ProductDto.Response updateProduct(Long id, ProductDto.Update dto) {
        validator.validate(dto.price());
        ProductEntity product = productReader.read(id);
        dto.update(product);
        return new ProductDto.Response(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        ProductEntity product = productReader.read(id);
        productWriter.delete(product);
    }
}
