package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.BannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.EcologicallyBlockResponseForAdd;
import org.example.black_sea_walnut.entity.Banner;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryCatalogMapper;
import org.example.black_sea_walnut.mapper.pages.HistoryMainMapper;
import org.example.black_sea_walnut.service.HistoryCatalogService;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.file.FileProcessable;
import org.example.black_sea_walnut.service.history.HistoryFileRequest;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryCatalogServiceImp implements HistoryCatalogService {
    private final HistoryService historyService;
    private final HistoryCatalogMapper historyCatalogMapper;
    private final ImageService imageService;
    @Value("${upload.path}")
    private String contextPath;

    @Override
    public BannerBlockResponseForAdd getByPageTypeInResponseBannerBlock(PageType type) {
        LogUtil.logInfo("Fetching BannerBlock for page type: " + type);
        BannerBlockResponseForAdd response = historyCatalogMapper.toResponseBannerBlockForAdd(historyService.getByPageType(type));
        LogUtil.logInfo("Fetched BannerBlock: " + response);
        return response;
    }

    @Override
    public EcologicallyBlockResponseForAdd getByPageTypeInResponseEcologicallyBlock(PageType type) {
        LogUtil.logInfo("Fetching EcologicallyBlock for page type: " + type);
        EcologicallyBlockResponseForAdd response = historyCatalogMapper.toResponseEcologicallyBlockForAdd(historyService.getByPageType(type));
        LogUtil.logInfo("Fetched EcologicallyBlock: " + response);
        return response;
    }

    @Override
    @Transactional
    @SneakyThrows
    public History saveHistory(Saveable<History, HistoryCatalogMapper> dto) {
        History entity = (dto.getId() != null)
                ? historyService.getById(dto.getId())
                : new History();

        if (dto instanceof FileProcessable fileDto && isNewImageProvided(fileDto)) {
            handleBannerUpdate(entity, fileDto, (Uploadable) dto);
        }

        if (dto instanceof HistoryFileRequest filesDto && dto instanceof Uploadable u) {
            handleFileSynchronization(filesDto, entity, u);
        }

        dto.updateEntity(entity, historyCatalogMapper);

        return historyService.save(entity);
    }

    private void handleBannerUpdate(History entity, FileProcessable fileDto, Uploadable u) throws IOException {
        if (entity.getBanner() == null) {
            entity.setBanner(new Banner());
            entity.getBanner().setHistory(entity);
        }

        if (entity.getBanner().getPathToMedia() != null) {
            safeDelete(entity.getBanner().getPathToMedia());
        }

        String path = imageService.generatePath(fileDto.getFileImage(), u);
        entity.getBanner().setPathToMedia(path);
        imageService.save(fileDto.getFileImage(), path);
    }

    private void handleFileSynchronization(HistoryFileRequest dto, History entity, Uploadable sub) {
        if (entity.getHistoryMedia() == null) {
            entity.setHistoryMedia(new ArrayList<>());
        }

        List<HistoryMediaRequestForAdd> newFiles = dto.getFiles();

        if (newFiles == null || newFiles.isEmpty()) {
            entity.getHistoryMedia().forEach(old -> safeDelete(old.getPathToImage()));
            entity.getHistoryMedia().clear();
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
            if (path != null && !path.isEmpty()) {
                imageService.deleteByPath(path);
            }
        } catch (IOException e) {
            LogUtil.logError("Failed to delete image at path: " + path, e);
        }
    }

    private boolean isNewImageProvided(FileProcessable dto) {
        return dto.getFileImage() != null && !dto.getFileImage().isEmpty();
    }
}