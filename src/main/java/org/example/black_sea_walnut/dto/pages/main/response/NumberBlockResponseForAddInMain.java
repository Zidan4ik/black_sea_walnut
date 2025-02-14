package org.example.black_sea_walnut.dto.pages.main.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NumberBlockResponseForAddInMain {
    private Long mainNumberId;
    private boolean mainNumberIsActive;
    private String mainNumberTitle1;
    private String mainNumberTitle2;
    private String mainNumberTitle3;
    private String mainNumberTitle4;
    private String mainNumberDescriptionUk1;
    private String mainNumberDescriptionUk2;
    private String mainNumberDescriptionUk3;
    private String mainNumberDescriptionUk4;
    private String mainNumberDescriptionEn1;
    private String mainNumberDescriptionEn2;
    private String mainNumberDescriptionEn3;
    private String mainNumberDescriptionEn4;
}
