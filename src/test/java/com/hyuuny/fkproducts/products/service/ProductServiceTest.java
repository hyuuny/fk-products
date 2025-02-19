package com.hyuuny.fkproducts.products.service;

import com.hyuuny.fkproducts.products.domain.ProductEntity;
import com.hyuuny.fkproducts.support.error.ErrorType;
import com.hyuuny.fkproducts.support.error.FkProductsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductWriter productWriter;

    private ProductValidator productValidator;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productWriter = mock(ProductWriter.class);
        productValidator = mock(ProductValidator.class);
        productService = new ProductService(productWriter, productValidator);
    }

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void createProduct() {
        ProductDto.Create dto = new ProductDto.Create("바나나", 5000L, 3000L, "당도 높은 바나나예요");
        ProductEntity product = new ProductEntity(1L, dto.getName(), dto.getPrice(), dto.getShippingFee(), dto.getDescription(), LocalDateTime.now());
        doNothing().when(productValidator).validate(any());
        when(productWriter.save(any())).thenReturn(product);

        ProductDto.Response savedProduct = productService.createProduct(dto);

        assertThat(savedProduct.getId()).isEqualTo(product.getId());
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(savedProduct.getShippingFee()).isEqualTo(product.getShippingFee());
        assertThat(savedProduct.getDescription()).isEqualTo(product.getDescription());
        assertThat(savedProduct.getCreatedAt()).isEqualTo(product.getCreatedAt());
    }

    @DisplayName("상품 가격이 0원 이하이면 상품을 등록할 수 없다")
    @Test
    void createProductAndPriceException() {
        ProductDto.Create dto = new ProductDto.Create("바나나", 0L, 3000L, "당도 높은 바나나예요");
        doThrow(new FkProductsException(ErrorType.INVALID_PRODUCT_PRICE, "상품 가격은 0보다 커야 합니다."))
                .when(productValidator).validate(any(ProductEntity.class));

        FkProductsException exception = assertThrows(FkProductsException.class, () -> productService.createProduct(dto));

        assertThat(exception.getMessage()).isEqualTo("invalid productPrice");
    }
}