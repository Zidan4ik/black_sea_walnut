package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.pages.main.request.*;
import org.example.black_sea_walnut.dto.pages.main.response.*;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;


public interface HistoryMainService {
    MainBlockResponseForAdd getByPageTypeInResponseMainBlock(PageType type);
    ProductionBlockResponseForAdd getByPageTypeInResponseProductionBlock(PageType type);
    FactoryBlockResponseForAdd getByPageTypeInResponseFactoryBlock(PageType type);
    NumberBlockResponseForAdd getByPageTypeInResponseNumberBlock(PageType type);
    AimBlockResponseForAdd getByPageTypeInResponseAimBlock(PageType type);
    EcoProductionResponseForAdd getByPageTypeInResponseEcoProductionBlock(PageType type);
    History saveHistoryMainBlock(MainBlockRequestForAdd dto);
    History saveHistoryAimBlock(AimBlockRequestForAdd dto);
    History saveHistoryEcoProductionBlock(EcoProductionRequestForAdd dto);
    History saveHistoryProductionBlock(ProductionBlockRequestForAdd dto);
    History saveHistoryNumberBlock(NumberBlockRequestForAdd dto);
    History saveHistoryFactoryBlock(FactoryBlockRequestForAdd dto);
}
