package org.example.black_sea_walnut.dto.admin.pages.catalog.response;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaResponseForAdd;
import org.example.black_sea_walnut.service.history.HistoryResponse;

import java.util.List;

@Builder
@Getter
public class EcologicallyBlockResponseForAdd implements HistoryResponse {
    private Long catalogEcologicallyId;
    private Boolean catalogEcologicallyIsActive;
    private String catalogEcologicallyTitleUk;
    private String catalogEcologicallyTitleEn;
    private String catalogEcologicallySubtitleUk;
    private String catalogEcologicallySubtitleEn;
    private String catalogEcologicallyDescriptionUk;
    private String catalogEcologicallyDescriptionEn;
    private List<HistoryMediaResponseForAdd> catalogEcologicallyFiles;
}
