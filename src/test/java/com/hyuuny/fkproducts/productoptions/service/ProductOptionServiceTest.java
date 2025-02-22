package com.hyuuny.fkproducts.productoptions.service;

import com.hyuuny.fkproducts.productoptions.domain.OptionItemEntity;
import com.hyuuny.fkproducts.productoptions.domain.ProductOptionEntity;
import com.hyuuny.fkproducts.productoptions.domain.ProductOptionType;
import com.hyuuny.fkproducts.products.service.ProductReader;
import com.hyuuny.fkproducts.support.error.ErrorType;
import com.hyuuny.fkproducts.support.error.FkProductsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductOptionServiceTest {

    private ProductOptionWriter productOptionWriter;

    private ProductOptionReader productOptionReader;

    private ProductReader productReader;

    private ProductOptionValidator productOptionValidator;

    private ProductOptionService productOptionService;

    @BeforeEach
    void setUp() {
        productOptionWriter = mock(ProductOptionWriter.class);
        productOptionReader = mock(ProductOptionReader.class);
        productReader = mock(ProductReader.class);
        productOptionValidator = mock(ProductOptionValidator.class);
        productOptionService = new ProductOptionService(productOptionWriter, productOptionReader, productReader, productOptionValidator);
    }

    @DisplayName("상품에 옵션을 추가할 수 있다")
    @Test
    void addOption() {
        ProductOptionDto.Create dto = new ProductOptionDto.Create(
                1L,
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
        ProductOptionEntity productOption = ProductOptionEntity.builder()
                .productId(1L)
                .name("사이즈")
                .optionType(ProductOptionType.SELECTED)
                .additionalPrice(1000L)
                .build();
        List<OptionItemEntity> items = List.of(
                OptionItemEntity.builder().id(1L).name("230").productOption(productOption).build(),
                OptionItemEntity.builder().id(2L).name("235").productOption(productOption).build(),
                OptionItemEntity.builder().id(3L).name("240").productOption(productOption).build(),
                OptionItemEntity.builder().id(4L).name("245").productOption(productOption).build()
        );
        items.forEach(productOption::addItem);

        when(productReader.existsProduct(any())).thenReturn(true);
        when(productOptionReader.getProductOptionCount(any())).thenReturn(0L);
        doNothing().when(productOptionValidator).validate(any());
        when(productOptionWriter.save(any())).thenReturn(productOption);

        ProductOptionDto.Response savedProductOption = productOptionService.addOption(dto);

        assertThat(savedProductOption.id()).isEqualTo(productOption.getId());
        assertThat(savedProductOption.name()).isEqualTo(productOption.getName());
        assertThat(savedProductOption.optionType()).isEqualTo(productOption.getOptionType());
        assertThat(savedProductOption.additionalPrice()).isEqualTo(productOption.getAdditionalPrice());
        assertThat(savedProductOption.items().size()).isEqualTo(items.size());
    }

    @DisplayName("상품이 존재하지 않으면 옵션을 추가할 수 없다")
    @Test
    void addOptionAndProductNotFoundException() {
        ProductOptionDto.Create dto = new ProductOptionDto.Create(
                1L,
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
        doThrow(new FkProductsException(ErrorType.PRODUCT_NOTFOUND, "상품을 찾을 수 없습니다 id:1"))
                .when(productReader).existsProduct(any());

        FkProductsException exception = assertThrows(FkProductsException.class, () -> productOptionService.addOption(dto));

        assertThat(exception.getMessage()).isEqualTo("product notFound");
    }

    @DisplayName("상품 옵션 최대 개수를 초과하면 옵션을 추가할 수 없다")
    @Test
    void addOptionAndMaximumOptionCountException() {
        ProductOptionDto.Create dto = new ProductOptionDto.Create(
                1L,
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
        when(productReader.existsProduct(any())).thenReturn(true);
        doThrow(new FkProductsException(ErrorType.MAXIMUM_PRODUCT_OPTION_COUNT, "상품에 등록 가능한 옵션 수는 최대 3개입니다."))
                .when(productOptionReader).getProductOptionCount(any());

        FkProductsException exception = assertThrows(FkProductsException.class, () -> productOptionService.addOption(dto));

        assertThat(exception.getMessage()).isEqualTo("maximum productOptionCount");
    }

    @DisplayName("선택 타입 옵션에는 선택 가능한 아이템들을 추가해야 한다")
    @Test
    void addOptionAndSelectedOptionItemException() {
        ProductOptionDto.Create dto = new ProductOptionDto.Create(
                1L,
                "사이즈",
                ProductOptionType.SELECTED,
                1000L,
                Collections.emptyList()
        );
        when(productReader.existsProduct(any())).thenReturn(true);
        when(productOptionReader.getProductOptionCount(any())).thenReturn(0L);
        doThrow(new FkProductsException(ErrorType.INVALID_SELECTED_OPTION_ITEM, "선택 타입 값은 필수입니다."))
                .when(productOptionValidator).validate(any());

        FkProductsException exception = assertThrows(FkProductsException.class, () -> productOptionService.addOption(dto));

        assertThat(exception.getMessage()).isEqualTo("invalid selectedOptionItem");
    }

    @DisplayName("입력 타입 옵션에는 선택 가능한 아이템이 존재하면 안된다")
    @Test
    void addOptionAndInputOptionItemException() {
        ProductOptionDto.Create dto = new ProductOptionDto.Create(
                1L,
                "사이즈",
                ProductOptionType.INPUT,
                1000L,
                List.of(
                        new ProductOptionDto.ItemCreate("230"),
                        new ProductOptionDto.ItemCreate("235"),
                        new ProductOptionDto.ItemCreate("240"),
                        new ProductOptionDto.ItemCreate("245")
                )
        );
        when(productReader.existsProduct(any())).thenReturn(true);
        when(productOptionReader.getProductOptionCount(any())).thenReturn(0L);
        doThrow(new FkProductsException(ErrorType.INVALID_INPUT_OPTION_ITEM, "입력 타입 값에는 아이템을 등록할 수 없습니다."))
                .when(productOptionValidator).validate(any());

        FkProductsException exception = assertThrows(FkProductsException.class, () -> productOptionService.addOption(dto));

        assertThat(exception.getMessage()).isEqualTo("invalid inputOptionItem");
    }

}