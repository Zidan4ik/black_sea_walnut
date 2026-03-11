package org.example.black_sea_walnut.service.news;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.web.NewResponseInWeb;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.mapper.NewMapper;
import org.example.black_sea_walnut.repository.NewRepository;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.file.FileProcessable;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.imp.ImageServiceImp;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.util.ImageUtil;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class NewServiceImp implements NewService {
    private final NewRepository newRepository;
    private final ImageServiceImp imageServiceImp;
    private final NewMapper newMapper;
    private final ImageUtil imageUtil;

    @Override
    public <R> PageResponse<R> getAll(Specification<New> spec, Pageable pageable, Function<New, R> mappingFunction) {
        LogUtil.logInfo("Fetching all news");
        Page<New> page = (spec == null) ? newRepository.findAll(pageable) : newRepository.findAll(spec, pageable);
        List<R> responsesDtoAdd = page.map(mappingFunction).getContent();
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
    public <R> R getByIdInResponse(Long id, Function<New,R> mappingFunction) {
        LogUtil.logInfo("Fetching news for web by ID: " + id);
        return mappingFunction.apply(getById(id));
    }

    @Override
    public List<NewResponseInWeb> getAllBySizeAmongLast(int size, LanguageCode code, Long id) {
        LogUtil.logInfo("Fetching last " + size + " news items except ID: " + id);
        return newRepository.getNewsThreeLast(size, id).stream().map(n -> newMapper.toResponseForWeb(n, code)).toList();
    }

    @Override
    public List<New> getAll() {
        LogUtil.logInfo("Fetching all news");
        return newRepository.findAll();
    }

    @Override
    public <R> List<R> getAllInResponseByActive(boolean IsActive, Function<New,R> mappingFunction) {
        LogUtil.logInfo("Fetching all active news in DTO response format");
        return newRepository.getAllByIsActive(IsActive).stream().map(mappingFunction).toList();
    }

    @Override
    @Transactional
    public New save(New entity) {
        LogUtil.logInfo("Saving news entity with ID: " + entity.getId());
        return newRepository.save(entity);
    }

    @Override
    @Transactional
    public <M extends GenericsMapper> New saveNew(Saveable<New, M> dto, M mapper) {
        LogUtil.logInfo("Saving news with file for ID: " + dto.getId());
        New entity = getOrCreate(dto.getId());
        if (dto instanceof FileProcessable fileDtos && dto instanceof Uploadable u) {
            imageUtil.handleImage(entity, fileDtos, u);
        }
        dto.updateEntity(entity, mapper);
        save(entity);
        LogUtil.logInfo("Saved news with ID: " + entity.getId());
        return entity;
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting news by ID: " + id);
        New new_ = getById(id);
        imageServiceImp.deleteByPath(new_.getPathToMedia());
        newRepository.deleteById(id);
    }

    private New getOrCreate(Long id) {
        return (id != null) ? getById(id) : new New();
    }
}
