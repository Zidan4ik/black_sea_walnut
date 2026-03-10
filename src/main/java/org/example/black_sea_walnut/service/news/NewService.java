package org.example.black_sea_walnut.service.news;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.new_.NewRequestForAdd;
import org.example.black_sea_walnut.dto.admin.new_.ResponseNewForView;
import org.example.black_sea_walnut.dto.web.NewResponseInWeb;
import org.example.black_sea_walnut.dto.web.ResponseNewForViewInWeb;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.specifications.NewSpecification;
import org.example.black_sea_walnut.service.user.Saveable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public interface NewService {
    <R> PageResponse<R> getAll(Specification<New> spec, Pageable pageable, Function<New,R> mappingFunction);

    List<New> getAll();

    List<NewRequestForAdd> getAllActiveInResponseForAdd();

    New getById(Long id);

    NewRequestForAdd getByIdLikeDTO(Long id);

    NewResponseInWeb getByIdInResponseForWeb(Long id, LanguageCode code);

    List<NewResponseInWeb> getAllBySizeAmongLast(int size, LanguageCode code, Long currentId);

    New save(New entity);

    <M extends GenericsMapper> New saveNew(Saveable<New,M> dto, M mapper) throws IOException;

    void deleteById(Long id);
}
