package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.enums.LanguageCode;


@Entity
@Table(name = "discounts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"discountCommonId", "languageCode"}))
@Getter
@Setter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long discountCommonId;
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;
    private String name;
    private int value;
}
