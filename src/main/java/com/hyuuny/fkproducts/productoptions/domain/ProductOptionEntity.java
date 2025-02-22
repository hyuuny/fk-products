package com.hyuuny.fkproducts.productoptions.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "product_options")
@Entity
public class ProductOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private String name;

    private ProductOptionType optionType;

    private Long additionalPrice;

    @Builder.Default
    @OneToMany(mappedBy = "productOption", cascade = ALL, fetch = LAZY, orphanRemoval = true)
    private List<OptionItemEntity> items = new ArrayList<>();

    public void addItem(OptionItemEntity optionItem) {
        optionItem.setProductOption(this);
    }

}
