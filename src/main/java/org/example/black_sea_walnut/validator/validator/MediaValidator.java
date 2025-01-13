package org.example.black_sea_walnut.validator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class MediaValidator implements ConstraintValidator<MediaValidation, MultipartFile> {
    public List<String> allowTypes;

    @Override
    public void initialize(MediaValidation constraintAnnotation) {
        this.allowTypes = List.of(constraintAnnotation.allowedTypes());
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null) return true;
        if (!file.isEmpty()) {
            return allowTypes.contains(file.getContentType());
        }
        return true;
    }
}
