package org.example.black_sea_walnut.mapper;

import org.example.black_sea_walnut.dto.nut.NutRequestForAdd;
import org.example.black_sea_walnut.dto.nut.NutResponseForAdd;
import org.example.black_sea_walnut.dto.nut.NutResponseForView;
import org.example.black_sea_walnut.entity.Nut;
import org.example.black_sea_walnut.entity.translation.NutTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.util.DateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class NutMapper {
    public Nut toEntityFromRequestAdd(NutRequestForAdd dto) {
        Nut entity = new Nut();
        entity.setId(dto.getId());
        entity.setActive(dto.isActive());
        entity.setPathToImage(dto.getPathToImage());
        entity.setPathToSvg(dto.getPathToSvg());
        entity.setDate(LocalDate.now());

        NutTranslation translationUk = new NutTranslation(null, LanguageCode.uk, dto.getTitleUk(), dto.getDescriptionUk(), entity);
        NutTranslation translationEn = new NutTranslation(null, LanguageCode.en, dto.getTitleEn(), dto.getDescriptionEn(), entity);

        entity.setTranslations(List.of(translationUk, translationEn));
        return entity;
    }

    public NutResponseForAdd toResponseForAdd(Nut entity) {
        NutTranslation translationUk = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst()
                .orElse(null);
        NutTranslation translationEn = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst()
                .orElse(null);
        return NutResponseForAdd.builder()
                .id(entity.getId())
                .isActive(entity.isActive())
                .pathToSvg(entity.getPathToSvg())
                .pathToImage(entity.getPathToImage())
                .titleUk(translationUk != null ? translationUk.getTitle() : null)
                .titleEn(translationEn != null ? translationEn.getTitle() : null)
                .descriptionUk(translationUk != null ? translationUk.getDescription() : null)
                .descriptionEn(translationEn != null ? translationEn.getDescription() : null)
                .build();
    }

    public NutResponseForView toResponseForView(Nut entity, LanguageCode languageCode) {
        NutTranslation translation = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(languageCode)).findFirst().orElse(null);
        return NutResponseForView
                .builder()
                .id(entity.getId())
                .dateOfUpdated(DateUtil.toFormatDateFromDB(entity.getDate(), "dd.MM.yyyy"))
                .title(translation != null ? translation.getTitle() : null)
                .description(translation != null ? translation.getDescription() : null)
                .isActive(entity.isActive())
                .build();
    }
}
