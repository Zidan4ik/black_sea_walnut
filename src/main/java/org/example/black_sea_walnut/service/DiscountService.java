package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.discount.DiscountRequestForAdd;
import org.example.black_sea_walnut.dto.discount.DiscountResponseForAdd;
import org.example.black_sea_walnut.dto.discount.DiscountResponseForView;
import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.enums.LanguageCode;

import java.util.List;
import java.util.Set;

public interface DiscountService {
    List<Discount> getAll();
    Set<DiscountResponseForView> getAllByLanguageCodeInDTO(LanguageCode code);
    String getSentence(Set<DiscountResponseForView> discounts);
    Set<Discount> getAllByDiscountId(Long id);
    boolean isExistByDiscountId(Long discountId);
    Discount getById(Long id);
    DiscountResponseForAdd getByIdInResponseForAdd(Long id);
    void save(Discount discount);
    void save(DiscountRequestForAdd dto);
}
