package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.gallery.GalleryRequestForAdd;
import org.example.black_sea_walnut.dto.gallery.GalleryResponseForAdd;
import org.example.black_sea_walnut.entity.Gallery;
import org.example.black_sea_walnut.entity.translation.GalleryTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class GalleryMapper {
    public Gallery toEntityForRequestAdd(GalleryRequestForAdd dto) {
        Gallery entity = new Gallery();
        entity.setId(dto.getId());
        entity.setActive(dto.isActive());
        entity.setPathToMedia(dto.getPathToMediaFile());
        entity.setMediaType(dto.getMediaType());
        entity.setTranslations(List.of(new GalleryTranslation(null, LanguageCode.uk, dto.getTitle(), dto.getDescription(),entity)));
        entity.setTimeUpdate(LocalDateTime.now());
        return entity;
    }

    public GalleryResponseForAdd toResponseForAdd(Gallery entity) {
        GalleryTranslation translation = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        return GalleryResponseForAdd.builder()
                .id(entity.getId())
                .isActive(entity.isActive())
                .pathToMediaFile(entity.getPathToMedia())
                .title(translation != null ? translation.getTitle() : null)
                .description(translation != null ? translation.getDescription() : null)
                .build();
    }
}
