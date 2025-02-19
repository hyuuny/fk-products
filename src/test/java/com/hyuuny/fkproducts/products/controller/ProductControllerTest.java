package com.hyuuny.fkproducts.products.controller;

import com.hyuuny.fkproducts.BaseIntegrationTest;
import com.hyuuny.fkproducts.products.domain.ProductRepository;
import com.hyuuny.fkproducts.support.error.ErrorType;
import com.hyuuny.fkproducts.support.response.ResultType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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
}