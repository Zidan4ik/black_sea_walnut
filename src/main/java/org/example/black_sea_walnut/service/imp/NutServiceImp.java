package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.nut.NutRequestForAdd;
import org.example.black_sea_walnut.dto.admin.nut.NutResponseForAdd;
import org.example.black_sea_walnut.dto.admin.nut.NutResponseForView;
import org.example.black_sea_walnut.entity.Nut;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.mapper.NutMapper;
import org.example.black_sea_walnut.repository.NutRepository;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.service.NutService;
import org.example.black_sea_walnut.service.specifications.NutSpecification;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NutServiceImp implements NutService {
    private final NutRepository nutRepository;
    private final ImageService imageService;
    private final NutMapper mapper;

    @Value("${upload.path}")
    private String contextPath;

    @Override
    public Nut getById(Long id) {
        LogUtil.logInfo("Fetching Nut by ID: " + id);
        return nutRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Nut with id: " + id + " was not found!"));
    }

    @Override
    public NutResponseForAdd getByIdInResponseDtoAdd(Long id) {
        LogUtil.logInfo("Fetching NutResponseForAdd by ID: " + id);
        return mapper.toResponseForAdd(getById(id));
    }

    @Override
    public Nut save(Nut entity) {
        LogUtil.logInfo("Saving Nut entity");
        return nutRepository.save(entity);
    }

    @SneakyThrows
    @Override
    public Nut save(NutRequestForAdd dto) {
        LogUtil.logInfo("Saving Nut from NutRequestForAdd");
        if (dto.getId() != null) {
            Nut nutById = getById(dto.getId());
            if (dto.getPathToImage().isEmpty())
                imageService.deleteByPath(nutById.getPathToImage());
            if (dto.getPathToSvg().isEmpty())
                imageService.deleteByPath(nutById.getPathToSvg());
        }

        if (dto.getFileImage() != null) {
            String generatedPath = contextPath + "/nuts/" + MediaType.image + "/" + imageService.generateFileName(dto.getFileImage());
            dto.setPathToImage(generatedPath);
        }
        if (dto.getFileSvg() != null) {
            String generatedPath = contextPath + "/nuts/" + MediaType.image + "/" + imageService.generateFileName(dto.getFileSvg());
            dto.setPathToSvg(generatedPath);
        }

        Nut nut = save(mapper.toEntityFromRequestAdd(dto));
        imageService.save(dto.getFileImage(), nut.getPathToImage());
        imageService.save(dto.getFileSvg(), nut.getPathToSvg());
        LogUtil.logInfo("Nut saved successfully");
        return nut;
    }

    @Override
    public List<Nut> getAll() {
        LogUtil.logInfo("Fetching all Nuts");
        return nutRepository.findAll();
    }

    @Override
    public List<NutResponseForAdd> getAllInResponseForAdd() {
        LogUtil.logInfo("Fetching all Nuts in response for add");
        return getAll().stream().map(mapper::toResponseForAdd).toList();
    }

    @Override
    public PageResponse<NutResponseForView> getAll(NutResponseForView response, Pageable pageable, LanguageCode code) {
        LogUtil.logInfo("Fetching paginated Nuts");
        Page<Nut> page = nutRepository.findAll(NutSpecification.getSpecification(response, code), pageable);
        List<NutResponseForView> responseDTOView = page.map(p -> mapper.toResponseForView(p, code)).stream().toList();
        return new PageResponse<>(responseDTOView, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting Nut by ID: " + id);
        Nut nut = getById(id);
        if (nut != null) {
            imageService.deleteByPath(nut.getPathToImage());
            imageService.deleteByPath(nut.getPathToSvg());
            nutRepository.deleteById(id);
        }
        LogUtil.logInfo("Nut deleted successfully");
    }
}
