package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseNewForViewInWeb {
    private Long id;
    private Boolean isActive;
    private String title;
    private String description;
    private String date;
    private String pathToImage;
}
