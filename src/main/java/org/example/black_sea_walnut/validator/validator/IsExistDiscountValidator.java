package org.example.black_sea_walnut.validator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.discount.DiscountRequestForAdd;
import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.service.DiscountService;
import org.example.black_sea_walnut.validator.annotation.IsExistDiscountValidation;


import java.util.Objects;

@RequiredArgsConstructor
public class IsExistDiscountValidator implements ConstraintValidator<IsExistDiscountValidation, DiscountRequestForAdd> {
    private final DiscountService discountService;

    @Override
    public boolean isValid(DiscountRequestForAdd dto, ConstraintValidatorContext context) {
        if (dto.getId() != null) {
            boolean existById = discountService.isExistByDiscountId(dto.getId());
            if (existById) {
                Discount discount = discountService.getById(dto.getId());
                if (Objects.equals(discount.getDiscountId(), dto.getDiscountId())) {
                    return true;
                }
            }
        }
        boolean existByTasteId = discountService.isExistByDiscountId(dto.getDiscountId());
        if (existByTasteId) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("discountId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
