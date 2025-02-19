package com.hyuuny.fkproducts.products.controller;

import com.hyuuny.fkproducts.BaseIntegrationTest;
import com.hyuuny.fkproducts.products.domain.ProductEntity;
import com.hyuuny.fkproducts.products.domain.ProductRepository;
import com.hyuuny.fkproducts.support.error.ErrorType;
import com.hyuuny.fkproducts.support.response.ResultType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends BaseIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

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
                .andExpect(jsonPath("data.name").value(request.getName()))
                .andExpect(jsonPath("data.price").value(request.getPrice()))
                .andExpect(jsonPath("data.shippingFee").value(request.getShippingFee()))
                .andExpect(jsonPath("data.description").value(request.getDescription()))
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