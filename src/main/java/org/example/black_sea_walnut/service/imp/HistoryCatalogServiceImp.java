package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.dto.pages.catalog.request.BannerBlockRequestForAdd;
import org.example.black_sea_walnut.dto.pages.catalog.request.EcologicallyBlockRequestForAdd;
import org.example.black_sea_walnut.dto.pages.catalog.response.BannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.catalog.response.EcologicallyBlockResponseForAdd;
import org.example.black_sea_walnut.entity.Banner;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.entity.HistoryMedia;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.HistoryCatalogMapper;
import org.example.black_sea_walnut.service.HistoryCatalogService;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.util.ImageUtil;
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
        return catalogMapper.toResponseBannerBlockForAdd(historyService.getByPageType(type));
    }

    @Override
    public EcologicallyBlockResponseForAdd getByPageTypeInResponseEcologicallyBlock(PageType type) {
        return catalogMapper.toResponseEcologicallyBlockForAdd(historyService.getByPageType(type));
    }

    @SneakyThrows
    @Override
    public History saveHistoryBannerBlock(BannerBlockRequestForAdd dto) {
        if (dto.getCatalogBannerId() != null) {
            History historyById = historyService.getById(dto.getCatalogBannerId());
            if (dto.getCatalogBannerPathToImage().isEmpty() && historyById.getBanner() != null) {
                imageService.deleteByPath(historyById.getBanner().getPathToMedia());
            }
            dto.setMediaType(ImageUtil.getMediaType(dto.getCatalogBannerFile()));
            if (dto.getCatalogBannerFile() != null) {
                String generatedPath = contextPath + "/page-catalog/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getCatalogBannerFile());
                dto.setCatalogBannerPathToImage(generatedPath);
            }
            if (historyById.getBanner() != null) {
                historyById.getBanner().setPathToMedia(dto.getCatalogBannerPathToImage());
                historyById.getBanner().setMediaType(dto.getMediaType());
            }
        }

        imageService.save(dto.getCatalogBannerFile(), dto.getCatalogBannerPathToImage());

        History mappedHistory = catalogMapper.toEntityFromRequestBannerBlock(dto);
        return historyService.save(mappedHistory);
    }

    @SneakyThrows
    @Override
    public History saveHistoryEcologicallyBlock(EcologicallyBlockRequestForAdd dto) {
        if (dto.getCatalogEcologicallyId() != null) {
            History historyById = historyService.getById(dto.getCatalogEcologicallyId());
            if (dto.getCatalogEcologicallyFiles() != null) {
                List<HistoryMedia> mediasFromBd = historyById.getHistoryMedia();

                List<HistoryMedia> mediasToDelete = mediasFromBd.stream().filter(media -> dto.getCatalogEcologicallyFiles().stream().noneMatch(mediaDto -> mediaDto.getId() != null && mediaDto.getId().equals(media.getId())))
                        .toList();
                    for (HistoryMedia media : mediasToDelete) {
                    imageService.deleteByPath(media.getPathToImage());
                }

                for (HistoryMediaRequestForAdd mediaDto : dto.getCatalogEcologicallyFiles()) {
                    mediaDto.setMediaType(ImageUtil.getMediaType(mediaDto.getFileImage()));
                    HistoryMedia media = mediasFromBd.stream().filter(m -> m.getId().equals(mediaDto.getId())).findFirst().orElse(null);
                    if (mediaDto.getPathToImage().isEmpty() && media != null) {
                        imageService.deleteByPath(media.getPathToImage());
                    }

                    if (mediaDto.getFileImage() != null) {
                        String generatedPath = contextPath + "/page-catalog/" + mediaDto.getMediaType() + "/" + imageService.generateFileName(mediaDto.getFileImage());
                        mediaDto.setPathToImage(generatedPath);
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
        return historyService.save(mappedHistory);
    }
}
