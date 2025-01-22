package org.example.black_sea_walnut.mapper;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.historyMedia.HistoryMediaResponseForAdd;
import org.example.black_sea_walnut.dto.pages.main.request.*;
import org.example.black_sea_walnut.dto.pages.main.response.*;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.entity.translation.HistoryTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.PageType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HistoryMainMapper {
    private final HistoryMediaMapper mediaMapper;

    public MainBlockResponseForAdd toResponseMainBlockForAdd(History entity) {
        HistoryTranslation translateUk = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        HistoryTranslation translateEn = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);

        return MainBlockResponseForAdd
                .builder()
                .mainId(entity.getId())
                .mainIsActive(entity.isActive())
                .mainPathToBanner(entity.getBanner() != null ? entity.getBanner().getPathToMedia() : null)
                .mainTitleUk(translateUk != null ? translateUk.getTitle() : null)
                .mainTitleEn(translateEn != null ? translateEn.getTitle() : null)
                .mainDescriptionUk(translateUk != null ? translateUk.getDescription() : null)
                .mainDescriptionEn(translateEn != null ? translateEn.getDescription() : null)
                .build();
    }

    public History toEntityFromRequestForAdd(MainBlockRequestForAdd dto) {
        History entity = new History();
        entity.setId(dto.getMainId());
        entity.setActive(dto.getMainIsActive());
        entity.setPageType(PageType.main_banner);
        HistoryTranslation translationUk = new HistoryTranslation(LanguageCode.uk, dto.getMainTitleUk(), dto.getMainDescriptionUk(), entity);
        HistoryTranslation translationEn = new HistoryTranslation(LanguageCode.en, dto.getMainTitleEn(), dto.getMainDescriptionEn(), entity);
        entity.setTranslations(List.of(translationUk, translationEn));
        return entity;
    }

    public History toEntityFromRequestForAdd(AimBlockRequestForAdd dto) {
        History entity = new History();
        entity.setId(dto.getMainAimId());
        entity.setActive(dto.getMainAimIsActive());
        entity.setPageType(PageType.main_aim);
        HistoryTranslation translationUk = new HistoryTranslation(LanguageCode.uk, dto.getMainAimTitleUk(), dto.getMainAimDescriptionUk(), entity);
        HistoryTranslation translationEn = new HistoryTranslation(LanguageCode.en, dto.getMainAimTitleEn(), dto.getMainAimDescriptionEn(), entity);
        entity.setTranslations(List.of(translationUk, translationEn));
        return entity;
    }

    public History toEntityFromRequestForAdd(EcoProductionRequestForAdd dto) {
        History entity = new History();
        entity.setId(dto.getMainEcoProductionId());
        entity.setActive(dto.getMainEcoProductionIsActive());
        entity.setPageType(PageType.main_eco_production);
        HistoryTranslation translationUk = new HistoryTranslation(LanguageCode.uk, dto.getMainEcoProductionTitleUk(), dto.getMainEcoProductionDescriptionUk(), entity);
        HistoryTranslation translationEn = new HistoryTranslation(LanguageCode.en, dto.getMainEcoProductionTitleEn(), dto.getMainEcoProductionDescriptionEn(), entity);
        entity.setTranslations(List.of(translationUk, translationEn));
        return entity;
    }

    public ProductionBlockResponseForAdd toResponseProductionBlockForAdd(History entity) {
        HistoryTranslation translateUk = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        HistoryTranslation translateEn = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);
        return ProductionBlockResponseForAdd
                .builder()
                .mainProductionId(entity.getId())
                .mainProductionIsActive(entity.isActive())
                .mainProductionTitleUk(translateUk != null ? translateUk.getTitle() : null)
                .mainProductionTitleEn(translateEn != null ? translateEn.getTitle() : null)
                .mainProductionDescriptionUk(translateUk != null ? translateUk.getDescription() : null)
                .mainProductionDescriptionEn(translateEn != null ? translateEn.getDescription() : null)
                .build();
    }

    public History toEntityFromRequestForAdd(ProductionBlockRequestForAdd dto) {
        History entity = new History();
        entity.setId(dto.getMainProductionId());
        entity.setActive(dto.isMainProductionIsActive());
        entity.setPageType(PageType.main_production);
        HistoryTranslation translationUk = new HistoryTranslation(LanguageCode.uk, dto.getMainProductionTitleUk(), dto.getMainProductionDescriptionUk(), entity);
        HistoryTranslation translationEn = new HistoryTranslation(LanguageCode.en, dto.getMainProductionTitleEn(), dto.getMainProductionDescriptionEn(), entity);
        entity.setTranslations(List.of(translationUk, translationEn));
        return entity;
    }

    public FactoryBlockResponseForAdd toResponseFactoryBlockForAdd(History entity) {
        HistoryTranslation translateUk = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        HistoryTranslation translateEn = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);
        List<HistoryMediaResponseForAdd> mediaList = entity.getHistoryMedia().stream().map(mediaMapper::toResponseForAdd).toList();
        return FactoryBlockResponseForAdd
                .builder()
                .mainFactoryId(entity.getId())
                .mainFactoryIsActive(entity.isActive())
                .mainFactoryTitleUk(translateUk != null ? translateUk.getTitle() : null)
                .mainFactoryTitleEn(translateEn != null ? translateEn.getTitle() : null)
                .mainFactoryDescriptionUk(translateUk != null ? translateUk.getDescription() : null)
                .mainFactoryDescriptionEn(translateEn != null ? translateEn.getDescription() : null)
                .mainFactoryHistories(mediaList)
                .build();
    }

    public History toEntityFromRequestForAdd(FactoryBlockRequestForAdd dto) {
        History entity = new History();
        entity.setId(dto.getMainFactoryId());
        entity.setActive(dto.getMainFactoryIsActive());
        entity.setPageType(PageType.main_factory_about);
        HistoryTranslation translationUk = new HistoryTranslation(LanguageCode.uk, dto.getMainFactoryTitleUk(), dto.getMainFactoryDescriptionUk(), entity);
        HistoryTranslation translationEn = new HistoryTranslation(LanguageCode.en, dto.getMainFactoryTitleEn(), dto.getMainFactoryDescriptionEn(), entity);
        entity.setTranslations(List.of(translationUk, translationEn));
        if(dto.getFiles()!=null) entity.setHistoryMedia(dto.getFiles().stream().map(t -> mediaMapper.toEntityFromRequestForAdd(t, entity)).toList());
        return entity;
    }

    public NumberBlockResponseForAdd toResponseNumberBlockForAdd(History entity) {
        HistoryTranslation translateUk = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        HistoryTranslation translateEn = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);
        return NumberBlockResponseForAdd
                .builder()
                .mainNumberId(entity.getId())
                .mainNumberIsActive(entity.isActive())
                .mainNumberTitle1(translateUk != null ? translateUk.getTitle() : null)
                .mainNumberTitle2(translateUk != null ? translateUk.getTitle2() : null)
                .mainNumberTitle3(translateUk != null ? translateUk.getTitle3() : null)
                .mainNumberTitle4(translateUk != null ? translateUk.getTitle4() : null)
                .mainNumberDescriptionUk1(translateUk != null ? translateUk.getDescription() : null)
                .mainNumberDescriptionUk2(translateUk != null ? translateUk.getDescription2() : null)
                .mainNumberDescriptionUk3(translateUk != null ? translateUk.getDescription3() : null)
                .mainNumberDescriptionUk4(translateUk != null ? translateUk.getDescription4() : null)
                .mainNumberDescriptionEn1(translateEn != null ? translateEn.getDescription() : null)
                .mainNumberDescriptionEn2(translateEn != null ? translateEn.getDescription2() : null)
                .mainNumberDescriptionEn3(translateEn != null ? translateEn.getDescription3() : null)
                .mainNumberDescriptionEn4(translateEn != null ? translateEn.getDescription4() : null)
                .build();
    }

    public History toEntityFromRequestForAdd(NumberBlockRequestForAdd dto) {
        History entity = new History();
        entity.setId(dto.getMainNumberId());
        entity.setActive(dto.getMainNumberIsActive());
        entity.setPageType(PageType.main_numbers);
        HistoryTranslation translationUk = new HistoryTranslation(LanguageCode.uk, dto.getMainNumberTitle1(), dto.getMainNumberTitle2(), dto.getMainNumberTitle3(), dto.getMainNumberTitle4(), dto.getMainNumberDescriptionUk1(), dto.getMainNumberDescriptionUk2(), dto.getMainNumberDescriptionUk3(), dto.getMainNumberDescriptionUk4(), entity);
        HistoryTranslation translationEn = new HistoryTranslation(LanguageCode.en, dto.getMainNumberTitle1(), dto.getMainNumberTitle2(), dto.getMainNumberTitle3(), dto.getMainNumberTitle4(), dto.getMainNumberDescriptionEn1(), dto.getMainNumberDescriptionEn2(), dto.getMainNumberDescriptionEn3(), dto.getMainNumberDescriptionEn4(), entity);
        entity.setTranslations(List.of(translationUk, translationEn));
        return entity;
    }

    public AimBlockResponseForAdd toResponseAimBlockForAdd(History entity) {
        HistoryTranslation translateUk = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        HistoryTranslation translateEn = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);
        return AimBlockResponseForAdd
                .builder()
                .mainAimId(entity.getId())
                .mainAimIsActive(entity.isActive())
                .mainAimTitleUk(translateUk != null ? translateUk.getTitle() : null)
                .mainAimTitleEn(translateEn != null ? translateEn.getTitle() : null)
                .mainAimDescriptionUk(translateUk != null ? translateUk.getDescription() : null)
                .mainAimDescriptionEn(translateEn != null ? translateEn.getDescription() : null)
                .mainAimPathToBanner(entity.getBanner() != null ? entity.getBanner().getPathToMedia() : null)
                .build();
    }

    public EcoProductionResponseForAdd toResponseEcoProductionBLockForAdd(History entity) {
        HistoryTranslation translateUk = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        HistoryTranslation translateEn = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);
        return EcoProductionResponseForAdd
                .builder()
                .mainEcoProductionId(entity.getId())
                .mainEcoProductionIsActive(entity.isActive())
                .mainEcoProductionTitleUk(translateUk != null ? translateUk.getTitle() : null)
                .mainEcoProductionTitleEn(translateEn != null ? translateEn.getTitle() : null)
                .mainEcoProductionDescriptionUk(translateUk != null ? translateUk.getDescription() : null)
                .mainEcoProductionDescriptionEn(translateEn != null ? translateEn.getDescription() : null)
                .mainEcoProductionPathToBanner(entity.getBanner() != null ? entity.getBanner().getPathToMedia() : null)
                .build();
    }
}
