package org.example.black_sea_walnut.service.nut;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.nut.NutResponseForAdd;
import org.example.black_sea_walnut.entity.Nut;
import org.example.black_sea_walnut.mapper.NutMapper;
import org.example.black_sea_walnut.repository.NutRepository;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.file.FileProcessable;
import org.example.black_sea_walnut.service.history.DtoResponse;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.util.ImageUtil;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class NutServiceImp implements NutService {
    private final NutRepository nutRepository;
    private final ImageService imageService;
    private final NutMapper nutMapper;
    private final ImageUtil imageUtil;

    @Override
    public Nut getById(Long id) {
        LogUtil.logInfo("Fetching Nut by ID: " + id);
        return nutRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Nut with id: " + id + " was not found!"));
    }

    @Override
    public <R extends DtoResponse> R getByIdInResponseDtoAdd(Long id, Function<Nut, R> mappingFunction) {
        LogUtil.logInfo("Fetching NutResponseForAdd by ID: " + id);
        Nut entity = getById(id);
        R response = mappingFunction.apply(entity);
        LogUtil.logInfo("Successfully returned mapped response for id: " + id);
        return response;
    }

    @Override
    public Nut save(Nut entity) {
        LogUtil.logInfo("Saving Nut entity");
        return nutRepository.save(entity);
    }

    @SneakyThrows
    public <M extends GenericsMapper> void saveNut(Saveable<Nut, M> dto, M mapper) {
        Nut entity = getOrCreate(dto.getId());
        if (dto instanceof FileProcessable dtos && dto instanceof Uploadable u) {
            imageUtil.handleImage(entity, dtos, u);
            imageUtil.handleSvg(entity, dtos, u);
        }
        dto.updateEntity(entity, mapper);
        nutRepository.save(entity);
    }

    @Override
    public List<Nut> getAll() {
        LogUtil.logInfo("Fetching all Nuts");
        return nutRepository.findAll();
    }

    @Override
    public List<NutResponseForAdd> getAllActiveInResponseForAdd() {
        LogUtil.logInfo("Fetching all nuts active in response for add");
        return nutRepository.getAllByIsActive(true).stream()
                .map(nutMapper::toResponseForAdd).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public <R> PageResponse<R> getAll(Specification<Nut> spec, Pageable pageable, Function<Nut,R> mappingFunction) {
        LogUtil.logInfo("Fetching paginated Nuts");
        Page<Nut> page = nutRepository.findAll(spec, pageable);
        List<R> list = page.map(mappingFunction).getContent();
        return new PageResponse<>(list, new PageResponse.Metadata(
                page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages()
        ));
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting Nut by ID: " + id);
        Nut nut = getById(id);
        imageService.deleteByPath(nut.getPathToImage());
        imageService.deleteByPath(nut.getPathToSvg());
        nutRepository.deleteById(id);
        LogUtil.logInfo("Nut deleted successfully");
    }


    private Nut getOrCreate(Long id) {
        return (id != null) ? getById(id) : new Nut();
    }
}
