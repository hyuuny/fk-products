package com.hyuuny.fkproducts.products.controller;

import com.hyuuny.fkproducts.products.service.ProductDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {

        @NotNull
        private String name;

        @NotNull
        private Long price;

        @NotNull
        private Long shippingFee;

        @NotNull
        private String description;

        public ProductDto.Create toCreate() {
            return ProductDto.Create.builder()
                    .name(this.name)
                    .price(this.price)
                    .shippingFee(this.shippingFee)
                    .description(this.description)
                    .build();
        }
    }

}
