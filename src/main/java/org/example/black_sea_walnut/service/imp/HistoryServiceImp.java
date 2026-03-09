package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.entity.Banner;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.repository.HistoryRepository;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.history.DtoResponse;
import org.example.black_sea_walnut.service.history.HistoryService;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryServiceImp implements HistoryService {
    private final HistoryRepository historyRepository;
    private final ImageService imageService;

    @Override
    public History getById(Long id) {
        LogUtil.logInfo("Fetching history by ID: " + id);
        return historyRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = "History with id: " + id + " was not found!";
                    LogUtil.logError(errorMessage, null);
                    return new EntityNotFoundException(errorMessage);
                });
    }

    @Override
    public History getByPageType(PageType type) {
        LogUtil.logInfo("Fetching history by PageType: " + type);
        return historyRepository.findByPageType(type)
                .orElseThrow(() -> {
                    String errorMessage = "History with type: " + type + " was not found!";
                    LogUtil.logError(errorMessage, null);
                    return new EntityNotFoundException(errorMessage);
                });
    }

    @Override
    public <R extends DtoResponse> R getResponseByPageType(PageType type, Function<History, R> mappingFunction) {
        LogUtil.logInfo("Fetching and mapping history for PageType: " + type);
        History entity = getByPageType(type);
        R response = mappingFunction.apply(entity);
        LogUtil.logInfo("Successfully returned mapped response for type: " + type);
        return response;
    }

    @Override
    public History save(History entity) {
        LogUtil.logInfo("Saving history: " + entity);
        return historyRepository.save(entity);
    }

    @Override
    public List<History> getAll() {
        LogUtil.logInfo("Fetching all history records");
        return historyRepository.findAll();
    }

    @Override
    public void handleBannerUpdate(History entity, FileProcessable fileDto, Uploadable u) throws IOException {
        if (entity.getBanner() == null) {
            entity.setBanner(new Banner());
            entity.getBanner().setHistory(entity);
        }

        if (fileDto.getPathToImage().isEmpty() &&
                !entity.getBanner().getPathToMedia().isEmpty()) {
            safeDelete(entity.getBanner().getPathToMedia());
        }

        String path = (fileDto.getPathToImage() != null) ? fileDto.getPathToImage() : "";
        if (isNewImageProvided(fileDto)) {
            path = imageService.generatePath(fileDto.getFileImage(), u);
            imageService.save(fileDto.getFileImage(), path);
        }
        entity.getBanner().setPathToMedia(path);
    }

    @Override
    public History getOrCreate(Long id) {
        return (id != null) ? getById(id) : new History();
    }

    @Override
    @SneakyThrows
    public <M extends GenericsMapper> History saveHistory(Saveable<History, M> dto, M mapper) {
        History entity = getOrCreate(dto.getId());

        if (dto instanceof FileProcessable fileDto) {
            handleBannerUpdate(entity, fileDto, (Uploadable) dto);
        }

        if (dto instanceof HistoryFileRequest filesDto && dto instanceof Uploadable u) {
            handleFileSynchronization(filesDto, entity, u);
        }

        dto.updateEntity(entity, mapper);
        return save(entity);
    }


    @Override
    public void handleFileSynchronization(HistoryFileRequest dto, History entity, Uploadable sub) {
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

    @Override
    public void safeDelete(String path) {
        try {
            if (path != null && !path.isEmpty()) {
                imageService.deleteByPath(path);
            }
        } catch (IOException e) {
            LogUtil.logError("Failed to delete image at path: " + path, e);
        }
    }

    @Override
    public boolean isNewImageProvided(FileProcessable dto) {
        return dto.getFileImage() != null && !dto.getFileImage().isEmpty();
    }
}
