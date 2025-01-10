package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.discount.ResponseDiscountForView;
import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.DiscountMapper;
import org.example.black_sea_walnut.repository.DiscountRepository;
import org.example.black_sea_walnut.service.DiscountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountServiceImp implements DiscountService {
    private final DiscountRepository discountRepository;
    private final DiscountMapper mapper = new DiscountMapper();

    @Override
    public List<Discount> getAll() {
        return discountRepository.findAll();
    }

    @Override
    public Set<ResponseDiscountForView> getAllByLanguageCodeInDTO(LanguageCode code) {
        Set<Discount> discountSet = discountRepository.getAllByLanguageCode(code);
        return discountSet.stream().map(mapper::toDTOForView).collect(Collectors.toSet());
    }

    @Override
    public String getSentence(Set<ResponseDiscountForView> tastes) {
        return tastes.stream()
                .map(ResponseDiscountForView::getName)
                .collect(Collectors.joining(", "));
    }

//    @Override
//    public ResponseDiscountsForProduct getByDiscountIdInDTO(Long id) {
//        return mapper.toDTOForProduct(getAllByDiscountId(id));
//    }

    @Override
    public Set<Discount> getAllByDiscountId(Long id) {
        return discountRepository.getAllByDiscountId(id);
    }
}
