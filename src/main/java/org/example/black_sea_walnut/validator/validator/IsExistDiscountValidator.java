package org.example.black_sea_walnut.validator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.discount.DiscountRequestForAdd;
import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.DiscountService;
import org.example.black_sea_walnut.validator.annotation.IsExistDiscountValidation;


import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public class IsExistDiscountValidator implements ConstraintValidator<IsExistDiscountValidation, DiscountRequestForAdd> {
    private final DiscountService discountService;

    @Override
    public boolean isValid(DiscountRequestForAdd dto, ConstraintValidatorContext context) {
        if (dto.getDiscountIdUk() != null && dto.getDiscountIdEn() != null) {
            boolean existByIdUk = discountService.isExistById(dto.getDiscountIdUk());
            boolean existByIdEn = discountService.isExistById(dto.getDiscountIdEn());
            if (existByIdUk && existByIdEn) {
                Set<Discount> discounts = discountService.getAllByDiscountCommonId(dto.getDiscountCommonId());
                for (Discount d : discounts) {
                    boolean f = true;
                    if (d.getLanguageCode().equals(LanguageCode.uk)) {
                        if (!Objects.equals(dto.getDiscountIdUk(), d.getId())) {
                            f = false;
                        }
                    } else if (d.getLanguageCode().equals(LanguageCode.en)) {
                        if (!Objects.equals(dto.getDiscountIdEn(), d.getId())) {
                            f = false;
                        }
                    }
                    return f;
                }
            }
        }
        boolean existByTasteId = discountService.isExistByDiscountCommonId(dto.getDiscountCommonId());
        if (existByTasteId) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("discountCommonId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
