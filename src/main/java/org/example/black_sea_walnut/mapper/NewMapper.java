package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.admin.new_.NewRequestForAdd;
import org.example.black_sea_walnut.dto.admin.new_.ResponseNewForView;
import org.example.black_sea_walnut.dto.web.NewResponseInWeb;
import org.example.black_sea_walnut.dto.web.ResponseNewForViewInWeb;
import org.example.black_sea_walnut.entity.New;
import org.example.black_sea_walnut.entity.translation.NewTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NewMapper implements GenericsMapper {
    public ResponseNewForView toResponseForView(New entity, LanguageCode code) {
        NewTranslation translation = entity.getTranslations()
                .stream()
                .filter(c -> c.getLanguageCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Translation not found for language code: " + code));
        return ResponseNewForView.builder().id(entity.getId())
                .title(translation.getTitle())
                .description(translation.getDescription())
                .code(code)
                .isActive(entity.isActive())
                .date(DateUtil.toFormatDateFromDB(entity.getDateOfPublication(), "dd.MM.yyyy"))
                .id(entity.getId())
                .build();
    }

    public ResponseNewForViewInWeb toDtoViewForWeb(New entity, LanguageCode code) {
        NewTranslation translation = entity.getTranslations()
                .stream()
                .filter(c -> c.getLanguageCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Translation not found for language code: " + code));
        return ResponseNewForViewInWeb.builder().id(entity.getId())
                .title(translation.getTitle())
                .description(translation.getDescription())
                .isActive(entity.isActive())
                .date(DateUtil.toFormatDateFromDB(entity.getDateOfPublication(), "dd.MM.yyyy"))
                .id(entity.getId())
                .pathToImage(entity.getPathToMedia())
                .build();
    }

    public NewRequestForAdd toDtoAdd(New entity) {
        NewRequestForAdd builder = NewRequestForAdd.builder()
                .id(entity.getId())
                .isActive(entity.isActive())
                .mediaType(entity.getMediaType())
                .pathToImage(entity.getPathToMedia() != null ? entity.getPathToMedia() : "")
                .dateOfPublication(DateUtil.toFormatDateFromDB(entity.getDateOfPublication(), "dd.MM.yyyy"))
                .build();

        for (NewTranslation t : entity.getTranslations()) {
            switch (t.getLanguageCode()) {
                case uk -> {
                    builder.setTitleUA(t.getTitle());
                    builder.setDescriptionUA(t.getDescription());
                }
                case en -> {
                    builder.setTitleENG(t.getTitle());
                    builder.setDescriptionENG(t.getDescription());
                }
            }
        }

        return builder;
    }

    public New toEntityForSaveNew(NewRequestForAdd dto, New entity) {
        entity.setId(dto.getId());
        entity.setActive(dto.isActive());
        entity.setMediaType(dto.getMediaType());
        entity.setPathToMedia(dto.getPathToImage());
        entity.setDateOfPublication(DateUtil.toFormatDateToDB(dto.getDateOfPublication(), "dd.MM.yyyy"));

        NewTranslation translationUA = new NewTranslation(LanguageCode.uk, dto.getTitleUA(), dto.getDescriptionUA(), entity);
        NewTranslation translationEN = new NewTranslation(LanguageCode.en, dto.getTitleENG(), dto.getDescriptionENG(), entity);
        entity.getTranslations().clear();
        entity.getTranslations().addAll(new ArrayList<>(List.of(translationUA,translationEN)));
        return entity;
    }

    public NewResponseInWeb toResponseForWeb(New entity, LanguageCode code) {
        NewTranslation translation = entity.getTranslations()
                .stream()
                .filter(c -> c.getLanguageCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Translation not found for language code: " + code));

        return NewResponseInWeb
                .builder()
                .id(entity.getId())
                .date(DateUtil.toFormatDateFromDB(entity.getDateOfPublication(), "dd.MM.yyyy"))
                .title(translation.getTitle())
                .isActive(entity.isActive())
                .pathToImage(entity.getPathToMedia())
                .description(translation.getDescription())
                .build();
    }
}
