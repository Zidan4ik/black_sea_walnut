package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.ResponseDiscountForView;
import org.example.black_sea_walnut.dto.ResponseDiscountsForProduct;
import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.enums.LanguageCode;

import java.util.List;

public class DiscountMapper {
    public ResponseDiscountForView toDTOForView(Discount entity) {
        return ResponseDiscountForView
                .builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public ResponseDiscountsForProduct toDTOForProduct(List<Discount> discounts) {
        Discount nameUk = discounts.stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Discount with language code 'uk' not found"));
        Discount nameEn = discounts.stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Discount with language code 'en' not found"));
        Long id = nameUk.getId().equals(nameUk.getDiscountId()) ? nameUk.getId() : null;
        Integer price = nameUk.getValue() == nameEn.getValue() ? nameUk.getValue() : null;
        return ResponseDiscountsForProduct
                .builder()
                .commonId(id)
                .price(price)
                .nameUk(nameUk.getName())
                .nameEn(nameEn.getName())
                .build();
    }
}
