package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.manager.ManagerDTO;
import org.example.black_sea_walnut.dto.manager.ManagerResponseForView;
import org.example.black_sea_walnut.entity.Manager;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.domain.Pageable;

public interface ManagerService {
    Manager save(Manager entity);

    Manager save(ManagerDTO dto);

    Manager getById(Long id);

    ManagerDTO getByIdInResponseForAdd(Long id);

    void deleteById(Long id);

    PageResponse<ManagerResponseForView> getAll(ManagerResponseForView response, Pageable pageable, LanguageCode code);
}
