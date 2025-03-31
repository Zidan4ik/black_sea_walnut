package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.main.request.*;
import org.example.black_sea_walnut.dto.admin.pages.main.response.*;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.entity.HistoryMedia;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryMainMapper;
import org.example.black_sea_walnut.service.HistoryMainService;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.util.ImageUtil;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class HistoryMainServiceImp implements HistoryMainService {
    private final HistoryMainMapper historyMainMapper;
    private final HistoryService historyService;
    private final ImageService imageService;
    @Value("${upload.path}")
    private String uploadPath;

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
            if (dto.getMainId() != null) {
                History historyById = historyService.getById(dto.getMainId());
                if (dto.getMainPathToBanner().isEmpty() && historyById.getBanner() != null && !historyById.getBanner().getPathToMedia().isEmpty()) {
                    LogUtil.logInfo("Deleting previous image for FactoryBanner ID: " + dto.getMainId());
                    imageService.deleteByPath(historyById.getBanner().getPathToMedia());
                }
                dto.setMediaType(ImageUtil.getMediaType(dto.getMainFileBanner()));
                if (dto.getMainFileBanner() != null) {
                    String generatedPath = uploadPath + "/pages/main/main-block/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getMainFileBanner());
                    dto.setMainPathToBanner(generatedPath);
                    LogUtil.logInfo("Generated media file path for FactoryBanner: " + generatedPath);
                }
                if (historyById.getBanner() != null) {
                    historyById.getBanner().setPathToMedia(dto.getMainPathToBanner());
                    historyById.getBanner().setMediaType(dto.getMediaType());
                }
            }
            dto.setMediaType(ImageUtil.getMediaType(dto.getMainFileBanner()));
            imageService.save(dto.getMainFileBanner(), dto.getMainPathToBanner());
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
            if (dto.getMainAimId() != null) {
                History historyById = historyService.getById(dto.getMainAimId());
                if (dto.getMainAimPathToBanner().isEmpty() && historyById.getBanner() != null && !historyById.getBanner().getPathToMedia().isEmpty()) {
                    LogUtil.logInfo("Deleting previous image for FactoryBanner ID: " + dto.getMainAimId());
                    imageService.deleteByPath(historyById.getBanner().getPathToMedia());
                }
                dto.setMediaType(ImageUtil.getMediaType(dto.getMainAimFileBanner()));
                if (dto.getMainAimFileBanner() != null) {
                    String generatedPath = uploadPath + "/pages/main/aim-block/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getMainAimFileBanner());
                    dto.setMainAimPathToBanner(generatedPath);
                    LogUtil.logInfo("Generated media file path for FactoryBanner: " + generatedPath);
                }
                if (historyById.getBanner() != null) {
                    historyById.getBanner().setPathToMedia(dto.getMainAimPathToBanner());
                    historyById.getBanner().setMediaType(dto.getMediaType());
                }
            }
            imageService.save(dto.getMainAimFileBanner(), dto.getMainAimPathToBanner());
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
            if (dto.getMainEcoProductionId() != null) {
                History historyById = historyService.getById(dto.getMainEcoProductionId());
                if (dto.getMainEcoProductionFileBanner() != null && historyById.getBanner() != null && !historyById.getBanner().getPathToMedia().isEmpty()) {
                    LogUtil.logInfo("Deleting previous image for FactoryBanner ID: " + dto.getMainEcoProductionId());
                    imageService.deleteByPath(historyById.getBanner().getPathToMedia());
                }
                dto.setMediaType(ImageUtil.getMediaType(dto.getMainEcoProductionFileBanner()));
                if (dto.getMainEcoProductionFileBanner() != null) {
                    String generatedPath = uploadPath + "/pages/main/eco-production-block/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getMainEcoProductionFileBanner());
                    dto.setMainEcoProductionPathToBanner(generatedPath);
                    LogUtil.logInfo("Generated media file path for FactoryBanner: " + generatedPath);
                }
                if (historyById.getBanner() != null) {
                    historyById.getBanner().setPathToMedia(dto.getMainEcoProductionPathToBanner());
                    historyById.getBanner().setMediaType(dto.getMediaType());
                }
            }
            imageService.save(dto.getMainEcoProductionFileBanner(), dto.getMainEcoProductionPathToBanner());
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
        LogUtil.logInfo("Saving factories images: " + dto);
        try {
            if (dto.getMainFactoryId() != null) {
                History historyById = historyService.getById(dto.getMainFactoryId());
                if (dto.getFiles() != null) {
                    List<HistoryMedia> mediasFromBd = historyById.getHistoryMedia();

                    List<HistoryMedia> mediasToDelete = mediasFromBd.stream().filter(
                                    media -> dto.getFiles().stream().noneMatch(
                                            mediaDto -> mediaDto.getPathToImage().equals(media.getPathToImage())))
                            .toList();
                    for (HistoryMedia media : mediasToDelete) {
                        LogUtil.logInfo("Deleting media image for main factory block id: " + dto.getMainFactoryId() + " with media ID: " + media.getId());
                        imageService.deleteByPath(media.getPathToImage());
                    }

                    for (HistoryMediaRequestForAdd mediaDto : dto.getFiles()) {
                        mediaDto.setMediaType(ImageUtil.getMediaType(mediaDto.getFileImage()));
                        if (mediaDto.getFileImage() != null) {
                            String generatedPath = uploadPath + "/pages/main/factory-images/" + imageService.generateFileName(mediaDto.getFileImage());
                            mediaDto.setPathToImage(generatedPath);
                            LogUtil.logInfo("Generated media file path for media ID: " + mediaDto.getId() + ": " + generatedPath);
                        }
                        imageService.save(mediaDto.getFileImage(), mediaDto.getPathToImage());
                    }
                } else {
                    historyById.getHistoryMedia().clear();
                    imageService.deleteByPath("/uploads/pages/main/factory-images");
                }
            }

            History mappedHistory = historyMainMapper.toEntityFromRequestForAdd(dto);
            History savedHistory = historyService.save(mappedHistory);
            LogUtil.logInfo("Successfully saved factories images with ID: " + savedHistory.getId());
            return savedHistory;
        } catch (Exception e) {
            LogUtil.logError("Error saving factories images", e);
            throw e;
        }
    }
}
