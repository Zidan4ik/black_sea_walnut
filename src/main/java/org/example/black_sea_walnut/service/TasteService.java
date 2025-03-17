package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.admin.taste.TasteRequestForAdd;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForAdd;
import org.example.black_sea_walnut.dto.admin.taste.TasteResponseForView;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.enums.LanguageCode;

import java.util.List;
import java.util.Set;

public interface TasteService {
    List<Taste> getAllByLanguageCodeInDTO();

    Set<TasteResponseForView> getAllByLanguageCodeInDTO(LanguageCode code);

    String getSentence(Set<TasteResponseForView> tastes);

    Set<Taste> getAllByCommonId(Long id);

    Taste save(Taste taste);

    void save(TasteRequestForAdd dto);

    boolean isExistByCommonId(Long tasteId);

    boolean isExistById(Long tasteId);

    Taste getById(Long id);

    TasteResponseForAdd getByIdInResponseForAdd(Long id);

    void deleteByCommonId(Long id);
}
