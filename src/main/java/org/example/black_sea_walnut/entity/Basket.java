package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "baskets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int count;
    private int unitPrice;
    private int discountUnitPrice;
    private int discountPercent;
    private int discountSumForUnit;
    private int summaWithoutDiscount;
    private int summaDiscount;
    private int summaWithDiscount;
    @ManyToMany
    private List<Product> products;
}
