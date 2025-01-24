package org.example.black_sea_walnut.dto.pages.factory.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.black_sea_walnut.dto.historyMedia.HistoryMediaRequestForAdd;

import java.util.List;

@Data
public class BlockRequestForAdd {
    private Long factoryBlockId;
    private Boolean factoryBlockIsActive;
    @NotBlank(message = "{error.field.empty}")
    private String factoryBlockTitleUk;
    @NotBlank(message = "{error.field.empty}")
    private String factoryBlockTitleEn;
    @Size(max = 3500, message = "{error.field.valid.size}")
    private String factoryBlockDescriptionUk;
    @Size(max = 3500, message = "{error.field.valid.size}")
    private String factoryBlockDescriptionEn;
    @Valid
    private List<HistoryMediaRequestForAdd> factoryBlockFiles;
}
