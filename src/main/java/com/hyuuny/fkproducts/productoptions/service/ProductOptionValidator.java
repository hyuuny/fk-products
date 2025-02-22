package com.hyuuny.fkproducts.productoptions.service;

import com.hyuuny.fkproducts.productoptions.domain.OptionItemEntity;
import com.hyuuny.fkproducts.productoptions.domain.ProductOptionEntity;
import com.hyuuny.fkproducts.productoptions.domain.ProductOptionType;
import com.hyuuny.fkproducts.support.error.ErrorType;
import com.hyuuny.fkproducts.support.error.FkProductsException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductOptionValidator {

    public void validate(ProductOptionEntity productOption) {
        List<OptionItemEntity> items = productOption.getItems();
        ProductOptionType optionType = productOption.getOptionType();

        if (optionType == ProductOptionType.SELECTED && items.isEmpty()) {
            throw new FkProductsException(ErrorType.INVALID_SELECTED_OPTION_ITEM, "선택 타입 값은 필수입니다.");
        }

        if (optionType == ProductOptionType.INPUT && !items.isEmpty()) {
            throw new FkProductsException(ErrorType.INVALID_INPUT_OPTION_ITEM, "입력 타입 값에는 아이템을 등록할 수 없습니다.");
        }
    }

}
