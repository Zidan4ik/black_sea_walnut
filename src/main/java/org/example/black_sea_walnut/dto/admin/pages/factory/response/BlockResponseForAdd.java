package org.example.black_sea_walnut.dto.admin.pages.factory.response;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaResponseForAdd;
import org.example.black_sea_walnut.service.history.HistoryResponse;

import java.util.List;

@Builder
@Getter
public class BlockResponseForAdd implements HistoryResponse {
    private Long factoryBlockId;
    private Boolean factoryBlockIsActive;
    private String factoryBlockTitleUk;
    private String factoryBlockTitleEn;
    private String factoryBlockDescriptionUk;
    private String factoryBlockDescriptionEn;
    private List<HistoryMediaResponseForAdd> factoryBlockFiles;
}