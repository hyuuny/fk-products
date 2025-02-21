package com.hyuuny.fkproducts.products.service;

import com.hyuuny.fkproducts.products.domain.ProductEntity;
import com.hyuuny.fkproducts.support.error.ErrorType;
import com.hyuuny.fkproducts.support.error.FkProductsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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

        assertThat(savedProduct.id()).isEqualTo(product.getId());
        assertThat(savedProduct.name()).isEqualTo(product.getName());
        assertThat(savedProduct.price()).isEqualTo(product.getPrice());
        assertThat(savedProduct.shippingFee()).isEqualTo(product.getShippingFee());
        assertThat(savedProduct.description()).isEqualTo(product.getDescription());
        assertThat(savedProduct.createdAt()).isEqualTo(product.getCreatedAt());
    }

    @DisplayName("상품 가격이 0원 이하이면 상품을 등록할 수 없다")
    @Test
    void createProductAndPriceException() {
        ProductDto.Create dto = new ProductDto.Create("바나나", 0L, 3000L, "당도 높은 바나나예요");
        doThrow(new FkProductsException(ErrorType.INVALID_PRODUCT_PRICE, "상품 가격은 0보다 커야 합니다."))
                .when(productValidator).validate(any());

        FkProductsException exception = assertThrows(FkProductsException.class, () -> productService.createProduct(dto));

        assertThat(exception.getMessage()).isEqualTo("invalid productPrice");
    }

    @DisplayName("상품을 상세 조회할 수 있다")
    @Test
    void getProduct() {
        ProductEntity product = generateProduct();
        when(productReader.read(any())).thenReturn(product);

        ProductDto.Response response = productService.getProduct(product.getId());

        assertThat(response.id()).isEqualTo(product.getId());
        assertThat(response.name()).isEqualTo(product.getName());
        assertThat(response.price()).isEqualTo(product.getPrice());
        assertThat(response.shippingFee()).isEqualTo(product.getShippingFee());
        assertThat(response.description()).isEqualTo(product.getDescription());
        assertThat(response.createdAt()).isEqualTo(product.getCreatedAt());
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

    @DisplayName("상품을 수정할 수 있다")
    @Test
    void updateProduct() {
        ProductEntity product = generateProduct();
        ProductDto.Update dto = new ProductDto.Update("사과", 6000L, 3500L, "맛있는 사과예요");
        doNothing().when(productValidator).validate(any());
        when(productReader.read(any())).thenReturn(product);

        ProductDto.Response response = productService.updateProduct(product.getId(), dto);

        assertThat(response.id()).isEqualTo(product.getId());
        assertThat(response.name()).isEqualTo(dto.name());
        assertThat(response.price()).isEqualTo(dto.price());
        assertThat(response.shippingFee()).isEqualTo(dto.shippingFee());
        assertThat(response.description()).isEqualTo(dto.description());
    }

    @DisplayName("상품 가격이 0원 이하이면 상품을 수정할 수 없다")
    @Test
    void updateProductAndPriceException() {
        ProductEntity product = generateProduct();
        ProductDto.Update dto = new ProductDto.Update("사과", 0L, 3500L, "맛있는 사과예요");
        doThrow(new FkProductsException(ErrorType.INVALID_PRODUCT_PRICE, "상품 가격은 0보다 커야 합니다."))
                .when(productValidator).validate(any());

        FkProductsException exception = assertThrows(FkProductsException.class, () -> productService.updateProduct(product.getId(), dto));

        assertThat(exception.getMessage()).isEqualTo("invalid productPrice");
    }

    @DisplayName("상품을 삭제 할 수 있다")
    @Test
    void deleteProduct() {
        ProductEntity product = generateProduct();
        when(productReader.read(product.getId())).thenReturn(product);
        doNothing().when(productWriter).delete(any());

        productService.deleteProduct(product.getId());

        verify(productReader).read(product.getId());
        verify(productWriter).delete(product);
    }

    @DisplayName("존재하지 않는 상품을 삭제 할 수 없다")
    @Test
    void deleteProductAndNotFoundException() {
        long invalidId = 999999999L;
        doThrow(new FkProductsException(ErrorType.PRODUCT_NOTFOUND, "상품을 찾을 수 없습니다 id:" + invalidId))
                .when(productReader).read(any());

        FkProductsException exception = assertThrows(FkProductsException.class, () -> productService.deleteProduct(invalidId));

        assertThat(exception.getMessage()).isEqualTo("product notFound");
    }

    @DisplayName("상품 목록을 조회 및 검색할 수 있다")
    @Test
    void getProducts() {
        ProductDto.ProductSearchCondition searchCondition = new ProductDto.ProductSearchCondition(null);
        List<ProductEntity> products = Arrays.asList(
                new ProductEntity(1L, "사과", 2000L, 3000L, "맛있는 사과예요", LocalDateTime.now()),
                new ProductEntity(2L, "포도", 3000L, 3000L, "맛있는 포도예요", LocalDateTime.now()),
                new ProductEntity(3L, "수박", 4000L, 3000L, "맛있는 수박이예요", LocalDateTime.now()),
                new ProductEntity(4L, "딸기", 5000L, 3000L, "맛있는 딸기예요", LocalDateTime.now()),
                new ProductEntity(5L, "바나나", 3000L, 3000L, "맛있는 바나나예요", LocalDateTime.now()),
                new ProductEntity(6L, "참외", 4000L, 3000L, "맛있는 참외예요", LocalDateTime.now()),
                new ProductEntity(7L, "아메리카노", 1500L, 3000L, "맛있는 아메리카노예요", LocalDateTime.now()),
                new ProductEntity(8L, "카페라떼", 2000L, 3000L, "맛있는 카페라떼예요", LocalDateTime.now()),
                new ProductEntity(9L, "핫초코", 2000L, 3000L, "맛있는 핫초코예요", LocalDateTime.now()),
                new ProductEntity(10L, "카푸치노", 25000L, 3000L, "맛있는 카푸치노예요", LocalDateTime.now())
        );
        PageRequest pageable = PageRequest.of(0, 10);
        Page<ProductEntity> page = new PageImpl<>(products, pageable, products.size());
        when(productReader.search(any(), any())).thenReturn(page);

        Page<ProductDto.Responses> search = productService.getProducts(searchCondition, pageable);

        assertThat(search.getTotalElements()).isEqualTo(10);
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