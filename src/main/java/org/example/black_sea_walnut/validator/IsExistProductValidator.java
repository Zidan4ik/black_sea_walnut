package org.example.black_sea_walnut.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.product.ProductRequestForAdd;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.service.ProductService;

import java.util.Objects;

@RequiredArgsConstructor
public class IsExistProductValidator implements ConstraintValidator<IsExistProductValidation, ProductRequestForAdd> {
    private final ProductService productService;

    @Override
    public boolean isValid(ProductRequestForAdd dto, ConstraintValidatorContext context) {
        boolean existByArticleId = productService.isExistByArticleId(dto.getArticleId());
        if (dto.getId() != null) {
            boolean existById = productService.isExistById(dto.getId());
            if (existById) {
                Product product = productService.getById(dto.getId());
                if (Objects.equals(product.getArticleId(), dto.getArticleId())) {
                    return true;
                }
            }
        }
        if (existByArticleId) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("articleId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
