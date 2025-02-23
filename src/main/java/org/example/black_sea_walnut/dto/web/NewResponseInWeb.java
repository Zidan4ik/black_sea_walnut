package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NewResponseInWeb {
    private Long id;
    private String date;
    private boolean isActive;
    private String title;
    private String description;
    private String pathToImage;
}
