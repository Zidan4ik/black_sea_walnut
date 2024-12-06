package org.example.black_sea_walnut.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.enums.LanguageCode;

@Builder
@Getter
public class ResponseNewForView {
    private Long id;
    private LanguageCode code;
    private String title;
    private String description;
    private String date;
    private String time;
    private Boolean isActive;
}
