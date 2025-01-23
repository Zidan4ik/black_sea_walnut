package org.example.black_sea_walnut.dto.pages.catalog.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.black_sea_walnut.dto.historyMedia.HistoryMediaRequestForAdd;

import java.util.List;

@Data
public class EcologicallyBlockRequestForAdd {
    private Long catalogEcologicallyId;
    private Boolean catalogEcologicallyIsActive;
    @NotBlank(message = "{error.field.empty}")
    private String catalogEcologicallyTitleUk;
    @NotBlank(message = "{error.field.empty}")
    private String catalogEcologicallyTitleEn;
    @NotBlank(message = "{error.field.empty}")
    private String catalogEcologicallySubtitleUk;
    @NotBlank(message = "{error.field.empty}")
    private String catalogEcologicallySubtitleEn;
    @Size(max = 3500, message = "{error.field.valid.size}")
    private String catalogEcologicallyDescriptionUk;
    @Size(max = 3500, message = "{error.field.valid.size}")
    private String catalogEcologicallyDescriptionEn;
    @Valid
    private List<HistoryMediaRequestForAdd> catalogEcologicallyFiles;
}
