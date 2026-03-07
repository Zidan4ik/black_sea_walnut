package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.main.request.*;
import org.example.black_sea_walnut.dto.admin.pages.main.response.*;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryMainMapper;
import org.example.black_sea_walnut.service.HistoryMainService;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.file.FileProcessable;
import org.example.black_sea_walnut.service.history.HistoryFileRequest;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class HistoryMainServiceImp implements HistoryMainService {
    private final HistoryMainMapper historyMainMapper;
    private final HistoryService historyService;
    private final ImageService imageService;

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

    @Override
    @SneakyThrows
    public History saveHistory(Saveable<History, HistoryMainMapper> dto) {
        History entity = (dto.getId() != null)
                ? historyService.getById(dto.getId())
                : new History();

        if (dto instanceof FileProcessable fileDto && isNewImageProvided(fileDto)) {
            if (entity.getBanner() != null && entity.getBanner().getPathToMedia() != null) {
                imageService.deleteByPath(entity.getBanner().getPathToMedia());
            }

            if (dto instanceof Uploadable u) {
                String path = imageService.generatePath(fileDto.getFileImage(), u);
                entity.getBanner().setPathToMedia(path);
                imageService.save(fileDto.getFileImage(), path);
            }
        }

        if (dto instanceof HistoryFileRequest filesDto && dto instanceof Uploadable u) {
            handleFileSynchronization(filesDto, entity, u);
        }

        dto.updateEntity(entity, historyMainMapper);
        return historyService.save(entity);
    }

    private void handleFileSynchronization(HistoryFileRequest dto, History entity, Uploadable sub) {
        List<HistoryMediaRequestForAdd> newFiles = dto.getFiles();

        if (entity.getHistoryMedia() == null) {
            entity.setHistoryMedia(new ArrayList<>());
        }

        if (newFiles == null || newFiles.isEmpty()) {
            if (!entity.getHistoryMedia().isEmpty()) {
                entity.getHistoryMedia().forEach(old ->
                        safeDelete(old.getPathToImage())
                );
                entity.getHistoryMedia().clear();
            }
            return;
        }

        Set<String> pathsInDto = newFiles.stream()
                .map(HistoryMediaRequestForAdd::getPathToImage)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        entity.getHistoryMedia().removeIf(old -> {
            if (!pathsInDto.contains(old.getPathToImage())) {
                safeDelete(old.getPathToImage());
                return true;
            }
            return false;
        });


        for (HistoryMediaRequestForAdd mediaDto : newFiles) {
            if (mediaDto.getFileImage() != null && !mediaDto.getFileImage().isEmpty()) {
                String generatedPath = imageService.generatePath(mediaDto.getFileImage(), sub);
                mediaDto.setPathToImage(generatedPath);
                imageService.save(mediaDto.getFileImage(), generatedPath);
            }
        }
    }

    private void safeDelete(String path) {
        try {
            imageService.deleteByPath(path);
        } catch (IOException e) {
//            LogUtil.logError("Failed to delete image: {}", e);
        }
    }

    private boolean isNewImageProvided(FileProcessable dto) {
        return dto.getFileImage() != null && !dto.getFileImage().isEmpty();
    }
}
