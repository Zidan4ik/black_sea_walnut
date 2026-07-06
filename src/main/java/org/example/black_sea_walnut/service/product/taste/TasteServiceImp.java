package org.example.black_sea_walnut.service.product.taste;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForAdd;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForView;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.TasteMapper;
import org.example.black_sea_walnut.repository.TasteRepository;
import org.example.black_sea_walnut.service.Nameable;
import org.example.black_sea_walnut.service.history.DtoResponse;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TasteServiceImp implements TasteService {
    private final TasteRepository tasteRepository;
    private final TasteMapper mapper = new TasteMapper();

    public List<Taste> getAllByLanguageCode() {
        LogUtil.logInfo("Fetching all tastes");
        return tasteRepository.findAll();
    }

    @Override
    public <R> Set<R> getAllByLanguageCodeInDTO(LanguageCode code, Function<Taste,R> mappingFunction) {
        LogUtil.logInfo("Fetching tastes by language code: " + code);
        Set<Taste> tastesSet = tasteRepository.findAllByLanguageCode(code);

        return tastesSet.stream()
                .map(mappingFunction)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public <R extends Nameable> String getSentence(Set<R> tastes) {
        LogUtil.logInfo("Generating sentence from taste names");
        return tastes.stream()
                .map(Nameable::getName)
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
    public <M extends GenericsMapper> void save(Saveable<Taste, M> dto, M mapper) {
        LogUtil.logInfo("Saving new tastes from DTO: " + dto);
        List<Taste> list = dto.updateAndGetList(mapper);
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
        deleteProductLinksByTasteCommonId(id);
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

    @Override
    public void deleteProductLinksByTasteCommonId(Long tasteId) {
        tasteRepository.deleteProductLinksByTasteCommonId(tasteId);
    }
}
