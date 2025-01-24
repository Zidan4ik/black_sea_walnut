package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.pages.factory.request.BannerBlockRequestForAdd;
import org.example.black_sea_walnut.dto.pages.factory.request.BlockRequestForAdd;
import org.example.black_sea_walnut.dto.pages.factory.response.FactoryBannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.factory.response.BlockResponseForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;

public interface HistoryFactoryService {
    FactoryBannerBlockResponseForAdd getByPageTypeInResponseBannerBlock(PageType type);
    BlockResponseForAdd getByPageTypeInResponseBlock(PageType type);
    History saveHistoryBannerBlock(BannerBlockRequestForAdd dto);
    History saveHistoryBlock(BlockRequestForAdd dto);
}
