package com.hyuuny.fkproducts.productoptions.controller;

import com.hyuuny.fkproducts.productoptions.domain.ProductOptionType;
import com.hyuuny.fkproducts.productoptions.service.ProductOptionDto;

import java.util.List;

public class ProductOptionResponseDto {

    public record ResponseDto(
            Long id,
            Long productId,
            String name,
            ProductOptionType optionType,
            Long additionalPrice,
            List<ItemResponseDto> items
    ) {
        public ResponseDto(ProductOptionDto.Response response) {
            this(
                    response.id(),
                    response.productId(),
                    response.name(),
                    response.optionType(),
                    response.additionalPrice(),
                    response.items().stream()
                            .map(ItemResponseDto::new)
                            .toList()
            );
        }
    }

    public record ItemResponseDto(
            Long id,
            Long productOptionId,
            String name
    ) {
        public ItemResponseDto(ProductOptionDto.ItemResponse response) {
            this(
                    response.id(),
                    response.productOptionId(),
                    response.name()
            );
        }
    }
}
