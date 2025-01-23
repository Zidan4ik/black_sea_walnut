package org.example.black_sea_walnut.dto.pages.catalog.response;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.historyMedia.HistoryMediaResponseForAdd;

import java.util.List;

@Builder
@Getter
public class EcologicallyBlockResponseForAdd {
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
