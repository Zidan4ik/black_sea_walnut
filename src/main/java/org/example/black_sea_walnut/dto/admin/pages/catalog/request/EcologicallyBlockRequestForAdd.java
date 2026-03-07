package org.example.black_sea_walnut.dto.admin.pages.catalog.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.mapper.pages.HistoryCatalogMapper;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.history.HistoryFileRequest;
import org.example.black_sea_walnut.service.user.Saveable;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class EcologicallyBlockRequestForAdd implements Saveable<History, HistoryCatalogMapper>, Uploadable, HistoryFileRequest {
    private Long catalogEcologicallyId;
    private Boolean catalogEcologicallyIsActive;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String catalogEcologicallyTitleUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String catalogEcologicallyTitleEn;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String catalogEcologicallySubtitleUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String catalogEcologicallySubtitleEn;
    @Size(max = 3500, message = "{error.field.valid.size}")
    private String catalogEcologicallyDescriptionUk;
    @Size(max = 3500, message = "{error.field.valid.size}")
    private String catalogEcologicallyDescriptionEn;
    @Valid
    private List<HistoryMediaRequestForAdd> catalogEcologicallyFiles;


    @Override
    public void updateEntity(History entity, HistoryCatalogMapper mapper) {
        mapper.toEntityFromRequestEcologicallyBlock(this,entity);
    }

    @Override
    public String getSubFolder() {
        return "pages/catalog/ecologically-block";
    }

    @Override
    public List<HistoryMediaRequestForAdd> getFiles() {
        return this.getCatalogEcologicallyFiles();
    }

    @Override
    public Long getId() {
        return this.getCatalogEcologicallyId();
    }
}
