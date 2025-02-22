package com.hyuuny.fkproducts.productoptions.controller;

import com.hyuuny.fkproducts.BaseIntegrationTest;
import com.hyuuny.fkproducts.productoptions.domain.ProductOptionEntity;
import com.hyuuny.fkproducts.productoptions.domain.ProductOptionRepository;
import com.hyuuny.fkproducts.productoptions.domain.ProductOptionType;
import com.hyuuny.fkproducts.productoptions.service.ProductOptionDto;
import com.hyuuny.fkproducts.productoptions.service.ProductOptionService;
import com.hyuuny.fkproducts.products.domain.ProductEntity;
import com.hyuuny.fkproducts.products.domain.ProductRepository;
import com.hyuuny.fkproducts.support.error.ErrorType;
import com.hyuuny.fkproducts.support.response.ResultType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductOptionControllerTest extends BaseIntegrationTest {

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private ProductRepository productRepository;

    private ProductEntity productEntity;

    @BeforeEach
    public void setUp() {
        productEntity = productRepository.save(
                ProductEntity.builder()
                        .name("바나나")
                        .price(5000L)
                        .shippingFee(3000L)
                        .description("당도 높은 바나나예요")
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @AfterEach
    void tearDown() {
        productOptionRepository.deleteAll();
        productRepository.deleteAll();
    }

    @DisplayName("상품에 옵션을 추가할 수 있다")
    @Test
    void addOption() throws Exception {
        ProductOptionRequestDto.CreateRequest request = new ProductOptionRequestDto.CreateRequest(
                productEntity.getId(),
                "사이즈",
                ProductOptionType.SELECTED,
                1000L,
                List.of(
                        new ProductOptionRequestDto.ItemCreateRequest("230"),
                        new ProductOptionRequestDto.ItemCreateRequest("235"),
                        new ProductOptionRequestDto.ItemCreateRequest("240"),
                        new ProductOptionRequestDto.ItemCreateRequest("245")
                )
        );

        mockMvc.perform(post("/api/v1/product-options")
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("result").value(ResultType.SUCCESS.name()))
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.name").value(request.name()))
                .andExpect(jsonPath("data.productId").value(request.productId()))
                .andExpect(jsonPath("data.name").value(request.name()))
                .andExpect(jsonPath("data.optionType").value(request.optionType().name()))
                .andExpect(jsonPath("data.additionalPrice").value(request.additionalPrice()))
                .andExpect(jsonPath("data.items[0].name").value(request.items().get(0).name()))
                .andExpect(jsonPath("data.items[1].name").value(request.items().get(1).name()))
                .andExpect(jsonPath("data.items[2].name").value(request.items().get(2).name()))
                .andExpect(jsonPath("data.items[3].name").value(request.items().get(3).name()));
    }

    @DisplayName("상품이 존재하지 않으면 옵션을 추가할 수 없다")
    @Test
    void addOptionAndProductNotFoundException() throws Exception {
        long invalidProductId = 99999999L;
        ProductOptionRequestDto.CreateRequest request = new ProductOptionRequestDto.CreateRequest(
                invalidProductId,
                "사이즈",
                ProductOptionType.SELECTED,
                1000L,
                List.of(
                        new ProductOptionRequestDto.ItemCreateRequest("230"),
                        new ProductOptionRequestDto.ItemCreateRequest("235"),
                        new ProductOptionRequestDto.ItemCreateRequest("240"),
                        new ProductOptionRequestDto.ItemCreateRequest("245")
                )
        );

        mockMvc.perform(post("/api/v1/product-options")
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error.message").value(ErrorType.PRODUCT_NOTFOUND.getMessage()))
                .andExpect(jsonPath("error.data").value("상품을 찾을 수 없습니다 id:" + invalidProductId));
    }

    @DisplayName("상품 옵션 최대 개수를 초과하면 옵션을 추가할 수 없다")
    @Test
    void addOptionAndMaximumOptionCountException() throws Exception {
        productOptionRepository.saveAll(
                List.of(
                        ProductOptionEntity.builder()
                                .productId(productEntity.getId())
                                .name("함께하면 더 좋아요")
                                .optionType(ProductOptionType.INPUT)
                                .additionalPrice(1000L)
                                .build(),
                        ProductOptionEntity.builder()
                                .productId(productEntity.getId())
                                .name("색상")
                                .optionType(ProductOptionType.INPUT)
                                .additionalPrice(1000L)
                                .build(),
                        ProductOptionEntity.builder()
                                .productId(productEntity.getId())
                                .name("추가")
                                .optionType(ProductOptionType.INPUT)
                                .additionalPrice(1000L)
                                .build())
        );
        ProductOptionRequestDto.CreateRequest request = new ProductOptionRequestDto.CreateRequest(
                productEntity.getId(),
                "사이즈",
                ProductOptionType.SELECTED,
                1000L,
                List.of(
                        new ProductOptionRequestDto.ItemCreateRequest("230"),
                        new ProductOptionRequestDto.ItemCreateRequest("235"),
                        new ProductOptionRequestDto.ItemCreateRequest("240"),
                        new ProductOptionRequestDto.ItemCreateRequest("245")
                )
        );

        mockMvc.perform(post("/api/v1/product-options")
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error.message").value(ErrorType.MAXIMUM_PRODUCT_OPTION_COUNT.getMessage()))
                .andExpect(jsonPath("error.data").value("상품에 등록 가능한 옵션 수는 최대 3개입니다."));
    }

    @DisplayName("선택 타입 옵션에는 선택 가능한 아이템들을 추가해야 한다")
    @Test
    void addOptionAndSelectedOptionItemException() throws Exception {
        ProductOptionRequestDto.CreateRequest request = new ProductOptionRequestDto.CreateRequest(
                productEntity.getId(),
                "사이즈",
                ProductOptionType.SELECTED,
                1000L,
                Collections.emptyList()
        );

        mockMvc.perform(post("/api/v1/product-options")
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error.message").value(ErrorType.INVALID_SELECTED_OPTION_ITEM.getMessage()))
                .andExpect(jsonPath("error.data").value("선택 타입 값은 필수입니다."));
    }

    @DisplayName("입력 타입 옵션에는 선택 가능한 아이템이 존재하면 안된다")
    @Test
    void addOptionAndInputOptionItemException() throws Exception {
        ProductOptionRequestDto.CreateRequest request = new ProductOptionRequestDto.CreateRequest(
                productEntity.getId(),
                "사이즈",
                ProductOptionType.INPUT,
                1000L,
                List.of(
                        new ProductOptionRequestDto.ItemCreateRequest("230"),
                        new ProductOptionRequestDto.ItemCreateRequest("235"),
                        new ProductOptionRequestDto.ItemCreateRequest("240"),
                        new ProductOptionRequestDto.ItemCreateRequest("245")
                )
        );

        mockMvc.perform(post("/api/v1/product-options")
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(ADMIN_EMAIL, ADMIN_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error.message").value(ErrorType.INVALID_INPUT_OPTION_ITEM.getMessage()))
                .andExpect(jsonPath("error.data").value("입력 타입 값에는 아이템을 등록할 수 없습니다."));
    }

    @DisplayName("상품 옵션을 상세조회 할 수 있다")
    @Test
    void getProductOption() throws Exception {
        ProductOptionDto.Create dto = new ProductOptionDto.Create(
                productEntity.getId(),
                "사이즈",
                ProductOptionType.SELECTED,
                1000L,
                List.of(
                        new ProductOptionDto.ItemCreate("230"),
                        new ProductOptionDto.ItemCreate("235"),
                        new ProductOptionDto.ItemCreate("240"),
                        new ProductOptionDto.ItemCreate("245")
                )
        );
        ProductOptionDto.Response productOption = productOptionService.addOption(dto);

        mockMvc.perform(get("/api/v1/product-options/{id}", productOption.id())
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(CUSTOMER_EMAIL, CUSTOMER_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(jsonPath("result").value(ResultType.SUCCESS.name()))
                .andExpect(jsonPath("data.id").exists())
                .andExpect(jsonPath("data.name").value(dto.name()))
                .andExpect(jsonPath("data.productId").value(dto.productId()))
                .andExpect(jsonPath("data.name").value(dto.name()))
                .andExpect(jsonPath("data.optionType").value(dto.optionType().name()))
                .andExpect(jsonPath("data.additionalPrice").value(dto.additionalPrice()))
                .andExpect(jsonPath("data.items[0].name").value(dto.items().get(0).name()))
                .andExpect(jsonPath("data.items[1].name").value(dto.items().get(1).name()))
                .andExpect(jsonPath("data.items[2].name").value(dto.items().get(2).name()))
                .andExpect(jsonPath("data.items[3].name").value(dto.items().get(3).name()));
    }

    @DisplayName("존재하지 않는 상품 옵션을 상세조회 할 수 있다")
    @Test
    void getProductOptionAndNotFoundException() throws Exception {
        long invalidId = 999999999L;

        mockMvc.perform(get("/api/v1/product-options/{id}", invalidId)
                        .header(HttpHeaders.AUTHORIZATION, this.getBearerToken(CUSTOMER_EMAIL, CUSTOMER_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error.message").value(ErrorType.PRODUCT_OPTION_NOTFOUND.getMessage()))
                .andExpect(jsonPath("error.data").value("상품 옵션을 찾을 수 없습니다 id:" + invalidId));
    }

}