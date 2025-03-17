package org.example.black_sea_walnut.dto.admin.product;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.admin.discount.DiscountResponseForView;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForView;

import java.util.Set;

@Builder
@Getter
public class ResponseAllDiscountsAndTastes {
    private Set<DiscountResponseForView> discountsUk;
    private Set<DiscountResponseForView> discountsEn;
    private Set<TasteResponseForView> tastesUk;
    private Set<TasteResponseForView> tastesEn;
}
