package org.example.black_sea_walnut.validator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.taste.TasteRequestForAdd;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.service.TasteService;
import org.example.black_sea_walnut.validator.annotation.IsExistTasteValidation;

import java.util.Objects;

@RequiredArgsConstructor
public class IsExistTasteValidator implements ConstraintValidator<IsExistTasteValidation, TasteRequestForAdd> {
    private final TasteService tasteService;

    @Override
    public boolean isValid(TasteRequestForAdd dto, ConstraintValidatorContext context) {
        if (dto.getId() != null) {
            boolean existById = tasteService.isExistByTasteId(dto.getId());
            if (existById) {
                Taste taste = tasteService.getById(dto.getId());
                if (Objects.equals(taste.getTasteId(), dto.getTasteId())) {
                    return true;
                }
            }
        }
        boolean existByTasteId = tasteService.isExistByTasteId(dto.getTasteId());
        if (existByTasteId) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("tasteId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
