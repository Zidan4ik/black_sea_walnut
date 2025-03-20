package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.pages.main.request.*;
import org.example.black_sea_walnut.dto.admin.pages.main.response.*;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryMainMapper;
import org.example.black_sea_walnut.service.HistoryMainService;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.util.ImageUtil;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class HistoryMainServiceImp implements HistoryMainService {
    private final HistoryMainMapper historyMainMapper;
    private final HistoryService historyService;

    @Override
    public BlockResponseForAddInMain getByPageTypeInResponseMainBlock(PageType type) {
        LogUtil.logInfo("Fetching Main Block by PageType: " + type);
        return historyMainMapper.toResponseMainBlockForAdd(historyService.getByPageType(type));
    }

    @Override
    public ProductionResponseForAddInMain getByPageTypeInResponseProductionBlock(PageType type) {
        LogUtil.logInfo("Fetching Production Block by PageType: " + type);
        return historyMainMapper.toResponseProductionBlockForAdd(historyService.getByPageType(type));
    }

    @Override
    public FactoryBlockResponseForAddInMain getByPageTypeInResponseFactoryBlock(PageType type) {
        LogUtil.logInfo("Fetching Factory Block by PageType: " + type);
        return historyMainMapper.toResponseFactoryBlockForAdd(historyService.getByPageType(type));
    }

    @Override
    public NumberBlockResponseForAddInMain getByPageTypeInResponseNumberBlock(PageType type) {
        LogUtil.logInfo("Fetching Number Block by PageType: " + type);
        return historyMainMapper.toResponseNumberBlockForAdd(historyService.getByPageType(type));
    }

    @Override
    public AimBlockResponseForAddInMain getByPageTypeInResponseAimBlock(PageType type) {
        LogUtil.logInfo("Fetching Aim Block by PageType: " + type);
        return historyMainMapper.toResponseAimBlockForAdd(historyService.getByPageType(type));
    }

    @Override
    public EcoProductionResponseForAddInMain getByPageTypeInResponseEcoProductionBlock(PageType type) {
        LogUtil.logInfo("Fetching EcoProduction Block by PageType: " + type);
        return historyMainMapper.toResponseEcoProductionBLockForAdd(historyService.getByPageType(type));
    }

    @SneakyThrows
    @Override
    public History saveHistoryMainBlock(MainBlockRequestForAdd dto) {
        LogUtil.logInfo("Saving Main Block: " + dto);
        try {
            dto.setMediaType(ImageUtil.getMediaType(dto.getMainFileBanner()));
            History mappedHistory = historyMainMapper.toEntityFromRequestForAdd(dto);
            History savedHistory = historyService.save(mappedHistory);
            LogUtil.logInfo("Successfully saved Main Block with ID: " + savedHistory.getId());
            return savedHistory;
        } catch (Exception e) {
            LogUtil.logError("Error saving Main Block", e);
            throw e;
        }
    }

    @SneakyThrows
    @Override
    public History saveHistoryAimBlock(AimBlockRequestForAdd dto) {
        LogUtil.logInfo("Saving Aim Block: " + dto);
        try {
            dto.setMediaType(ImageUtil.getMediaType(dto.getMainAimFileBanner()));
            History mappedHistory = historyMainMapper.toEntityFromRequestForAdd(dto);
            History savedHistory = historyService.save(mappedHistory);
            LogUtil.logInfo("Successfully saved Aim Block with ID: " + savedHistory.getId());
            return savedHistory;
        } catch (Exception e) {
            LogUtil.logError("Error saving Aim Block", e);
            throw e;
        }
    }

    @SneakyThrows
    @Override
    public History saveHistoryEcoProductionBlock(EcoProductionRequestForAdd dto) {
        LogUtil.logInfo("Saving EcoProduction Block: " + dto);
        try {
            dto.setMediaType(ImageUtil.getMediaType(dto.getMainEcoProductionFileBanner()));
            History mappedHistory = historyMainMapper.toEntityFromRequestForAdd(dto);
            History savedHistory = historyService.save(mappedHistory);
            LogUtil.logInfo("Successfully saved EcoProduction Block with ID: " + savedHistory.getId());
            return savedHistory;
        } catch (Exception e) {
            LogUtil.logError("Error saving EcoProduction Block", e);
            throw e;
        }
    }

    @Override
    public History saveHistoryProductionBlock(ProductionBlockRequestForAdd dto) {
        LogUtil.logInfo("Saving Production Block: " + dto);
        return historyService.save(historyMainMapper.toEntityFromRequestForAdd(dto));
    }

    @Override
    public History saveHistoryNumberBlock(NumberBlockRequestForAdd dto) {
        LogUtil.logInfo("Saving Number Block: " + dto);
        return historyService.save(historyMainMapper.toEntityFromRequestForAdd(dto));
    }

    @SneakyThrows
    @Override
    public History saveHistoryFactoryBlock(FactoryBlockRequestForAdd dto) {
        LogUtil.logInfo("Saving Factory Block: " + dto);
        try {
            History mappedHistory = historyMainMapper.toEntityFromRequestForAdd(dto);
            History savedHistory = historyService.save(mappedHistory);
            LogUtil.logInfo("Successfully saved Factory Block with ID: " + savedHistory.getId());
            return savedHistory;
        } catch (Exception e) {
            LogUtil.logError("Error saving Factory Block", e);
            throw e;
        }
    }
}
