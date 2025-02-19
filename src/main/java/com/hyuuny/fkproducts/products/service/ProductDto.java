package com.hyuuny.fkproducts.products.service;

import com.hyuuny.fkproducts.products.domain.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ProductDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {

        private String name;

        private Long price;

        private Long shippingFee;

        private String description;

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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long id;

        private String name;

        private Long price;

        private Long shippingFee;

        private String description;

        private LocalDateTime createdAt;

        public Response(ProductEntity entity) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.price = entity.getPrice();
            this.shippingFee = entity.getShippingFee();
            this.description = entity.getDescription();
            this.createdAt = entity.getCreatedAt();
        }
    }
}