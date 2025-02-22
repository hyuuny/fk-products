package com.hyuuny.fkproducts.productoptions.service;

import com.hyuuny.fkproducts.productoptions.domain.OptionItemEntity;
import com.hyuuny.fkproducts.productoptions.domain.ProductOptionEntity;
import com.hyuuny.fkproducts.productoptions.domain.ProductOptionType;

import java.util.List;

public class ProductOptionDto {

    public record Create(
            Long productId,
            String name,
            ProductOptionType optionType,
            Long additionalPrice,
            List<ItemCreate> items
    ) {
        public ProductOptionEntity toEntity() {
            ProductOptionEntity productOption = ProductOptionEntity.builder()
                    .productId(this.productId)
                    .name(this.name)
                    .optionType(this.optionType)
                    .additionalPrice(this.additionalPrice)
                    .build();

            items.stream()
                    .map(ItemCreate::toEntity)
                    .forEach(productOption::addItem);
            return productOption;
        }
    }

    public record ItemCreate(
            String name
    ) {
        public OptionItemEntity toEntity() {
            return OptionItemEntity.builder()
                    .name(this.name)
                    .build();
        }
    }

    public record Response(
            Long id,
            Long productId,
            String name,
            ProductOptionType optionType,
            Long additionalPrice,
            List<ItemResponse> items
    ) {
        public Response(ProductOptionEntity entity) {
            this(
                    entity.getId(),
                    entity.getProductId(),
                    entity.getName(),
                    entity.getOptionType(),
                    entity.getAdditionalPrice(),
                    entity.getItems().stream()
                            .map(ItemResponse::new)
                            .toList()
            );
        }
    }

    public record ItemResponse(
            Long id,
            Long productOptionId,
            String name
    ) {
        public ItemResponse(OptionItemEntity entity) {
            this(
                    entity.getId(),
                    entity.getProductOption().getId(),
                    entity.getName()
            );
        }
    }

}
