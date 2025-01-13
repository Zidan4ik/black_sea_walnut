package org.example.black_sea_walnut.dto.discount;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.validator.annotation.IsExistDiscountValidation;
import org.example.black_sea_walnut.validator.annotation.NumberNullValidation;

@Builder
@Getter
@IsExistDiscountValidation
public class DiscountRequestForAdd {
    private Long id;
    @NumberNullValidation(message = "The discount id should be present!")
    @Min(value = 0, message = "{error.field.valid.min.value}")
    private Long discountId;
    @NotBlank(message = "{error.field.empty}")
    private String nameUk2;
    @NotBlank(message = "{error.field.empty}")
    private String nameEn2;
    @Min(value = 0, message = "{error.field.valid.min.value}")
    private Long value;
}
