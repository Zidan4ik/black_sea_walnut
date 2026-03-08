package org.example.black_sea_walnut.dto.admin.pages.factory.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.mapper.pages.HistoryFactoryMapper;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.history.HistoryFileRequest;
import org.example.black_sea_walnut.service.user.Saveable;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class BlockRequestForAdd implements Saveable<History, HistoryFactoryMapper>, Uploadable, HistoryFileRequest {
    private Long factoryBlockId;
    private Boolean factoryBlockIsActive;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String factoryBlockTitleUk;
    @NotBlank(message = "{error.field.empty}")
    @Length(max = 100,message = "{error.field.valid.length.title}")
    private String factoryBlockTitleEn;
    @Size(max = 3500, message = "{error.field.valid.size}")
    private String factoryBlockDescriptionUk;
    @Size(max = 3500, message = "{error.field.valid.size}")
    private String factoryBlockDescriptionEn;
    @Valid
    private List<HistoryMediaRequestForAdd> factoryBlockFiles;

    @Override
    public void updateEntity(History entity, HistoryFactoryMapper mapper) {
        mapper.toEntityFromRequestFactoryBlock(this,entity);
    }

    @Override
    public String getSubFolder() {
        return "pages/factory/2blocks";
    }

    @Override
    public List<HistoryMediaRequestForAdd> getFiles() {
        return this.getFactoryBlockFiles();
    }

    @Override
    public Long getId() {
        return this.getFactoryBlockId();
    }
}
