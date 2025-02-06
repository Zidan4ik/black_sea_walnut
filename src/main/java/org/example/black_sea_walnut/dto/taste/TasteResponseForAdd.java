package org.example.black_sea_walnut.dto.taste;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class TasteResponseForAdd {
    private Long tasteIdUk;
    private Long tasteIdEn;
    private Long commonId;
    @Setter
    private String tasteNameUk;
    @Setter
    private String tasteNameEn;
}
