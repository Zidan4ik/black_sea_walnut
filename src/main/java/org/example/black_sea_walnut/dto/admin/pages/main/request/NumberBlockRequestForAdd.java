package org.example.black_sea_walnut.dto.admin.pages.main.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
public class NumberBlockRequestForAdd {
    private Long mainNumberId;
    private Boolean mainNumberIsActive;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String mainNumberTitle1;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String mainNumberTitle2;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String mainNumberTitle3;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
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
