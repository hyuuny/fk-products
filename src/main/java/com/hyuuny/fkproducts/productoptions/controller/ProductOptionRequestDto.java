package com.hyuuny.fkproducts.productoptions.controller;

import com.hyuuny.fkproducts.productoptions.domain.ProductOptionType;
import com.hyuuny.fkproducts.productoptions.service.ProductOptionDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ProductOptionRequestDto {

    public record CreateRequest(
            @NotNull Long productId,
            @NotNull String name,
            @NotNull ProductOptionType optionType,
            @NotNull Long additionalPrice,
            List<ItemCreateRequest> items
    ) {
        public ProductOptionDto.Create toCreate() {
            return new ProductOptionDto.Create(
                    productId,
                    name,
                    optionType,
                    additionalPrice,
                    items.stream()
                            .map(ItemCreateRequest::toItemCreate)
                            .toList()
            );
        }
    }

    public record ItemCreateRequest(
            @NotNull String name
    ) {
        public ProductOptionDto.ItemCreate toItemCreate() {
            return new ProductOptionDto.ItemCreate(name);
        }
    }

    public record UpdateRequest(
            @NotNull Long productId,
            @NotNull String name,
            @NotNull ProductOptionType optionType,
            @NotNull Long additionalPrice,
            List<ItemCreateRequest> items
    ) {
        public ProductOptionDto.Update toUpdate() {
            return new ProductOptionDto.Update(
                    productId,
                    name,
                    optionType,
                    additionalPrice,
                    items.stream()
                            .map(ItemCreateRequest::toItemCreate)
                            .toList()
            );
        }
    }

}

