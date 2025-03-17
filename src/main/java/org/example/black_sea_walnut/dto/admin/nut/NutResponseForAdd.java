package org.example.black_sea_walnut.dto.admin.nut;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NutResponseForAdd {
    private Long id;
    private boolean isActive;
    private String titleUk;
    private String titleEn;
    private String descriptionUk;
    private String descriptionEn;
    private String pathToImage;
    private String pathToSvg;
}
