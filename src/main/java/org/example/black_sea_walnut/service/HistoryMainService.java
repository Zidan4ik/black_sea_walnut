package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.admin.pages.main.request.*;
import org.example.black_sea_walnut.dto.admin.pages.main.response.*;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryMainMapper;
import org.example.black_sea_walnut.service.user.Saveable;

public interface HistoryMainService {
    BlockResponseForAddInMain getByPageTypeInResponseMainBlock(PageType type);

    ProductionResponseForAddInMain getByPageTypeInResponseProductionBlock(PageType type);

    FactoryBlockResponseForAddInMain getByPageTypeInResponseFactoryBlock(PageType type);

    NumberBlockResponseForAddInMain getByPageTypeInResponseNumberBlock(PageType type);

    AimBlockResponseForAddInMain getByPageTypeInResponseAimBlock(PageType type);

    EcoProductionResponseForAddInMain getByPageTypeInResponseEcoProductionBlock(PageType type);

    History saveHistory(Saveable<History, HistoryMainMapper> dto);
}
