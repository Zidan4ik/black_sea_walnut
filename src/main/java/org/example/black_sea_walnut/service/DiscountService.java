package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.admin.discount.DiscountRequestForAdd;
import org.example.black_sea_walnut.dto.admin.discount.DiscountResponseForAdd;
import org.example.black_sea_walnut.dto.admin.discount.DiscountResponseForView;
import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.enums.LanguageCode;

import java.util.List;
import java.util.Set;

public interface DiscountService {
    List<Discount> getAll();

    Set<DiscountResponseForView> getAllByLanguageCodeInDTO(LanguageCode code);

    String getSentence(Set<DiscountResponseForView> discounts);

    Set<Discount> getAllByDiscountCommonId(Long id);

    boolean isExistByDiscountCommonId(Long discountId);

    boolean isExistById(Long tasteId);

    Discount getById(Long id);

    DiscountResponseForAdd getByIdInResponseForAdd(Long id);

    DiscountResponseForAdd getByIdAndLanguageInResponseForAdd(Long id, LanguageCode code);

    void save(Discount discount);

    void save(DiscountRequestForAdd dto);

    void deleteCommonById(Long id);
}
