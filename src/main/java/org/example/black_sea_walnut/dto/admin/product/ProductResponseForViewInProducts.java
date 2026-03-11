package org.example.black_sea_walnut.dto.admin.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.history.DtoResponse;

@Builder
@Getter
@Setter
public class ProductResponseForViewInProducts implements DtoResponse {
    private Long id;
    private LanguageCode languageCode;
    private String name;
    private String taste;
    private String totalCount;
    private String priceByUnit;
    private String discount;
}
