package com.hyuuny.fkproducts.productoptions.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "option_items")
@Entity
public class OptionItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = LAZY)
    private ProductOptionEntity productOption;

    private String name;

    public void setProductOption(ProductOptionEntity productOption) {
        this.productOption = productOption;
        this.productOption.getItems().add(this);
    }
}
