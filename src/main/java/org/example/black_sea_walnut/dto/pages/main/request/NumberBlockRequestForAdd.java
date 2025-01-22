package org.example.black_sea_walnut.dto.pages.main.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NumberBlockRequestForAdd {
    private Long mainNumberId;
    private Boolean mainNumberIsActive;
    @NotBlank(message = "{error.field.empty}")
    private String mainNumberTitle1;
    @NotBlank(message = "{error.field.empty}")
    private String mainNumberTitle2;
    @NotBlank(message = "{error.field.empty}")
    private String mainNumberTitle3;
    @NotBlank(message = "{error.field.empty}")
    private String mainNumberTitle4;
    @Size(max = 50, message = "{error.field.valid.size}")
    private String mainNumberDescriptionUk1;
    @Size(max = 50, message = "{error.field.valid.size}")
    private String mainNumberDescriptionUk2;
    @Size(max = 50, message = "{error.field.valid.size}")
    private String mainNumberDescriptionUk3;
    @Size(max = 50, message = "{error.field.valid.size}")
    private String mainNumberDescriptionUk4;
    @Size(max = 50, message = "{error.field.valid.size}")
    private String mainNumberDescriptionEn1;
    @Size(max = 50, message = "{error.field.valid.size}")
    private String mainNumberDescriptionEn2;
    @Size(max = 50, message = "{error.field.valid.size}")
    private String mainNumberDescriptionEn3;
    @Size(max = 50, message = "{error.field.valid.size}")
    private String mainNumberDescriptionEn4;
}
