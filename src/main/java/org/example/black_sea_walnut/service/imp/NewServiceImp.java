package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.new_.NewRequestForAdd;
import org.example.black_sea_walnut.dto.admin.new_.ResponseNewForView;
import org.example.black_sea_walnut.dto.web.NewResponseInWeb;
import org.example.black_sea_walnut.dto.web.ResponseNewForViewInWeb;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.NewMapper;
import org.example.black_sea_walnut.repository.NewRepository;
import org.example.black_sea_walnut.service.NewService;
import org.example.black_sea_walnut.service.specifications.NewSpecification;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewServiceImp implements NewService {
    private final NewRepository newRepository;
    private final ImageServiceImp imageServiceImp;
    private final NewMapper mapper = new NewMapper();
    @Value("${upload.path}")
    private String contextPath;

    @Override
    public PageResponse<ResponseNewForView> getAll(ResponseNewForView response, Pageable pageable, LanguageCode code) {
        LogUtil.logInfo("Fetching all news with filters");
        Page<New> page = newRepository.findAll(NewSpecification.getSpecification(response, code), pageable);
        List<ResponseNewForView> responsesDtoAdd = page.map(t -> mapper.toDtoView(t, code)).stream().toList();
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public PageResponse<ResponseNewForViewInWeb> getAll(Pageable pageable, LanguageCode code) {
        LogUtil.logInfo("Fetching all news for web");
        Page<New> page = newRepository.findAll(pageable);
        List<ResponseNewForViewInWeb> responsesDtoAdd = page.map(t -> mapper.toDtoViewForWeb(t, code)).stream().toList();
        return new PageResponse<>(responsesDtoAdd, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @Override
    public New getById(Long id) {
        LogUtil.logInfo("Fetching news by ID: " + id);
        return newRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("New with id: " + id + " was not found!"));
    }

    @Override
    public NewRequestForAdd getByIdLikeDTO(Long id) {
        LogUtil.logInfo("Fetching news DTO by ID: " + id);
        return mapper.toDtoAdd(getById(id));
    }

    @Override
    public NewResponseInWeb getByIdInResponseForWeb(Long id, LanguageCode code) {
        LogUtil.logInfo("Fetching news for web by ID: " + id);
        return mapper.toResponseForWeb(getById(id), code);
    }

    @Override
    public List<NewResponseInWeb> getAllBySizeAmongLast(int size, LanguageCode code, Long id) {
        LogUtil.logInfo("Fetching last " + size + " news items except ID: " + id);
        return newRepository.getNewsThreeLast(size, id).stream().map(n -> mapper.toResponseForWeb(n, code)).toList();
    }

    @Override
    public List<New> getAll() {
        LogUtil.logInfo("Fetching all news");
        return newRepository.findAll();
    }

    @Override
    public List<NewRequestForAdd> getAllInResponseForAdd() {
        LogUtil.logInfo("Fetching all news in DTO response format");
        return getAll().stream().map(mapper::toDtoAdd).toList();
    }

    @Override
    @Transactional
    public New save(New entity) {
        LogUtil.logInfo("Saving news entity with ID: " + entity.getId());
        if (entity.getId() != null) {
            New existingNew = getById(entity.getId());
            existingNew.getTranslations().clear();
            existingNew.getTranslations().addAll(entity.getTranslations());
        }
        return newRepository.save(entity);
    }

    @Override
    @Transactional
    public New saveLikeDto(NewRequestForAdd dto) {
        LogUtil.logInfo("Saving news DTO");
        return save(mapper.toEntity(dto));
    }

    @Override
    @Transactional
    public New saveImage(NewRequestForAdd dto) throws IOException {
        LogUtil.logInfo("Saving news with file for ID: " + dto.getId());
        if (dto.getId() != null) {
            New newById = getById(dto.getId());
            if (dto.getPathToImage().isEmpty()) {
                LogUtil.logInfo("Deleting old image at path: " + newById.getPathToMedia());
                imageServiceImp.deleteByPath(newById.getPathToMedia());
            }
        }

        if (dto.getFile() != null) {
            String generatedPath = contextPath + "/news/" + dto.getMediaType().toString() + "/" + imageServiceImp.generateFileName(dto.getFile());
            dto.setPathToImage(generatedPath);
            LogUtil.logInfo("Generated new path for image: " + generatedPath);
        }
        New entity = saveLikeDto(dto);
        imageServiceImp.save(dto.getFile(), entity.getPathToMedia());
        LogUtil.logInfo("Saved news with ID: " + entity.getId());
        return entity;
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting news by ID: " + id);
        New new_ = getById(id);
        if(new_ != null){
            imageServiceImp.deleteByPath(new_.getPathToMedia());
            newRepository.deleteById(id);
        }
    }
}
