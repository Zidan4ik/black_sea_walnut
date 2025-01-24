package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.dto.pages.factory.request.BannerBlockRequestForAdd;
import org.example.black_sea_walnut.dto.pages.factory.request.BlockRequestForAdd;
import org.example.black_sea_walnut.dto.pages.factory.response.FactoryBannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.factory.response.BlockResponseForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.entity.HistoryMedia;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.HistoryFactoryMapper;
import org.example.black_sea_walnut.service.HistoryFactoryService;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.util.ImageUtil;
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
        return factoryMapper.toResponseBannerBlockForAdd(historyService.getByPageType(type));
    }

    @Override
    public BlockResponseForAdd getByPageTypeInResponseBlock(PageType type) {
        return factoryMapper.toResponseBlockForAdd(historyService.getByPageType(type));
    }

    @SneakyThrows
    @Override
    public History saveHistoryBannerBlock(BannerBlockRequestForAdd dto) {
        if (dto.getFactoryBannerId() != null) {
            History historyById = historyService.getById(dto.getFactoryBannerId());
            if (dto.getFactoryBannerPathToBanner().isEmpty() && historyById.getBanner() != null) {
                imageService.deleteByPath(historyById.getBanner().getPathToMedia());
            }
            dto.setMediaType(ImageUtil.getMediaType(dto.getFactoryBannerFile()));
            if (dto.getFactoryBannerFile() != null) {
                String generatedPath = contextPath + "/page-factory/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getFactoryBannerFile());
                dto.setFactoryBannerPathToBanner(generatedPath);
            }
            if (historyById.getBanner() != null) {
                historyById.getBanner().setPathToMedia(dto.getFactoryBannerPathToBanner());
                historyById.getBanner().setMediaType(dto.getMediaType());
            }
        }
        imageService.save(dto.getFactoryBannerFile(), dto.getFactoryBannerPathToBanner());
        History mappedHistory = factoryMapper.toEntityFromRequestBannerBlock(dto);
        return historyService.save(mappedHistory);
    }

    @SneakyThrows
    @Override
    public History saveHistoryBlock(BlockRequestForAdd dto) {
        if (dto.getFactoryBlockId() != null) {
            History historyById = historyService.getById(dto.getFactoryBlockId());
            if (dto.getFactoryBlockFiles() != null) {
                List<HistoryMedia> mediasFromBd = historyById.getHistoryMedia();
                List<HistoryMedia> mediasToDelete = mediasFromBd.stream().filter(media -> dto.getFactoryBlockFiles().stream().noneMatch(mediaDto -> mediaDto.getId() != null && mediaDto.getId().equals(media.getId())))
                        .toList();
                for (HistoryMedia media : mediasToDelete) {
                    imageService.deleteByPath(media.getPathToImage());
                }

                for (HistoryMediaRequestForAdd mediaDto : dto.getFactoryBlockFiles()) {
                    mediaDto.setMediaType(ImageUtil.getMediaType(mediaDto.getFileImage()));
                    HistoryMedia media = mediasFromBd.stream().filter(m -> m.getId().equals(mediaDto.getId())).findFirst().orElse(null);
                    if (mediaDto.getPathToImage().isEmpty() && media != null) {
                        imageService.deleteByPath(media.getPathToImage());
                    }

                    if (mediaDto.getFileImage() != null) {
                        String generatedPath = contextPath + "/page-files/" + mediaDto.getMediaType() + "/" + imageService.generateFileName(mediaDto.getFileImage());
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
        History mappedHistory = factoryMapper.toEntityFromRequestFactoryBlock(dto);
        return historyService.save(mappedHistory);
    }
}
