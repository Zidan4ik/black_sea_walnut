package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.ResponseNewForAdd;
import org.example.black_sea_walnut.dto.ResponseNewForView;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface NewService {
    PageResponse<ResponseNewForView> getAll(ResponseNewForView response, Pageable pageable, LanguageCode code);

    List<New> getAll();

    New getById(Long id);

    ResponseNewForAdd getByIdLikeDTO(Long id);

    New save(New entity);

    New saveLikeDto(ResponseNewForAdd dto);

    New saveImage(ResponseNewForAdd dto) throws IOException;

    void deleteById(Long id);
}
