package org.example.black_sea_walnut.dto.product;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.discount.ResponseDiscountForView;
import org.example.black_sea_walnut.dto.taste.ResponseTasteForView;

import java.util.Set;

@Builder
@Getter
public class ResponseAllDiscountsAndTastes {
    private Set<ResponseDiscountForView> discountsUk;
    private Set<ResponseDiscountForView> discountsEn;
    private Set<ResponseTasteForView> tastesUk;
    private Set<ResponseTasteForView> tastesEn;
}
