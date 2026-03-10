package org.example.black_sea_walnut.dto.admin.nut;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.service.history.DtoResponse;

@Builder
@Getter
public class NutResponseForAdd implements DtoResponse {
    private Long id;
    private boolean isActive;
    private String titleUk;
    private String titleEn;
    private String descriptionUk;
    private String descriptionEn;
    private String pathToImage;
    private String pathToSvg;
}
