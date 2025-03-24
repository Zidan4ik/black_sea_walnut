package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.request.BannerBlockRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.request.BlockRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.response.FactoryBannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.response.BlockResponseForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.entity.HistoryMedia;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryFactoryMapper;
import org.example.black_sea_walnut.service.HistoryFactoryService;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.util.ImageUtil;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryFactoryServiceImp implements HistoryFactoryService {
    private final HistoryService historyService;
    private final HistoryFactoryMapper factoryMapper;
    private final ImageService imageService;
    @Value("${upload.path}")
    private String contextPath;

    @Override
    public FactoryBannerBlockResponseForAdd getByPageTypeInResponseBannerBlock(PageType type) {
        LogUtil.logInfo("Fetching FactoryBanner for page type: " + type);
        FactoryBannerBlockResponseForAdd response = factoryMapper.toResponseBannerBlockForAdd(historyService.getByPageType(type));
        LogUtil.logInfo("Fetched FactoryBanner: " + response);
        return response;
    }

    @Override
    public BlockResponseForAdd getByPageTypeInResponseBlock(PageType type) {
        LogUtil.logInfo("Fetching FactoryBlock for page type: " + type);
        BlockResponseForAdd response = factoryMapper.toResponseBlockForAdd(historyService.getByPageType(type));
        LogUtil.logInfo("Fetched FactoryBlock: " + response);
        return response;
    }

    @SneakyThrows
    @Override
    public History saveHistoryBannerBlock(BannerBlockRequestForAdd dto) {
        LogUtil.logInfo("Saving FactoryBanner with DTO: " + dto);
        if (dto.getFactoryBannerId() != null) {
            History historyById = historyService.getById(dto.getFactoryBannerId());
            if (dto.getFactoryBannerPathToBanner().isEmpty() && historyById.getBanner() != null) {
                LogUtil.logInfo("Deleting previous image for FactoryBanner ID: " + dto.getFactoryBannerId());
                imageService.deleteByPath(historyById.getBanner().getPathToMedia());
            }
            dto.setMediaType(ImageUtil.getMediaType(dto.getFactoryBannerFile()));
            if (dto.getFactoryBannerFile() != null) {
                String generatedPath = contextPath + "/page-factory/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getFactoryBannerFile());
                dto.setFactoryBannerPathToBanner(generatedPath);
                LogUtil.logInfo("Generated media file path for FactoryBanner: " + generatedPath);
            }
            if (historyById.getBanner() != null) {
                historyById.getBanner().setPathToMedia(dto.getFactoryBannerPathToBanner());
                historyById.getBanner().setMediaType(dto.getMediaType());
            }
        }
        imageService.save(dto.getFactoryBannerFile(), dto.getFactoryBannerPathToBanner());
        History mappedHistory = factoryMapper.toEntityFromRequestBannerBlock(dto);
        LogUtil.logInfo("Saved FactoryBanner with ID: " + mappedHistory.getId());
        return historyService.save(mappedHistory);
    }

    @SneakyThrows
    @Override
    public History saveHistoryBlock(BlockRequestForAdd dto) {
        LogUtil.logInfo("Saving FactoryBlock with DTO: " + dto);
        if (dto.getFactoryBlockId() != null) {
            History historyById = historyService.getById(dto.getFactoryBlockId());
            if (dto.getFactoryBlockFiles() != null) {
                List<HistoryMedia> mediasFromBd = historyById.getHistoryMedia();
                List<HistoryMedia> mediasToDelete = mediasFromBd.stream().filter(
                        media -> dto.getFactoryBlockFiles().stream().noneMatch(
                                mediaDto -> mediaDto.getId() != null &&
                                        mediaDto.getId().equals(media.getId())))
                        .toList();
                for (HistoryMedia media : mediasToDelete) {
                    LogUtil.logInfo("Deleting media image for FactoryBlock ID: " + dto.getFactoryBlockId() + " with media ID: " + media.getId());
                    imageService.deleteByPath(media.getPathToImage());
                }

                for (HistoryMediaRequestForAdd mediaDto : dto.getFactoryBlockFiles()) {
                    mediaDto.setMediaType(ImageUtil.getMediaType(mediaDto.getFileImage()));
                    HistoryMedia media = mediasFromBd.stream().filter(m -> m.getId().equals(mediaDto.getId())).findFirst().orElse(null);
                    if (mediaDto.getPathToImage().isEmpty() && media != null) {
                        LogUtil.logInfo("Deleting previous media image for media ID: " + media.getId());
                        imageService.deleteByPath(media.getPathToImage());
                    }

                    if (mediaDto.getFileImage() != null) {
                        String generatedPath = contextPath + "/page-files/" + mediaDto.getMediaType() + "/" + imageService.generateFileName(mediaDto.getFileImage());
                        mediaDto.setPathToImage(generatedPath);
                        LogUtil.logInfo("Generated media file path for media ID: " + mediaDto.getId() + ": " + generatedPath);
                    }

                    if (media != null) {
                        media.setPathToImage(mediaDto.getPathToImage());
                        media.setMediaType(mediaDto.getMediaType());
                    }
                    imageService.save(mediaDto.getFileImage(), mediaDto.getPathToImage());
                }
            }
        }
        History mappedHistory = factoryMapper.toEntityFromRequestFactoryBlock(dto);
        LogUtil.logInfo("Saved FactoryBlock with ID: " + mappedHistory.getId());
        return historyService.save(mappedHistory);
    }
}
