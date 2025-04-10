package org.example.black_sea_walnut.dto.admin.taste;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.validator.annotation.IsExistTasteValidation;
import org.example.black_sea_walnut.validator.annotation.NumberNullValidation;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
@IsExistTasteValidation
public class TasteRequestForAdd {
    private Long tasteIdUk;
    private Long tasteIdEn;
    @NumberNullValidation(message ="{error.field.empty.number}")
    @Min(value = 0, message = "{error.field.valid.min.value}")
    private Long commonId;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String nameUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String nameEn;
}
