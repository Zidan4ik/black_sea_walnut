package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.discount.ResponseDiscountForView;
import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.enums.LanguageCode;

import java.util.List;
import java.util.Set;

public interface DiscountService {
    List<Discount> getAll();
    Set<ResponseDiscountForView> getAllByLanguageCodeInDTO(LanguageCode code);
    String getSentence(Set<ResponseDiscountForView> discounts);
//    ResponseDiscountsForProduct getByDiscountIdInDTO(Long id);
    Set<Discount> getAllByDiscountId(Long id);
}
