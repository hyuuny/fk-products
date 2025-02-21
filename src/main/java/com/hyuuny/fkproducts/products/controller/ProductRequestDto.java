package com.hyuuny.fkproducts.products.controller;

import com.hyuuny.fkproducts.products.service.ProductDto;
import jakarta.validation.constraints.NotNull;

public class ProductRequestDto {

    public record CreateRequest(
            @NotNull String name,
            @NotNull Long price,
            @NotNull Long shippingFee,
            @NotNull String description
    ) {
        public ProductDto.Create toCreate() {
            return new ProductDto.Create(name, price, shippingFee, description);
        }
    }

    public record UpdateRequest(
            @NotNull String name,
            @NotNull Long price,
            @NotNull Long shippingFee,
            @NotNull String description
    ) {
        public ProductDto.Update toUpdate() {
            return new ProductDto.Update(name, price, shippingFee, description);
        }
    }

    public record ProductSearchConditionRequest(
            String name
    ) {
        public ProductDto.ProductSearchCondition toSearchCondition() {
            return new ProductDto.ProductSearchCondition(this.name);
        }
    }
}

