package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.request.BannerBlockRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.request.EcologicallyBlockRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.BannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.EcologicallyBlockResponseForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.entity.HistoryMedia;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryCatalogMapper;
import org.example.black_sea_walnut.service.HistoryCatalogService;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.util.ImageUtil;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryCatalogServiceImp implements HistoryCatalogService {
    private final HistoryService historyService;
    private final HistoryCatalogMapper catalogMapper;
    private final ImageService imageService;
    @Value("${upload.path}")
    private String contextPath;

    @Override
    public BannerBlockResponseForAdd getByPageTypeInResponseBannerBlock(PageType type) {
        LogUtil.logInfo("Fetching BannerBlock for page type: " + type);
            BannerBlockResponseForAdd response = catalogMapper.toResponseBannerBlockForAdd(historyService.getByPageType(type));
        LogUtil.logInfo("Fetched BannerBlock: " + response);
        return response;
    }

    @Override
    public EcologicallyBlockResponseForAdd getByPageTypeInResponseEcologicallyBlock(PageType type) {
        LogUtil.logInfo("Fetching EcologicallyBlock for page type: " + type);
        EcologicallyBlockResponseForAdd response = catalogMapper.toResponseEcologicallyBlockForAdd(historyService.getByPageType(type));
        LogUtil.logInfo("Fetched EcologicallyBlock: " + response);
        return response;
    }

    @SneakyThrows
    @Override
    public History saveHistoryBannerBlock(BannerBlockRequestForAdd dto) {
        LogUtil.logInfo("Saving BannerBlock with DTO: " + dto);
        if (dto.getCatalogBannerId() != null) {
            History historyById = historyService.getById(dto.getCatalogBannerId());
            if (dto.getCatalogBannerPathToImage().isEmpty() && historyById.getBanner() != null) {
                LogUtil.logInfo("Deleting previous image for BannerBlock ID: " + dto.getCatalogBannerId());
                imageService.deleteByPath(historyById.getBanner().getPathToMedia());
            }
            dto.setMediaType(ImageUtil.getMediaType(dto.getCatalogBannerFile()));
            if (dto.getCatalogBannerFile() != null) {
                String generatedPath = contextPath + "/page-catalog/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getCatalogBannerFile());
                dto.setCatalogBannerPathToImage(generatedPath);
                LogUtil.logInfo("Generated media file path for BannerBlock: " + generatedPath);
            }
            if (historyById.getBanner() != null) {
                historyById.getBanner().setPathToMedia(dto.getCatalogBannerPathToImage());
                historyById.getBanner().setMediaType(dto.getMediaType());
            }
        }

        imageService.save(dto.getCatalogBannerFile(), dto.getCatalogBannerPathToImage());
        History mappedHistory = catalogMapper.toEntityFromRequestBannerBlock(dto);
        LogUtil.logInfo("Saved BannerBlock with ID: " + mappedHistory.getId());
        return historyService.save(mappedHistory);
    }

    @SneakyThrows
    @Override
    public History saveHistoryEcologicallyBlock(EcologicallyBlockRequestForAdd dto) {
        LogUtil.logInfo("Saving EcologicallyBlock with DTO: " + dto);
        if (dto.getCatalogEcologicallyId() != null) {
            History historyById = historyService.getById(dto.getCatalogEcologicallyId());
            if (dto.getCatalogEcologicallyFiles() != null) {
                List<HistoryMedia> mediasFromBd = historyById.getHistoryMedia();

                List<HistoryMedia> mediasToDelete = mediasFromBd.stream().filter(media -> dto.getCatalogEcologicallyFiles().stream().noneMatch(mediaDto -> mediaDto.getId() != null && mediaDto.getId().equals(media.getId())))
                        .toList();
                for (HistoryMedia media : mediasToDelete) {
                    LogUtil.logInfo("Deleting media file with ID: " + media.getId());
                    imageService.deleteByPath(media.getPathToImage());
                }

                for (HistoryMediaRequestForAdd mediaDto : dto.getCatalogEcologicallyFiles()) {
                    mediaDto.setMediaType(ImageUtil.getMediaType(mediaDto.getFileImage()));
                    HistoryMedia media = mediasFromBd.stream().filter(m -> m.getId().equals(mediaDto.getId())).findFirst().orElse(null);
                    if (mediaDto.getPathToImage().isEmpty() && media != null) {
                        LogUtil.logInfo("Deleting previous image for media ID: " + mediaDto.getId());
                        imageService.deleteByPath(media.getPathToImage());
                    }

                    if (mediaDto.getFileImage() != null) {
                        String generatedPath = contextPath + "/page-catalog/" + mediaDto.getMediaType() + "/" + imageService.generateFileName(mediaDto.getFileImage());
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
        History mappedHistory = catalogMapper.toEntityFromRequestEcologicallyBlock(dto);
        LogUtil.logInfo("Saved EcologicallyBlock with ID: " + mappedHistory.getId());
        return historyService.save(mappedHistory);
    }
}
