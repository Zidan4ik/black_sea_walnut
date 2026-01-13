package org.example.black_sea_walnut.dto.admin.discount;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.validator.annotation.IsExistDiscountValidation;
import org.example.black_sea_walnut.validator.annotation.NumberNullValidation;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@IsExistDiscountValidation
public class DiscountRequestForAdd {
    private Long discountIdUk; // no delete, is important
    private Long discountIdEn; // no delete, is important
    @NumberNullValidation(message ="{error.field.empty.number}")
    @Min(value = 0, message = "{error.field.valid.min.value}")
    private Long discountCommonId;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String nameUk2;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String nameEn2;
    @Min(value = 0, message = "{error.field.valid.min.value}")
    @NumberNullValidation(message ="{error.field.empty.number}")
    private Long value;
}
