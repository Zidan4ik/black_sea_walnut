package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.taste.TasteRequestForAdd;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForAdd;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForView;
import org.example.black_sea_walnut.entity.Discount;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.TasteMapper;
import org.example.black_sea_walnut.repository.TasteRepository;
import org.example.black_sea_walnut.service.TasteService;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TasteServiceImp implements TasteService {
    private final TasteRepository tasteRepository;
    private final TasteMapper mapper = new TasteMapper();

    @Override
    public List<Taste> getAllByLanguageCodeInDTO() {
        LogUtil.logInfo("Fetching all tastes");
        return tasteRepository.findAll();
    }

    @Override
    public Set<TasteResponseForView> getAllByLanguageCodeInDTO(LanguageCode code) {
        LogUtil.logInfo("Fetching tastes by language code: " + code);
        Set<Taste> tastesSet = tasteRepository.findAllByLanguageCode(code);
        return tastesSet.stream()
                .map(mapper::toDTOForView)
                .sorted(Comparator.comparing(TasteResponseForView::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public String getSentence(Set<TasteResponseForView> tastes) {
        LogUtil.logInfo("Generating sentence from taste names");
        return tastes.stream()
                .map(TasteResponseForView::getName)
                .collect(Collectors.joining(", "));
    }

    @Override
    public Set<Taste> getAllByCommonId(Long id) {
        LogUtil.logInfo("Fetching all tastes by common ID: " + id);
        return tasteRepository.findAllByCommonId(id);
    }

    @Override
    public Taste save(Taste taste) {
        LogUtil.logInfo("Saving taste: " + taste);
        return tasteRepository.save(taste);
    }

    @Override
    public void save(TasteRequestForAdd dto) {
        LogUtil.logInfo("Saving new tastes from DTO: " + dto);
        List<Taste> list = mapper.toEntityFromRequest(dto);
        for (Taste t : list) {
            save(t);
        }
    }

    @Override
    public boolean isExistByCommonId(Long tasteId) {
        LogUtil.logInfo("Checking if taste exists by common ID: " + tasteId);
        return tasteRepository.existsByCommonId(tasteId);
    }

    @Override
    public boolean isExistById(Long id) {
        LogUtil.logInfo("Checking if taste exists by ID: " + id);
        return tasteRepository.existsById(id);
    }

    @Override
    public Taste getById(Long id) {
        LogUtil.logInfo("Fetching taste by ID: " + id);
        return tasteRepository.findById(id)
                .orElseThrow(() -> {
                    LogUtil.logError("Taste not found with ID: " + id, null);
                    return new EntityNotFoundException("Taste with id: " + id + " was not found!");
                });
    }

    @Override
    public TasteResponseForAdd getByIdInResponseForAdd(Long id) {
        LogUtil.logInfo("Fetching taste for response by common ID: " + id);
        return mapper.toResponseForAdd(tasteRepository.findAllByCommonId(id));
    }

    @Override
    @Transactional
    public void deleteByCommonId(Long id) {
        LogUtil.logInfo("Deleting all tastes by common ID: " + id);
        tasteRepository.deleteAllByCommonId(id);
    }

    @Override
    public List<Taste> getAll() {
        LogUtil.logInfo("Fetching all tastes: ");
        return tasteRepository.findAll();
    }

    @Override
    public void saveAll(List<Taste> tastes) {
        LogUtil.logInfo("Saving new tastes: " + tastes);
        tasteRepository.saveAll(tastes);
    }

    @Override
    public Map<Long, List<Taste>> getAllGroupedByCommonId() {
        LogUtil.logInfo("Fetching all tastes by common ids.");
        Map<Long, List<Taste>> tastes = tasteRepository.findAllGroupedByCommonId();
        LogUtil.logInfo("Fetched " + tastes.size() + " tastes.");
        return tastes;
    }
}
