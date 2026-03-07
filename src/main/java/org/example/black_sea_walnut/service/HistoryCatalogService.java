package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.admin.pages.catalog.request.BannerBlockRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.request.EcologicallyBlockRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.BannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.EcologicallyBlockResponseForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryCatalogMapper;
import org.example.black_sea_walnut.service.user.Saveable;


public interface HistoryCatalogService {
    BannerBlockResponseForAdd getByPageTypeInResponseBannerBlock(PageType type);

    EcologicallyBlockResponseForAdd getByPageTypeInResponseEcologicallyBlock(PageType type);

    History saveHistory(Saveable<History, HistoryCatalogMapper> dto);
}
