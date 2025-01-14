package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.gallery.GalleryRequestForAdd;
import org.example.black_sea_walnut.dto.gallery.GalleryResponseForAdd;
import org.example.black_sea_walnut.entity.Gallery;
import org.example.black_sea_walnut.enums.LanguageCode;

import java.util.List;

public interface GalleryService {
    Gallery save(Gallery gallery);
    Gallery save(GalleryRequestForAdd dto);
    List<Gallery> getAll();
    List<Gallery> getAllByLanguageCode(LanguageCode code);
    List<GalleryResponseForAdd> getAllInResponse(LanguageCode code);
    void deleteById(Long id);
    Gallery getById(Long id);
}
