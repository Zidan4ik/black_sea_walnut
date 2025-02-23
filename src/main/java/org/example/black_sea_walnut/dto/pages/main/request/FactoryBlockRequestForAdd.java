package org.example.black_sea_walnut.dto.pages.main.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.black_sea_walnut.dto.historyMedia.HistoryMediaRequestForAdd;
import org.hibernate.validator.constraints.Length;


import java.util.List;

@Getter
@Setter
public class FactoryBlockRequestForAdd {
    private Long mainFactoryId;
    private Boolean mainFactoryIsActive;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String mainFactoryTitleUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String mainFactoryTitleEn;
    @Size(max = 500, message = "{error.field.valid.size}")
    private String mainFactoryDescriptionUk;
    @Size(max = 500, message = "{error.field.valid.size}")
    private String mainFactoryDescriptionEn;
    @Valid
    private List<HistoryMediaRequestForAdd> files;
}
