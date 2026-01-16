package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.discount.DiscountRequestForAdd;
import org.example.black_sea_walnut.dto.admin.discount.DiscountResponseForAdd;
import org.example.black_sea_walnut.dto.admin.discount.DiscountResponseForView;
import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.DiscountMapper;
import org.example.black_sea_walnut.repository.DiscountRepository;
import org.example.black_sea_walnut.service.DiscountService;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImp implements DiscountService {
    private final DiscountRepository discountRepository;
    private final DiscountMapper mapper = new DiscountMapper();

    @Override
    public List<Discount> getAll() {
        LogUtil.logInfo("Fetching all discounts.");
        List<Discount> discounts = discountRepository.findAll();
        LogUtil.logInfo("Fetched " + discounts.size() + " discounts.");
        return discounts;
    }

    @Override
    public Set<DiscountResponseForView> getAllByLanguageCodeInDTO(LanguageCode code) {
        LogUtil.logInfo("Fetching discounts by language code: " + code);
        Set<Discount> discountSet = discountRepository.getAllByLanguageCode(code);
        Set<DiscountResponseForView> response = discountSet.stream()
                .map(mapper::toDTOForView)
                .collect(Collectors.toSet());
        LogUtil.logInfo("Fetched " + response.size() + " discounts for language code: " + code);
        return response;
    }

    @Override
    public String getSentence(Set<DiscountResponseForView> tastes) {
        LogUtil.logInfo("Generating sentence from discount responses.");
        String sentence = tastes.stream()
                .map(DiscountResponseForView::getName)
                .collect(Collectors.joining(", "));
        LogUtil.logInfo("Generated sentence: " + sentence);
        return sentence;
    }

    @Override
    public Set<Discount> getAllByDiscountCommonId(Long id) {
        LogUtil.logInfo("Fetching discounts by common ID: " + id);
        Set<Discount> discounts = discountRepository.getAllByDiscountCommonId(id);
        LogUtil.logInfo("Fetched " + discounts.size() + " discounts for common ID: " + id);
        return discounts;
    }

    @Override
    public boolean isExistByDiscountCommonId(Long discountId) {
        LogUtil.logInfo("Checking if discount with common ID: " + discountId + " exists.");
        boolean exists = discountRepository.existsByDiscountCommonId(discountId);
        LogUtil.logInfo("Discount with common ID: " + discountId + " exists: " + exists);
        return exists;
    }

    @Override
    public boolean isExistById(Long id) {
        LogUtil.logInfo("Checking if discount with ID: " + id + " exists.");
        boolean exists = discountRepository.existsById(id);
        LogUtil.logInfo("Discount with ID: " + id + " exists: " + exists);
        return exists;
    }

    @Override
    public Discount getById(Long id) {
        LogUtil.logInfo("Fetching discount with ID: " + id);
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> {
                    LogUtil.logError("Discount with ID: " + id + " was not found!", null);
                    return new EntityNotFoundException("Discount with id: " + id + " was not found!");
                });
        LogUtil.logInfo("Found discount with ID: " + id);
        return discount;
    }

    @Override
    public DiscountResponseForAdd getByIdInResponseForAdd(Long id) {
        LogUtil.logInfo("Fetching discount response for add with ID: " + id);
        DiscountResponseForAdd response = mapper.toResponseForAdd(discountRepository.getAllByDiscountCommonId(id));
        LogUtil.logInfo("Fetched discount response for add with ID: " + id);
        return response;
    }

    @Override
    public DiscountResponseForAdd getByIdAndLanguageInResponseForAdd(Long id, LanguageCode code) {
        LogUtil.logInfo("Fetching discount response for add with ID: " + id + " and language code: " + code);
        DiscountResponseForAdd response = mapper.toResponseForAdd(discountRepository.getAllByDiscountCommonIdAndLanguageCode(id, code));
        LogUtil.logInfo("Fetched discount response for add with ID: " + id + " and language code: " + code);
        return response;
    }

    @Override
    public void save(Discount discount) {
        LogUtil.logInfo("Saving discount: " + discount);
        discountRepository.save(discount);
        LogUtil.logInfo("Saved discount: " + discount);
    }

    @Override
    public void save(DiscountRequestForAdd dto) {
        LogUtil.logInfo("Saving discounts from DTO: " + dto);
        List<Discount> list = mapper.toEntityFromRequest(dto);
        for (Discount discount : list) {
            save(discount);
        }
        LogUtil.logInfo("Saved discounts from DTO: " + dto);
    }

    @Override
    public void saveAll(List<Discount> discounts) {
        LogUtil.logInfo("Saving discounts from DTO: " + discounts);
        discountRepository.saveAll(discounts);
        LogUtil.logInfo("Saved discounts from DTO: " + discounts);
    }

    @Override
    @Transactional
    public void deleteCommonById(Long id) {
        LogUtil.logInfo("Deleting all discounts with common ID: " + id);
        discountRepository.deleteAllByDiscountCommonId(id);
        LogUtil.logInfo("Deleted all discounts with common ID: " + id);
    }

    @Override
    public Map<Long, List<Discount>> findAllGroupedByCommonId() {
        LogUtil.logInfo("Fetching all discounts by common ids.");
        Map<Long, List<Discount>> discounts = discountRepository.findAllGroupedByCommonId();
        LogUtil.logInfo("Fetched " + discounts.size() + " discounts.");
        return discounts;
    }
}
