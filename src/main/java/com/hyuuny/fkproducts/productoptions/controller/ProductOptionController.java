package com.hyuuny.fkproducts.productoptions.controller;

import com.hyuuny.fkproducts.productoptions.service.ProductOptionDto;
import com.hyuuny.fkproducts.productoptions.service.ProductOptionService;
import com.hyuuny.fkproducts.support.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/product-options")
@RestController
public class ProductOptionController {

    private final ProductOptionService productOptionService;

    @PostMapping
    public ApiResponse<ProductOptionResponseDto.ResponseDto> addOption(
            @RequestBody @Valid ProductOptionRequestDto.CreateRequest request
    ) {
        ProductOptionDto.Response savedOption = productOptionService.addOption(request.toCreate());
        return ApiResponse.success(new ProductOptionResponseDto.ResponseDto(savedOption));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductOptionResponseDto.ResponseDto> getProductOption(
            @PathVariable Long id
    ) {
        ProductOptionDto.Response productOption = productOptionService.getProductOption(id);
        return ApiResponse.success(new ProductOptionResponseDto.ResponseDto(productOption));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductOptionResponseDto.ResponseDto> updateProductOption(
            @PathVariable Long id,
            @RequestBody @Valid ProductOptionRequestDto.UpdateRequest request
    ) {
        ProductOptionDto.Response updatedProductOption = productOptionService.updateProductOption(id, request.toUpdate());
        return ApiResponse.success(new ProductOptionResponseDto.ResponseDto(updatedProductOption));
    }

}
