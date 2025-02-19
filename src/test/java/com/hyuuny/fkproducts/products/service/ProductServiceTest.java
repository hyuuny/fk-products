package com.hyuuny.fkproducts.products.service;

import com.hyuuny.fkproducts.products.domain.ProductEntity;
import com.hyuuny.fkproducts.support.error.ErrorType;
import com.hyuuny.fkproducts.support.error.FkProductsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductWriter productWriter;

    private ProductReader productReader;

    private ProductValidator productValidator;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productWriter = mock(ProductWriter.class);
        productReader = mock(ProductReader.class);
        productValidator = mock(ProductValidator.class);
        productService = new ProductService(productWriter, productReader, productValidator);
    }

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void createProduct() {
        ProductDto.Create dto = new ProductDto.Create("바나나", 5000L, 3000L, "당도 높은 바나나예요");
        ProductEntity product = generateProduct();
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

    @DisplayName("상품을 상세 조회할 수 있다")
    @Test
    void getProduct() {
        ProductEntity product = generateProduct();
        when(productReader.read(any())).thenReturn(product);

        ProductDto.Response response = productService.getProduct(product.getId());

        assertThat(response.getId()).isEqualTo(product.getId());
        assertThat(response.getName()).isEqualTo(product.getName());
        assertThat(response.getPrice()).isEqualTo(product.getPrice());
        assertThat(response.getShippingFee()).isEqualTo(product.getShippingFee());
        assertThat(response.getDescription()).isEqualTo(product.getDescription());
        assertThat(response.getCreatedAt()).isEqualTo(product.getCreatedAt());
    }

    @DisplayName("존재하지 않는 상품은 조회할 수 없다")
    @Test
    void getProductAndNotFoundException() {
        long invalidId = 999999999L;
        doThrow(new FkProductsException(ErrorType.PRODUCT_NOTFOUND, "상품을 찾을 수 없습니다 id:" + invalidId))
                .when(productReader).read(any());

        FkProductsException exception = assertThrows(FkProductsException.class, () -> productService.getProduct(invalidId));

        assertThat(exception.getMessage()).isEqualTo("product notFound");
    }

    private ProductEntity generateProduct() {
        return ProductEntity.builder()
                .name("바나나")
                .price(5000L)
                .shippingFee(3000L)
                .description("당도 높은 바나나예요")
                .build();
    }
}