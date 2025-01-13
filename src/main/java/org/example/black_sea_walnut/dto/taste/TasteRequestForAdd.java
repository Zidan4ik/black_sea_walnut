package org.example.black_sea_walnut.dto.taste;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.validator.annotation.IsExistTasteValidation;
import org.example.black_sea_walnut.validator.annotation.NumberNullValidation;

@Builder
@Getter
@IsExistTasteValidation
public class TasteRequestForAdd {
    private Long id;
    @NumberNullValidation(message = "The taste id should be present!")
    @Min(value = 0, message = "{error.field.valid.min.value}")
    private Long tasteId;
    @NotBlank(message = "{error.field.empty}")
    private String nameUk;
    @NotBlank(message = "{error.field.empty}")
    private String nameEn;
}
