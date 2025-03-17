package org.example.black_sea_walnut.dto.admin.historyMedia;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.enums.MediaType;

@Builder
@Getter
public class HistoryMediaResponseForAdd {
    private Long id;
    private MediaType mediaType;
    private String pathToImage;
}
