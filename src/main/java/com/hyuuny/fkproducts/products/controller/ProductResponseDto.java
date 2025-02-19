package com.hyuuny.fkproducts.products.controller;

import com.hyuuny.fkproducts.products.service.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ProductResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseDto {

        private Long id;

        private String name;

        private Long price;

        private Long shippingFee;

        private String description;

        private LocalDateTime createdAt;

        public ResponseDto(ProductDto.Response response) {
            this.id = response.getId();
            this.name = response.getName();
            this.price = response.getPrice();
            this.shippingFee = response.getShippingFee();
            this.description = response.getDescription();
            this.createdAt = response.getCreatedAt();
        }
    }

}
