package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.enums.DiscountType;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private DiscountType discountType;
    private int value;
}
