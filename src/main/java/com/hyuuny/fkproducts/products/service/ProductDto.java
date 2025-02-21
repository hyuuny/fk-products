package com.hyuuny.fkproducts.products.service;

import com.hyuuny.fkproducts.products.domain.ProductEntity;

import java.time.LocalDateTime;

public class ProductDto {

    public record Create(
            String name,
            Long price,
            Long shippingFee,
            String description
    ) {
        public ProductEntity toEntity(LocalDateTime createdAt) {
            return ProductEntity.builder()
                    .name(this.name)
                    .price(this.price)
                    .shippingFee(this.shippingFee)
                    .description(this.description)
                    .createdAt(createdAt)
                    .build();
        }
    }

    public record Responses(
            Long id,
            String name,
            Long price,
            LocalDateTime createdAt
    ) {
        public Responses(ProductEntity entity) {
            this(
                    entity.getId(),
                    entity.getName(),
                    entity.getPrice(),
                    entity.getCreatedAt()
            );
        }
    }

    public record Response(
            Long id,
            String name,
            Long price,
            Long shippingFee,
            String description,
            LocalDateTime createdAt
    ) {
        public Response(ProductEntity entity) {
            this(
                    entity.getId(),
                    entity.getName(),
                    entity.getPrice(),
                    entity.getShippingFee(),
                    entity.getDescription(),
                    entity.getCreatedAt()
            );
        }
    }

    public record Update(
            String name,
            Long price,
            Long shippingFee,
            String description
    ) {
        public void update(ProductEntity entity) {
            entity.changeName(this.name);
            entity.changePrice(this.price);
            entity.changeShippingFee(this.shippingFee);
            entity.changeDescription(this.description);
        }
    }

    public record ProductSearchCondition(
            String name
    ) {
    }
}