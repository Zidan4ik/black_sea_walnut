package org.example.black_sea_walnut.dto.admin.nut;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NutResponseForView {
    private Long id;
    private String title;
    private String description;
    private String date;
    private Boolean isActive;
}
