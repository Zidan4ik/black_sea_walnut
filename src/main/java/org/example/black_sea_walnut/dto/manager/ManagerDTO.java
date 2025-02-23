package org.example.black_sea_walnut.dto.manager;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.validator.annotation.PhoneFormatValidation;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
public class ManagerDTO {
    private Long id;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String nameUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String nameEn;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String surnameUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String surnameEn;
    @PhoneFormatValidation
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 15,message = "{error.field.phone.size}")
    private String phone;
}
