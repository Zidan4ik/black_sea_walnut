package org.example.black_sea_walnut.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.black_sea_walnut.enums.LanguageCode;

import java.util.Objects;


@Entity
@Table(name = "discounts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"discountCommonId", "languageCode"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long discountCommonId;
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;
    private String name;
    private int value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discount discount = (Discount) o;
        return value == discount.value && Objects.equals(id, discount.id) && Objects.equals(discountCommonId, discount.discountCommonId) && languageCode == discount.languageCode && Objects.equals(name, discount.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, discountCommonId, languageCode, name, value);
    }
}
