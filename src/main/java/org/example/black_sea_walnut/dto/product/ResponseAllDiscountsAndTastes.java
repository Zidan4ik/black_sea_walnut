package org.example.black_sea_walnut.dto.product;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.discount.DiscountResponseForView;
import org.example.black_sea_walnut.dto.taste.TasteResponseForView;

import java.util.Set;

@Builder
@Getter
public class ResponseAllDiscountsAndTastes {
    private Set<DiscountResponseForView> discountsUk;
    private Set<DiscountResponseForView> discountsEn;
    private Set<TasteResponseForView> tastesUk;
    private Set<TasteResponseForView> tastesEn;
}
