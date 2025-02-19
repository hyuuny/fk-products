package com.hyuuny.fkproducts.products.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "products")
@Entity
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long price;

    private Long shippingFee;

    private String description;

    private LocalDateTime createdAt;

    public void changeName(String name) {
        this.name = name;
    }

    public void changePrice(Long price) {
        this.price = price;
    }

    public void changeShippingFee(Long shippingFee) {
        this.shippingFee = shippingFee;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

}
