package org.example.black_sea_walnut.service.call;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.entity.Call;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.user.Saveable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.function.Function;

public interface CallService {
    <M extends GenericsMapper> Call save(Saveable<Call, M> dto, M mapper);

    List<Call> getAll();

    <R> PageResponse<R> getAll(Specification<Call> spec, Pageable pageable, Function<Call, R> mappingFunction);

    void deleteById(Long id);

    void deleteAllById(Iterable<? extends Long> ids);

    Call getById(Long id);
}
