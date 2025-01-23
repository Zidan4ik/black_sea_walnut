package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.pages.catalog.request.BannerBlockRequestForAdd;
import org.example.black_sea_walnut.dto.pages.catalog.request.EcologicallyBlockRequestForAdd;
import org.example.black_sea_walnut.dto.pages.catalog.response.BannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.catalog.response.EcologicallyBlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.main.request.*;
import org.example.black_sea_walnut.dto.pages.main.response.*;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;


public interface HistoryCatalogService {
    BannerBlockResponseForAdd getByPageTypeInResponseBannerBlock(PageType type);
    EcologicallyBlockResponseForAdd getByPageTypeInResponseEcologicallyBlock(PageType type);
    History saveHistoryBannerBlock(BannerBlockRequestForAdd dto);
    History saveHistoryEcologicallyBlock(EcologicallyBlockRequestForAdd dto);
}
