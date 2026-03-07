package org.example.black_sea_walnut.mapper.pages;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.pages.catalog.request.BannerBlockRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.request.EcologicallyBlockRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.BannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.EcologicallyBlockResponseForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.entity.HistoryMedia;
import org.example.black_sea_walnut.entity.translation.HistoryTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.HistoryMediaMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HistoryCatalogMapper {
    private final HistoryMediaMapper mediaMapper;

    public BannerBlockResponseForAdd toResponseBannerBlockForAdd(History entity) {
        return BannerBlockResponseForAdd
                .builder()
                .catalogBannerId(entity.getId())
                .catalogBannerIsActive(entity.isActive())
                .catalogBannerPathToImage(entity.getBanner() != null ? entity.getBanner().getPathToMedia() : null)
                .build();
    }

    public EcologicallyBlockResponseForAdd toResponseEcologicallyBlockForAdd(History entity) {
        HistoryTranslation translateUk = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        HistoryTranslation translateEn = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);
        return EcologicallyBlockResponseForAdd
                .builder()
                .catalogEcologicallyId(entity.getId())
                .catalogEcologicallyIsActive(entity.isActive())
                .catalogEcologicallyTitleUk(translateUk != null ? translateUk.getTitle() : "")
                .catalogEcologicallyTitleEn(translateEn != null ? translateEn.getTitle() : "")
                .catalogEcologicallySubtitleUk(translateUk != null ? translateUk.getSubtitle() : "")
                .catalogEcologicallySubtitleEn(translateEn != null ? translateEn.getSubtitle() : "")
                .catalogEcologicallyDescriptionUk(translateUk != null ? translateUk.getDescription() : "")
                .catalogEcologicallyDescriptionEn(translateEn != null ? translateEn.getDescription() : "")
                .catalogEcologicallyFiles(entity.getHistoryMedia().stream().map(mediaMapper::toResponseForAdd).toList())
                .build();
    }

    public History toEntityFromRequestBannerBlock(BannerBlockRequestForAdd dto,History entity) {
        entity.setId(dto.getCatalogBannerId());
        entity.setActive(dto.getCatalogBannerIsActive());
        entity.setPageType(PageType.catalog_banner);
        return entity;
    }

    public History toEntityFromRequestEcologicallyBlock(EcologicallyBlockRequestForAdd dto, History entity) {
        entity.setId(dto.getCatalogEcologicallyId());
        entity.setActive(dto.getCatalogEcologicallyIsActive());
        entity.setPageType(PageType.catalog_ecologically_pure_walnut);

        HistoryTranslation translationUk = new HistoryTranslation(null, LanguageCode.uk, dto.getCatalogEcologicallyTitleUk(), dto.getCatalogEcologicallySubtitleUk(), dto.getCatalogEcologicallyDescriptionUk(), entity);
        HistoryTranslation translationEn = new HistoryTranslation(null, LanguageCode.en, dto.getCatalogEcologicallyTitleEn(), dto.getCatalogEcologicallySubtitleEn(), dto.getCatalogEcologicallyDescriptionEn(), entity);
        entity.getTranslations().clear();
        entity.getTranslations().addAll(new ArrayList<>(List.of(translationUk, translationEn)));

        if (dto.getFiles() != null) {
            ArrayList<HistoryMedia> medias = dto.getFiles().stream()
                    .map(m -> mediaMapper.toEntityFromRequestForAdd(m, entity))
                    .collect(Collectors.toCollection(ArrayList::new));
            entity.getHistoryMedia().clear();
            entity.getHistoryMedia().addAll(medias);
        } else {
            entity.getHistoryMedia().clear();
        }
        return entity;
    }
}
