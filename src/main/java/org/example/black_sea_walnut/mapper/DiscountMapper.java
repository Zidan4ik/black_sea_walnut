package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.discount.DiscountRequestForAdd;
import org.example.black_sea_walnut.dto.discount.DiscountResponseForAdd;
import org.example.black_sea_walnut.dto.discount.DiscountResponseForView;
import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.enums.LanguageCode;

import java.util.List;
import java.util.Set;


public class DiscountMapper {
    public DiscountResponseForView toDTOForView(Discount entity) {
        return DiscountResponseForView
                .builder()
                .id(entity.getDiscountCommonId())
                .name(entity.getName())
                .value((long) entity.getValue())
                .build();
    }
    public List<Discount> toEntityFromRequest(DiscountRequestForAdd dto) {
        Discount discountUk = new Discount();
        discountUk.setId(dto.getDiscountIdUk());
        discountUk.setDiscountCommonId(dto.getDiscountCommonId());
        discountUk.setLanguageCode(LanguageCode.uk);
        discountUk.setName(dto.getNameUk2());
        discountUk.setValue(Math.toIntExact(dto.getValue()));

        Discount discountEn = new Discount();
        discountEn.setId(dto.getDiscountIdEn());
        discountEn.setDiscountCommonId(dto.getDiscountCommonId());
        discountEn.setLanguageCode(LanguageCode.en);
        discountEn.setName(dto.getNameEn2());
        discountEn.setValue(Math.toIntExact(dto.getValue()));
        return List.of(discountUk, discountEn);
    }

    public DiscountResponseForAdd toResponseForAdd(Set<Discount> entities) {
        Discount tasteUk = entities.stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        Discount tasteEn = entities.stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);
        if (tasteUk != null && tasteEn != null) {
            return DiscountResponseForAdd
                    .builder()
                    .discountIdUk(tasteUk.getId())
                    .discountIdEn(tasteEn.getId())
                    .discountCommonId(tasteUk.getDiscountCommonId())
                    .nameUk2(tasteUk.getName())
                    .nameEn2(tasteEn.getName())
                    .value((long) tasteUk.getValue())
                    .build();
        }
        return null;
    }
}
