package com.hyuuny.fkproducts.products.controller;

import com.hyuuny.fkproducts.products.service.ProductDto;
import com.hyuuny.fkproducts.products.service.ProductService;
import com.hyuuny.fkproducts.support.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

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

    @GetMapping
    public ApiResponse<Page<ProductResponseDto.ResponsesDto>> getProducts(
            ProductRequestDto.ProductSearchConditionRequest request,
            @PageableDefault(sort = "id", direction = DESC) Pageable pageable
    ) {
        Page<ProductResponseDto.ResponsesDto> page = productService.getProducts(request.toSearchCondition(), pageable)
                .map(ProductResponseDto.ResponsesDto::new);
        return ApiResponse.success(page);
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponseDto.ResponseDto> getProduct(
            @PathVariable Long id
    ) {
        ProductDto.Response product = productService.getProduct(id);
        return ApiResponse.success(new ProductResponseDto.ResponseDto(product));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponseDto.ResponseDto> updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid ProductRequestDto.UpdateRequest request
    ) {
        ProductDto.Response updatedProduct = productService.updateProduct(id, request.toUpdate());
        return ApiResponse.success(new ProductResponseDto.ResponseDto(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteProduct(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
        return ApiResponse.success();
    }
}
