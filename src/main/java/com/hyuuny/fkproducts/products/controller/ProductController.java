package com.hyuuny.fkproducts.products.controller;

import com.hyuuny.fkproducts.products.service.ProductDto;
import com.hyuuny.fkproducts.products.service.ProductService;
import com.hyuuny.fkproducts.support.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ApiResponse<ProductResponseDto.ResponseDto> getProduct(
            @PathVariable Long id
    ) {
        ProductDto.Response product = productService.getProduct(id);
        return ApiResponse.success(new ProductResponseDto.ResponseDto(product));
    }

}
