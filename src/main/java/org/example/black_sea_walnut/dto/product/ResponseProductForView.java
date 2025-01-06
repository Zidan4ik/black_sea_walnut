package org.example.black_sea_walnut.dto.product;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.enums.LanguageCode;

@Builder
@Getter
public class ResponseProductForView {
    private Long id;
    private LanguageCode languageCode;
    private String name;
    private String taste;
    private String totalCount;
    private String priceByUnit;
    private String discountType;
}
