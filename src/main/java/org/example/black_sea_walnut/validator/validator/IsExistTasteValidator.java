package org.example.black_sea_walnut.validator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.taste.TasteRequestForAdd;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.TasteService;
import org.example.black_sea_walnut.validator.annotation.IsExistTasteValidation;

import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public class IsExistTasteValidator implements ConstraintValidator<IsExistTasteValidation, TasteRequestForAdd> {
    private final TasteService tasteService;

    @Override
    public boolean isValid(TasteRequestForAdd dto, ConstraintValidatorContext context) {
        if (dto.getTasteIdUk() != null && dto.getTasteIdEn() != null) {
            boolean existByIdUk = tasteService.isExistById(dto.getTasteIdUk());
            boolean existByIdEn = tasteService.isExistById(dto.getTasteIdEn());
            if (existByIdUk && existByIdEn) {
                Set<Taste> tastes = tasteService.getAllByCommonId(dto.getCommonId());
                boolean f = true;
                for (Taste t : tastes) {
                    if (t.getLanguageCode().equals(LanguageCode.uk)) {
                        if (!Objects.equals(dto.getTasteIdUk(), t.getId())) {
                            f = false;
                        }
                    }
                    else if (t.getLanguageCode().equals(LanguageCode.en)) {
                        if (!Objects.equals(dto.getTasteIdEn(), t.getId())) {
                            f = false;
                        }
                    }
                    return f;
                }
            }
        }
        if (tasteService.isExistByCommonId(dto.getCommonId())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("commonId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
