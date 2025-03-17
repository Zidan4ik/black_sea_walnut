package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.PageResponse;
import org.example.black_sea_walnut.dto.admin.gallery.GalleryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.gallery.GalleryResponseForAdd;
import org.example.black_sea_walnut.entity.Gallery;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GalleryService {
    Gallery save(Gallery gallery);

    Gallery save(GalleryRequestForAdd dto);

    List<Gallery> getAll();

    List<Gallery> getAllByLanguageCode(LanguageCode code);

    List<GalleryResponseForAdd> getAllInResponseByLanguageCode(LanguageCode code);

    PageResponse<GalleryResponseForAdd> getAllInResponseByLanguageCode(Pageable pageable, LanguageCode code);

    void deleteById(Long id);

    Gallery getById(Long id);
}
