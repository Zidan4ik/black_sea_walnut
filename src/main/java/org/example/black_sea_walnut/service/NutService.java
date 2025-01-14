package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.nut.NutRequestForAdd;
import org.example.black_sea_walnut.dto.nut.NutResponseForAdd;
import org.example.black_sea_walnut.dto.nut.NutResponseForView;
import org.example.black_sea_walnut.entity.Nut;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NutService {
    Nut getById(Long id);
    NutResponseForAdd getByIdInResponseDtoAdd(Long id);
    Nut save(Nut entity);
    Nut save(NutRequestForAdd dto);
    List<Nut> getAll();
    PageResponse<NutResponseForView> getAll(NutResponseForView response, Pageable pageable, LanguageCode code);
    void deleteById(Long id);
}
