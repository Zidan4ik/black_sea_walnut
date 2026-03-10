package org.example.black_sea_walnut.service.nut;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.nut.NutResponseForAdd;
import org.example.black_sea_walnut.entity.Nut;
import org.example.black_sea_walnut.service.history.DtoResponse;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.user.Saveable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.function.Function;

public interface NutService {
    Nut getById(Long id);

    <R extends DtoResponse> R getByIdInResponseDtoAdd(Long id, Function<Nut,R> mappingFunction);

    <M extends GenericsMapper> void saveNut(Saveable<Nut, M> dto, M mapper);

    Nut save(Nut entity);

    List<Nut> getAll();

    List<NutResponseForAdd> getAllActiveInResponseForAdd();

   <R> PageResponse<R> getAll(Specification<Nut> spec, Pageable pageable, Function<Nut, R> mappingFunction);

    void deleteById(Long id);
}
