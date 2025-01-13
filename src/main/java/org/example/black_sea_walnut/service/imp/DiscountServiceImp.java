package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.discount.DiscountRequestForAdd;
import org.example.black_sea_walnut.dto.discount.DiscountResponseForAdd;
import org.example.black_sea_walnut.dto.discount.DiscountResponseForView;
import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.entity.Taste;
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
    public Set<DiscountResponseForView> getAllByLanguageCodeInDTO(LanguageCode code) {
        Set<Discount> discountSet = discountRepository.getAllByLanguageCode(code);
        return discountSet.stream().map(mapper::toDTOForView).collect(Collectors.toSet());
    }

    @Override
    public String getSentence(Set<DiscountResponseForView> tastes) {
        return tastes.stream()
                .map(DiscountResponseForView::getName)
                .collect(Collectors.joining(", "));
    }

    @Override
    public Set<Discount> getAllByDiscountId(Long id) {
        return discountRepository.getAllByDiscountId(id);
    }

    @Override
    public boolean isExistByDiscountId(Long discountId) {
        return discountRepository.existsByDiscountId(discountId);
    }

    @Override
    public Discount getById(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Discount with id: " + id + " was not found!"));
    }

    @Override
    public DiscountResponseForAdd getByIdInResponseForAdd(Long id) {
        return mapper.toResponseForAdd(discountRepository.getAllByDiscountId(id));
    }

    @Override
    public void save(Discount discount) {
        discountRepository.save(discount);
    }

    @Override
    public void save(DiscountRequestForAdd dto) {
        List<Discount> list = mapper.toEntityFromRequest(dto);
        for (Discount t: list){
            save(t);
        }
    }
}
