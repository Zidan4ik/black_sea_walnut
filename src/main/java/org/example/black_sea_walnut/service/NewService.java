package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.new_.NewRequestForAdd;
import org.example.black_sea_walnut.dto.admin.new_.ResponseNewForView;
import org.example.black_sea_walnut.dto.web.NewResponseInWeb;
import org.example.black_sea_walnut.dto.web.ResponseNewForViewInWeb;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface NewService {
    PageResponse<ResponseNewForView> getAll(ResponseNewForView response, Pageable pageable, LanguageCode code);

    PageResponse<ResponseNewForViewInWeb> getAll(Pageable pageable, LanguageCode code);

    List<New> getAll();

    List<NewRequestForAdd> getAllInResponseForAdd();

    List<NewRequestForAdd> getAllActiveInResponseForAdd();

    New getById(Long id);

    NewRequestForAdd getByIdLikeDTO(Long id);

    NewResponseInWeb getByIdInResponseForWeb(Long id, LanguageCode code);

    List<NewResponseInWeb> getAllBySizeAmongLast(int size, LanguageCode code, Long currentId);

    New save(New entity);

    New saveLikeDto(NewRequestForAdd dto);

    New saveImage(NewRequestForAdd dto) throws IOException;

    void deleteById(Long id);
}
