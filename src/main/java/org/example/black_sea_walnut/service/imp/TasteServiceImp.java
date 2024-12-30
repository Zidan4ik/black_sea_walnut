package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.ResponseTasteForView;
import org.example.black_sea_walnut.dto.ResponseTastesForProduct;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.TasteMapper;
import org.example.black_sea_walnut.repository.TasteRepository;
import org.example.black_sea_walnut.service.TasteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TasteServiceImp implements TasteService {
    private final TasteRepository tasteRepository;
    private final TasteMapper mapper = new TasteMapper();
    @Override
    public List<Taste> getAllByLanguageCodeInDTO() {
        return tasteRepository.findAll();
    }

    @Override
    public Set<ResponseTasteForView> getAllByLanguageCodeInDTO(LanguageCode code) {
        Set<Taste> tastesSet = tasteRepository.findAllByLanguageCode(code);
        return tastesSet.stream().map(mapper::toDTOForView).collect(Collectors.toSet());
    }

    @Override
    public String getSentence(Set<ResponseTasteForView> tastes) {
        return tastes.stream()
                .map(ResponseTasteForView::getName)
                .collect(Collectors.joining(", "));
    }

    @Override
    public ResponseTastesForProduct getByTasteIdInDTO(Long id) {
        List<Taste> all = getAllByTasteId(id);
        return mapper.toDTOForProduct(all);
    }

    @Override
    public List<Taste> getAllByTasteId(Long id) {
        return tasteRepository.findAllByTasteId(id);
    }
}
