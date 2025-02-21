package com.hyuuny.fkproducts.products.domain;

import com.hyuuny.fkproducts.products.service.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<ProductEntity> search(ProductDto.ProductSearchCondition searchCondition, Pageable pageable);

}
