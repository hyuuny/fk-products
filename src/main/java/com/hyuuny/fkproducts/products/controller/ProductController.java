package com.hyuuny.fkproducts.products.controller;

import com.hyuuny.fkproducts.products.service.ProductDto;
import com.hyuuny.fkproducts.products.service.ProductService;
import com.hyuuny.fkproducts.support.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ApiResponse<ProductResponseDto.ResponseDto> createProduct(
            @RequestBody @Valid ProductRequestDto.CreateRequest request
    ) {
        ProductDto.Response savedProduct = productService.createProduct(request.toCreate());
        return ApiResponse.success(new ProductResponseDto.ResponseDto(savedProduct));
    }

}
