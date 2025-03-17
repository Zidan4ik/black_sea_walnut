package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.taste.TasteRequestForAdd;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForAdd;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForView;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.TasteMapper;
import org.example.black_sea_walnut.repository.TasteRepository;
import org.example.black_sea_walnut.service.TasteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashSet;
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
    public Set<TasteResponseForView> getAllByLanguageCodeInDTO(LanguageCode code) {
        Set<Taste> tastesSet = tasteRepository.findAllByLanguageCode(code);
        return tastesSet.stream().map(mapper::toDTOForView).
                sorted(Comparator.comparing(TasteResponseForView::getId)).
                collect(Collectors.toCollection(LinkedHashSet::new));

    }

    @Override
    public String getSentence(Set<TasteResponseForView> tastes) {
        return tastes.stream()
                .map(TasteResponseForView::getName)
                .collect(Collectors.joining(", "));
    }

    @Override
    public Set<Taste> getAllByCommonId(Long id) {
        return tasteRepository.findAllByCommonId(id);
    }

    @Override
    public Taste save(Taste taste) {
        return tasteRepository.save(taste);
    }

    @Override
    public void save(TasteRequestForAdd dto) {
        List<Taste> list = mapper.toEntityFromRequest(dto);
        for (Taste t : list) {
            save(t);
        }
    }

    @Override
    public boolean isExistByCommonId(Long tasteId) {
        return tasteRepository.existsByCommonId(tasteId);
    }

    @Override
    public boolean isExistById(Long id) {
        return tasteRepository.existsById(id);
    }

    @Override
    public Taste getById(Long id) {
        return tasteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Taste with id: " + id + " was not found!"));
    }

    @Override
    public TasteResponseForAdd getByIdInResponseForAdd(Long id) {
        return mapper.toResponseForAdd(tasteRepository.findAllByCommonId(id));
    }

    @Override
    @Transactional
    public void deleteByCommonId(Long id) {
        tasteRepository.deleteAllByCommonId(id);
    }
}
