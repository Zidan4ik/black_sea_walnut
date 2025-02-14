package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.pages.main.request.*;
import org.example.black_sea_walnut.dto.pages.main.response.*;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;


public interface HistoryMainService {
    BlockResponseForAddInMain getByPageTypeInResponseMainBlock(PageType type);
    ProductionResponseForAddInMain getByPageTypeInResponseProductionBlock(PageType type);
    FactoryBlockResponseForAddInMain getByPageTypeInResponseFactoryBlock(PageType type);
    NumberBlockResponseForAddInMain getByPageTypeInResponseNumberBlock(PageType type);
    AimBlockResponseForAddInMain getByPageTypeInResponseAimBlock(PageType type);
    EcoProductionResponseForAddInMain getByPageTypeInResponseEcoProductionBlock(PageType type);
    History saveHistoryMainBlock(MainBlockRequestForAdd dto);
    History saveHistoryAimBlock(AimBlockRequestForAdd dto);
    History saveHistoryEcoProductionBlock(EcoProductionRequestForAdd dto);
    History saveHistoryProductionBlock(ProductionBlockRequestForAdd dto);
    History saveHistoryNumberBlock(NumberBlockRequestForAdd dto);
    History saveHistoryFactoryBlock(FactoryBlockRequestForAdd dto);
}
