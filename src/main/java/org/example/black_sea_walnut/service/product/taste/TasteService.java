package org.example.black_sea_walnut.service.product.taste;

import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForAdd;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.Nameable;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.user.Saveable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public interface TasteService {
    List<Taste> getAllByLanguageCode();

    <R> Set<R> getAllByLanguageCodeInDTO(LanguageCode code, Function<Taste, R> mappingFunction);

    <R extends Nameable> String getSentence(Set<R> tastes);

    Set<Taste> getAllByCommonId(Long id);

    Taste save(Taste taste);

    <M extends GenericsMapper> void save(Saveable<Taste, M> dto, M mapper);

    boolean isExistByCommonId(Long tasteId);

    boolean isExistById(Long tasteId);

    Taste getById(Long id);

    TasteResponseForAdd getByIdInResponseForAdd(Long id);

    void deleteByCommonId(Long id);

    List<Taste> getAll();

    void saveAll(List<Taste> tastes);

    Map<Long, List<Taste>> getAllGroupedByCommonId();

    void deleteProductLinksByTasteCommonId(Long tasteId);
}
