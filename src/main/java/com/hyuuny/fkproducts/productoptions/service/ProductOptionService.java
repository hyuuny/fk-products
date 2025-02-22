package com.hyuuny.fkproducts.productoptions.service;

import com.hyuuny.fkproducts.productoptions.domain.ProductOptionEntity;
import com.hyuuny.fkproducts.products.service.ProductReader;
import com.hyuuny.fkproducts.support.error.ErrorType;
import com.hyuuny.fkproducts.support.error.FkProductsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductOptionService {

    private static final int MAX_PRODUCT_OPTIONS = 3;

    private final ProductOptionWriter productOptionWriter;
    private final ProductOptionReader productOptionReader;
    private final ProductReader productReader;
    private final ProductOptionValidator validator;

    @Transactional
    public ProductOptionDto.Response addOption(ProductOptionDto.Create dto) {
        validateProduct(dto.productId());
        ProductOptionEntity productOption = dto.toEntity();
        validator.validate(productOption);
        ProductOptionEntity savedProductOption = productOptionWriter.save(productOption);
        return new ProductOptionDto.Response(savedProductOption);
    }

    private void validateProduct(Long productId) {
        if (!productReader.existsProduct(productId)) {
            throw new FkProductsException(ErrorType.PRODUCT_NOTFOUND, "상품을 찾을 수 없습니다 id:" + productId);
        }
        if (productOptionReader.getProductOptionCount(productId) >= MAX_PRODUCT_OPTIONS) {
            throw new FkProductsException(ErrorType.MAXIMUM_PRODUCT_OPTION_COUNT,
                    String.format("상품에 등록 가능한 옵션 수는 최대 %d개입니다.", MAX_PRODUCT_OPTIONS));
        }
    }

}
