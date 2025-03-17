package org.example.black_sea_walnut.dto.admin.pages.main.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
public class ProductionBlockRequestForAdd {
    private Long mainProductionId;
    private boolean mainProductionIsActive;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String mainProductionTitleUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String mainProductionTitleEn;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String mainProductionDescriptionUk;
    @Size(max = 150, message = "{error.field.valid.size}")
    private String mainProductionDescriptionEn;
}
