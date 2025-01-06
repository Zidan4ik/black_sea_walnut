package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.enums.LanguageCode;

import java.util.List;

@Entity
@Table(name = "discounts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"discountId", "languageCode"}))
@Getter
@Setter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long discountId;
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;
    private String name;
    private int value;
    @ManyToMany(mappedBy = "discounts")
    private List<Product> products;
}
