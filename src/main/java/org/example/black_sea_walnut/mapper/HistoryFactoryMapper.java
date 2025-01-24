package org.example.black_sea_walnut.mapper;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.pages.factory.request.BannerBlockRequestForAdd;
import org.example.black_sea_walnut.dto.pages.factory.request.BlockRequestForAdd;
import org.example.black_sea_walnut.dto.pages.factory.response.FactoryBannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.factory.response.BlockResponseForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.entity.translation.HistoryTranslation;
import org.example.black_sea_walnut.enums.LanguageCode;
import org.example.black_sea_walnut.enums.PageType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HistoryFactoryMapper {
    private final HistoryMediaMapper mediaMapper;

    public FactoryBannerBlockResponseForAdd toResponseBannerBlockForAdd(History entity) {
        HistoryTranslation translateUk = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        HistoryTranslation translateEn = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);

        return FactoryBannerBlockResponseForAdd
                .builder()
                .factoryBannerId(entity.getId())
                .factoryBannerIsActive(entity.isActive())
                .factoryBannerPathToImage(entity.getBanner() != null ? entity.getBanner().getPathToMedia() : null)
                .factoryBannerTitleUk(translateUk!=null?translateUk.getTitle():null)
                .factoryBannerTitleEn(translateEn!=null?translateEn.getTitle():null)
                .factoryBannerDescriptionUk(translateUk!=null?translateUk.getDescription():null)
                .factoryBannerDescriptionEn(translateEn!=null?translateEn.getDescription():null)
                .build();
    }

    public BlockResponseForAdd toResponseBlockForAdd(History entity) {
        HistoryTranslation translateUk = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.uk)).findFirst().orElse(null);
        HistoryTranslation translateEn = entity.getTranslations().stream().filter(t -> t.getLanguageCode().equals(LanguageCode.en)).findFirst().orElse(null);
        return BlockResponseForAdd
                .builder()
                .factoryBlockId(entity.getId())
                .factoryBlockIsActive(entity.isActive())
                .factoryBlockTitleUk(translateUk != null ? translateUk.getTitle() : "")
                .factoryBlockTitleEn(translateEn != null ? translateEn.getTitle() : "")
                .factoryBlockDescriptionUk(translateUk != null ? translateUk.getDescription() : "")
                .factoryBlockDescriptionEn(translateEn != null ? translateEn.getDescription() : "")
                .factoryBlockFiles(entity.getHistoryMedia().stream().map(mediaMapper::toResponseForAdd).toList())
                .build();
    }

    public History toEntityFromRequestBannerBlock(BannerBlockRequestForAdd dto) {
        History entity = new History();
        entity.setId(dto.getFactoryBannerId());
        entity.setActive(dto.getFactoryBannerIsActive());
        entity.setPageType(PageType.factory_banner);
        HistoryTranslation translationUk = new HistoryTranslation(LanguageCode.uk, dto.getFactoryBannerTitleUk(), dto.getFactoryBannerDescriptionUk(), entity);
        HistoryTranslation translationEn = new HistoryTranslation(LanguageCode.en, dto.getFactoryBannerTitleEn(), dto.getFactoryBannerDescriptionEn(), entity);
        entity.setTranslations(List.of(translationUk,translationEn));
        return entity;
    }

    public History toEntityFromRequestFactoryBlock(BlockRequestForAdd dto) {
        History entity = new History();
        entity.setId(dto.getFactoryBlockId());
        entity.setActive(dto.getFactoryBlockIsActive());
        entity.setPageType(PageType.factory_block2);
        HistoryTranslation translationUk = new HistoryTranslation(LanguageCode.uk, dto.getFactoryBlockTitleUk(), dto.getFactoryBlockDescriptionUk(), entity);
        HistoryTranslation translationEn = new HistoryTranslation(LanguageCode.en, dto.getFactoryBlockTitleEn(), dto.getFactoryBlockDescriptionEn(), entity);
        entity.setTranslations(List.of(translationUk, translationEn));
        if (dto.getFactoryBlockFiles() != null)
            entity.setHistoryMedia(dto.getFactoryBlockFiles().stream().map(t -> mediaMapper.toEntityFromRequestForAdd(t, entity)).toList());
        return entity;
    }
}
