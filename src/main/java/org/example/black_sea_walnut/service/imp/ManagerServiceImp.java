package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.manager.ManagerDTO;
import org.example.black_sea_walnut.dto.admin.manager.ManagerResponseForView;
import org.example.black_sea_walnut.entity.Manager;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.ManagerMapper;
import org.example.black_sea_walnut.repository.ManagerRepository;
import org.example.black_sea_walnut.service.ManagerService;
import org.example.black_sea_walnut.service.specifications.ManagerSpecification;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerServiceImp implements ManagerService {
    private final ManagerRepository managerRepository;
    private final ManagerMapper mapper;

    @Override
    public Manager save(Manager entity) {
        LogUtil.logInfo("Saving Manager entity: " + entity);
        return managerRepository.save(entity);
    }

    @Override
    public Manager save(ManagerDTO dto) {
        LogUtil.logInfo("Saving ManagerDTO: " + dto);
        return save(mapper.toEntityFromRequest(dto));
    }

    @Override
    public Manager getById(Long id) {
        LogUtil.logInfo("Fetching Manager by ID: " + id);
        return managerRepository.findById(id)
                .orElseThrow(() -> {
                    LogUtil.logError("Manager with id: " + id + " was not found!", null);
                    return new EntityNotFoundException("Manager with id:" + id + " was not found!");
                });
    }

    @Override
    public ManagerDTO getByIdInResponseForAdd(Long id) {
        LogUtil.logInfo("Fetching ManagerDTO for add by ID: " + id);
        return mapper.toResponseForDTO(getById(id));
    }

    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting Manager by ID: " + id);
        managerRepository.deleteById(id);
    }

    @Override
    public PageResponse<ManagerResponseForView> getAll(ManagerResponseForView response, Pageable pageable, LanguageCode code) {
        LogUtil.logInfo("Fetching all Managers with filters: " + response);
        Page<Manager> page = managerRepository.findAll(ManagerSpecification.getSpecification(response), pageable);
        List<ManagerResponseForView> responsesDtoAdd = page.map(m -> mapper.toResponseForView(m, code)).stream().toList();
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }
}
