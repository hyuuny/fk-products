package com.hyuuny.fkproducts.products.controller;

import com.hyuuny.fkproducts.BaseIntegrationTest;
import com.hyuuny.fkproducts.products.domain.ProductEntity;
import com.hyuuny.fkproducts.products.domain.ProductRepository;
import com.hyuuny.fkproducts.products.service.ProductDto;
import com.hyuuny.fkproducts.products.service.ProductService;
import com.hyuuny.fkproducts.support.error.ErrorType;
import com.hyuuny.fkproducts.support.response.ResultType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends BaseIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @DisplayName("상품을 등록할 수 있다")
    @Test
    void createProduct() throws Exception {
        ProductRequestDto.CreateRequest request = new ProductRequestDto.CreateRequest("바나나", 5000L, 3000L, "당도 높은 바나나예요");

        mockMvc.perform(post("/api/v1/products")
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("result").value(ResultType.SUCCESS.name()))
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.name").value(request.name()))
                .andExpect(jsonPath("data.price").value(request.price()))
                .andExpect(jsonPath("data.shippingFee").value(request.shippingFee()))
                .andExpect(jsonPath("data.description").value(request.description()))
                .andExpect(jsonPath("data.createdAt").exists());
    }

    @DisplayName("상품 가격이 0원 이하이면 상품을 등록할 수 없다")
    @Test
    void createProductAndPriceException() throws Exception {
        ProductRequestDto.CreateRequest request = new ProductRequestDto.CreateRequest("바나나", 0L, 3000L, "당도 높은 바나나예요");

        mockMvc.perform(post("/api/v1/products")
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error.message").value(ErrorType.INVALID_PRODUCT_PRICE.getMessage()))
                .andExpect(jsonPath("error.data").value("상품 가격은 0보다 커야 합니다."));
    }

    @DisplayName("상품을 상세 조회할 수 있다")
    @Test
    void getProduct() throws Exception {
        ProductEntity product = generateProduct();

        mockMvc.perform(get("/api/v1/products/{id}", product.getId())
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(CUSTOMER_EMAIL, CUSTOMER_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("result").value(ResultType.SUCCESS.name()))
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.name").value(product.getName()))
                .andExpect(jsonPath("data.price").value(product.getPrice()))
                .andExpect(jsonPath("data.shippingFee").value(product.getShippingFee()))
                .andExpect(jsonPath("data.description").value(product.getDescription()))
                .andExpect(jsonPath("data.createdAt").exists());
    }

    @DisplayName("존재하지 않는 상품은 조회할 수 없다")
    @Test
    void getProductAndNotFoundException() throws Exception {
        long invalidId = 999999999L;

        mockMvc.perform(get("/api/v1/products/{id}", invalidId)
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(CUSTOMER_EMAIL, CUSTOMER_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error.message").value(ErrorType.PRODUCT_NOTFOUND.getMessage()))
                .andExpect(jsonPath("error.data").value("상품을 찾을 수 없습니다 id:" + invalidId));
    }

    @DisplayName("상품을 수정할 수 있다")
    @Test
    void updateProduct() throws Exception {
        ProductEntity product = generateProduct();
        ProductDto.Update request = new ProductDto.Update("사과", 6000L, 3500L, "맛있는 사과예요");

        mockMvc.perform(put("/api/v1/products/{id}", product.getId())
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("result").value(ResultType.SUCCESS.name()))
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.name").value(request.name()))
                .andExpect(jsonPath("data.price").value(request.price()))
                .andExpect(jsonPath("data.shippingFee").value(request.shippingFee()))
                .andExpect(jsonPath("data.description").value(request.description()))
                .andExpect(jsonPath("data.createdAt").exists());
    }

    @DisplayName("상품 가격이 0원 이하이면 상품을 수정할 수 없다")
    @Test
    void updateProductAndPriceException() throws Exception {
        ProductEntity product = generateProduct();
        ProductDto.Update request = new ProductDto.Update("사과", 0L, 3500L, "맛있는 사과예요");

        mockMvc.perform(put("/api/v1/products/{id}", product.getId())
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error.message").value(ErrorType.INVALID_PRODUCT_PRICE.getMessage()))
                .andExpect(jsonPath("error.data").value("상품 가격은 0보다 커야 합니다."));
    }

    @DisplayName("상품을 삭제 할 수 있다")
    @Test
    void deleteProduct() throws Exception {
        ProductEntity product = generateProduct();

        mockMvc.perform(delete("/api/v1/products/{id}", product.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("result").value(ResultType.SUCCESS.name()));
    }

    @DisplayName("존재하지 않는 상품을 삭제 할 수 없다")
    @Test
    void deleteProductAndNotFoundException() throws Exception {
        long invalidId = 999999999L;

        mockMvc.perform(delete("/api/v1/products/{id}", invalidId)
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error.message").value(ErrorType.PRODUCT_NOTFOUND.getMessage()))
                .andExpect(jsonPath("error.data").value("상품을 찾을 수 없습니다 id:" + invalidId));
    }

    @DisplayName("상품 목록을 조회 및 검색할 수 있다")
    @Test
    void getProducts() throws Exception {
        List<ProductDto.Create> products = Arrays.asList(
                new ProductDto.Create("사과", 2000L, 3000L, "맛있는 사과예요"),
                new ProductDto.Create("포도", 3000L, 3000L, "맛있는 포도예요"),
                new ProductDto.Create("수박", 4000L, 3000L, "맛있는 수박이예요"),
                new ProductDto.Create("딸기", 5000L, 3000L, "맛있는 딸기예요"),
                new ProductDto.Create("바나나", 3000L, 3000L, "맛있는 바나나예요"),
                new ProductDto.Create("참외", 4000L, 3000L, "맛있는 참외예요"),
                new ProductDto.Create("아메리카노", 1500L, 3000L, "맛있는 아메리카노예요"),
                new ProductDto.Create("카페라떼", 2000L, 3000L, "맛있는 카페라떼예요"),
                new ProductDto.Create("핫초코", 2000L, 3000L, "맛있는 핫초코예요"),
                new ProductDto.Create("카푸치노", 25000L, 3000L, "맛있는 카푸치노예요")
        );
        List<ProductDto.Response> savedProducts = products.stream()
                .map(productService::createProduct)
                .toList();

        mockMvc.perform(get("/api/v1/products")
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(CUSTOMER_EMAIL, CUSTOMER_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.content[0].name").value(savedProducts.get(9).name()))
                .andExpect(jsonPath("data.content[1].name").value(savedProducts.get(8).name()))
                .andExpect(jsonPath("data.content[2].name").value(savedProducts.get(7).name()))
                .andExpect(jsonPath("data.content[3].name").value(savedProducts.get(6).name()))
                .andExpect(jsonPath("data.content[4].name").value(savedProducts.get(5).name()))
                .andExpect(jsonPath("data.content[5].name").value(savedProducts.get(4).name()))
                .andExpect(jsonPath("data.content[6].name").value(savedProducts.get(3).name()))
                .andExpect(jsonPath("data.content[7].name").value(savedProducts.get(2).name()))
                .andExpect(jsonPath("data.content[8].name").value(savedProducts.get(1).name()))
                .andExpect(jsonPath("data.content[9].name").value(savedProducts.get(0).name()))
                .andExpect(jsonPath("data.totalPages").value(1))
                .andExpect(jsonPath("data.totalElements").value(10))
                .andExpect(jsonPath("data.last").value(true));
    }

    private ProductEntity generateProduct() {
        return productRepository.save(
                ProductEntity.builder()
                        .name("바나나")
                        .price(5000L)
                        .shippingFee(3000L)
                        .description("당도 높은 바나나예요")
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

}