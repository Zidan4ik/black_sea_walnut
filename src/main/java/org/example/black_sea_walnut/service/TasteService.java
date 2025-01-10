package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.taste.ResponseTasteForView;
import org.example.black_sea_walnut.entity.Taste;
import org.example.black_sea_walnut.enums.LanguageCode;

import java.util.List;
import java.util.Set;

public interface TasteService {
    List<Taste> getAllByLanguageCodeInDTO();
    Set<ResponseTasteForView> getAllByLanguageCodeInDTO(LanguageCode code);
    String getSentence(Set<ResponseTasteForView> tastes);
//    ResponseTastesForProduct getByTasteIdInDTO(Long id);
    Set<Taste> getAllByTasteId(Long id);
}
