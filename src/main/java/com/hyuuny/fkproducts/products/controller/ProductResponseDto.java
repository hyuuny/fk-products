package com.hyuuny.fkproducts.products.controller;

import com.hyuuny.fkproducts.products.service.ProductDto;

import java.time.LocalDateTime;

public class ProductResponseDto {

    public record ResponseDto(
            Long id,
            String name,
            Long price,
            Long shippingFee,
            String description,
            LocalDateTime createdAt
    ) {
        public ResponseDto(ProductDto.Response response) {
            this(
                    response.id(),
                    response.name(),
                    response.price(),
                    response.shippingFee(),
                    response.description(),
                    response.createdAt()
            );
        }
    }

    public record ResponsesDto(
            Long id,
            String name,
            Long price,
            LocalDateTime createdAt
    ) {
        public ResponsesDto(ProductDto.Responses responses) {
            this(
                    responses.id(),
                    responses.name(),
                    responses.price(),
                    responses.createdAt()
            );
        }
    }

}