package org.example.black_sea_walnut.dto.pages.main.response;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.historyMedia.HistoryMediaResponseForAdd;

import java.util.List;

@Builder
@Getter
public class FactoryBlockResponseForAddInMain {
    private Long mainFactoryId;
    private boolean mainFactoryIsActive;
    private String mainFactoryTitleUk;
    private String mainFactoryTitleEn;
    private String mainFactoryDescriptionUk;
    private String mainFactoryDescriptionEn;
    private List<HistoryMediaResponseForAdd> mainFactoryHistories;
}
