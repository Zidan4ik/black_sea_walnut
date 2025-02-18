package org.example.black_sea_walnut.mapper.pages;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.pages.clients.request.ClientBannerRequestForAdd;
import org.example.black_sea_walnut.dto.pages.clients.request.ClientCategoryRequestForAdd;
import org.example.black_sea_walnut.dto.pages.clients.request.ClientEcoProductionRequestForAdd;
import org.example.black_sea_walnut.dto.pages.clients.response.ClientBannerResponseForAdd;
import org.example.black_sea_walnut.dto.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.dto.pages.clients.response.ClientEcoProductionResponseForAdd;
import org.example.black_sea_walnut.entity.ClientCategory;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.entity.translation.ClientCategoryTranslation;
import org.example.black_sea_walnut.entity.translation.HistoryTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.PageType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HistoryClientsMapper {
    public ClientBannerResponseForAdd toResponseBannerBlockForAdd(History entity) {
        HistoryTranslation translateUk = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        HistoryTranslation translateEn = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);
        return ClientBannerResponseForAdd
                .builder()
                .clientsBannerId(entity.getId())
                .clientsBannerIsActive(entity.isActive())
                .clientsBannerPathToBanner(entity.getBanner() != null ? entity.getBanner().getPathToMedia() : null)
                .clientsBannerTitleUk(translateUk != null ? translateUk.getTitle() : null)
                .clientsBannerTitleEn(translateEn != null ? translateEn.getTitle() : null)
                .clientsBannerDescriptionUk(translateUk != null ? translateUk.getDescription() : null)
                .clientsBannerDescriptionEn(translateEn != null ? translateEn.getDescription() : null)
                .build();
    }

    public ClientCategoryResponseForAdd toResponseCategoryForAdd(ClientCategory entity) {
        ClientCategoryTranslation translateUk = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        ClientCategoryTranslation translateEn = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);
        return ClientCategoryResponseForAdd
                .builder()
                .clientCategoryId(entity.getId())
                .clientCategoryIsActive(entity.isActive())
                .clientCategoryTitleUk(translateUk != null ? translateUk.getTitle() : "")
                .clientCategoryTitleEn(translateEn != null ? translateEn.getTitle() : "")
                .clientCategorySubtitleUk(translateUk != null ? translateUk.getSubtitle() : "")
                .clientCategorySubtitleEn(translateEn != null ? translateEn.getSubtitle() : "")
                .clientCategoryDescriptionUk(translateUk != null ? translateUk.getDescription() : "")
                .clientCategoryDescriptionEn(translateEn != null ? translateEn.getDescription() : "")
                .clientCategoryPathToImage(entity.getPathToImage())
                .clientCategoryPathToSvg(entity.getPathToSvg())
                .build();
    }

    public ClientEcoProductionResponseForAdd toResponseEcoProductionBlockForAdd(History entity) {
        HistoryTranslation translateUk = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        HistoryTranslation translateEn = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);
        return ClientEcoProductionResponseForAdd
                .builder()
                .clientsEcoProductionId(entity.getId())
                .clientsEcoProductionIsActive(entity.isActive())
                .clientsEcoProductionPathToBanner(entity.getBanner() != null ? entity.getBanner().getPathToMedia() : null)
                .clientsEcoProductionTitleUk(translateUk != null ? translateUk.getTitle() : null)
                .clientsEcoProductionTitleEn(translateEn != null ? translateEn.getTitle() : null)
                .clientsEcoProductionDescriptionUk(translateUk != null ? translateUk.getDescription() : null)
                .clientsEcoProductionDescriptionEn(translateEn != null ? translateEn.getDescription() : null)
                .build();
    }

    public History toEntityFromRequestBannerBlock(ClientBannerRequestForAdd dto) {
        History entity = new History();
        entity.setId(dto.getClientsBannerId());
        entity.setActive(dto.getClientsBannerIsActive());
        entity.setPageType(PageType.clients_banner);
        HistoryTranslation translationUk = new HistoryTranslation(LanguageCode.uk, dto.getClientsBannerTitleUk(), dto.getClientsBannerDescriptionUk(), entity);
        HistoryTranslation translationEn = new HistoryTranslation(LanguageCode.en, dto.getClientsBannerTitleEn(), dto.getClientsBannerDescriptionEn(), entity);
        entity.setTranslations(List.of(translationUk, translationEn));
        return entity;
    }

    public ClientCategory toEntityFromRequestClientCategoryBlock(ClientCategoryRequestForAdd dto) {
        ClientCategory entity = new ClientCategory();
        entity.setId(dto.getClientsCategoryId());
        entity.setActive(dto.getClientsCategoryIsActive());
        entity.setMediaTypeSvg(dto.getMediaTypeSvg());
        entity.setMediaTypeImage(dto.getMediaTypeImage());
        entity.setPathToImage(dto.getClientsCategoryPathToImage());
        entity.setPathToSvg(dto.getClientsCategoryPathToSvg());
        ClientCategoryTranslation translationUk = new ClientCategoryTranslation(null, LanguageCode.uk, dto.getClientsCategoryTitleUk(), dto.getClientsCategorySubtitleUk(), dto.getClientsCategoryDescriptionUk(), entity);
        ClientCategoryTranslation translationEn = new ClientCategoryTranslation(null, LanguageCode.en, dto.getClientsCategoryTitleEn(), dto.getClientsCategorySubtitleEn(), dto.getClientsCategoryDescriptionEn(), entity);
        entity.setTranslations(List.of(translationUk, translationEn));
        return entity;
    }

    public History toEntityFromRequestClientEcoProductionBlock(ClientEcoProductionRequestForAdd dto) {
        History entity = new History();
        entity.setId(dto.getClientsEcoProductionId());
        entity.setActive(dto.getClientsEcoProductionIsActive());
        entity.setPageType(PageType.clients_eco_production);
        HistoryTranslation translationUk = new HistoryTranslation(LanguageCode.uk, dto.getClientsEcoProductionTitleUk(), dto.getClientsEcoProductionDescriptionUk(), entity);
        HistoryTranslation translationEn = new HistoryTranslation(LanguageCode.en, dto.getClientsEcoProductionTitleEn(), dto.getClientsEcoProductionDescriptionEn(), entity);
        entity.setTranslations(List.of(translationUk, translationEn));
        return entity;
    }
}
