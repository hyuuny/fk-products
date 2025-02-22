package com.hyuuny.fkproducts.productoptions.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Long> {

    Long countByProductId(Long productId);

}
