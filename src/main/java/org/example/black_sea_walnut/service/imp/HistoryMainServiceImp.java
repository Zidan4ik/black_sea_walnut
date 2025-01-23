package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.historyMedia.HistoryMediaRequestForAdd;
import org.example.black_sea_walnut.dto.pages.main.request.*;
import org.example.black_sea_walnut.dto.pages.main.response.*;
import org.example.black_sea_walnut.entity.Banner;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.entity.HistoryMedia;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.HistoryMainMapper;
import org.example.black_sea_walnut.repository.HistoryRepository;
import org.example.black_sea_walnut.service.HistoryMainService;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.util.ImageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryMainServiceImp implements HistoryMainService {
    private final HistoryRepository historyRepository;
    private final HistoryMainMapper historyMainMapper;
    private final HistoryService historyService;
    private final ImageService imageService;
    @Value("${upload.path}")
    private String contextPath;

    @Override
    public MainBlockResponseForAdd getByPageTypeInResponseMainBlock(PageType type) {
        return historyMainMapper.toResponseMainBlockForAdd(historyService.getByPageType(type));
    }

    @Override
    public ProductionBlockResponseForAdd getByPageTypeInResponseProductionBlock(PageType type) {
        return historyMainMapper.toResponseProductionBlockForAdd(historyService.getByPageType(type));

    }

    @Override
    public FactoryBlockResponseForAdd getByPageTypeInResponseFactoryBlock(PageType type) {
        return historyMainMapper.toResponseFactoryBlockForAdd(historyService.getByPageType(type));
    }

    @Override
    public NumberBlockResponseForAdd getByPageTypeInResponseNumberBlock(PageType type) {
        return historyMainMapper.toResponseNumberBlockForAdd(historyService.getByPageType(type));
    }

    @Override
    public AimBlockResponseForAdd getByPageTypeInResponseAimBlock(PageType type) {
        return historyMainMapper.toResponseAimBlockForAdd(historyService.getByPageType(type));
    }

    @Override
    public EcoProductionResponseForAdd getByPageTypeInResponseEcoProductionBlock(PageType type) {
        return historyMainMapper.toResponseEcoProductionBLockForAdd(historyService.getByPageType(type));
    }

    @SneakyThrows
    @Override
    public History saveHistoryMainBlock(MainBlockRequestForAdd dto) {
        dto.setMediaType(ImageUtil.getMediaType(dto.getMainFileBanner()));
        if (dto.getMainId() != null) {
            History historyById = historyService.getById(dto.getMainId());
            if (dto.getMainPathToBanner().isEmpty() && historyById.getBanner() != null) {
                imageService.deleteByPath(historyById.getBanner().getPathToMedia());
            }

            if (dto.getMainFileBanner() != null) {
                String generatedPath = contextPath + "/page-main/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getMainFileBanner());
                dto.setMainPathToBanner(generatedPath);
            }

            if (historyById.getBanner() != null) {
                historyById.getBanner().setPathToMedia(dto.getMainPathToBanner());
                historyById.getBanner().setMediaType(dto.getMediaType());
            }
        }

        imageService.save(dto.getMainFileBanner(), dto.getMainPathToBanner());

        History mappedHistory = historyMainMapper.toEntityFromRequestForAdd(dto);
        if (dto.getMainId() == null)
            mappedHistory.setBanner(new Banner(null, dto.getMainPathToBanner(), dto.getMediaType(), mappedHistory));
        return historyService.save(mappedHistory);
    }

    @SneakyThrows
    @Override
    public History saveHistoryAimBlock(AimBlockRequestForAdd dto) {
        dto.setMediaType(ImageUtil.getMediaType(dto.getMainAimFileBanner()));
        if (dto.getMainAimId() != null) {
            History historyById = historyService.getById(dto.getMainAimId());
            if (dto.getMainAimPathToBanner().isEmpty() && historyById.getBanner() != null) {
                imageService.deleteByPath(historyById.getBanner().getPathToMedia());
            }

            if (dto.getMainAimFileBanner() != null) {
                String generatedPath = contextPath + "/page-main/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getMainAimFileBanner());
                dto.setMainAimPathToBanner(generatedPath);
            }

            if (historyById.getBanner() != null) {
                historyById.getBanner().setPathToMedia(dto.getMainAimPathToBanner());
                historyById.getBanner().setMediaType(dto.getMediaType());
            }
        }

        imageService.save(dto.getMainAimFileBanner(), dto.getMainAimPathToBanner());

        History mappedHistory = historyMainMapper.toEntityFromRequestForAdd(dto);
        if (dto.getMainAimId() == null)
            mappedHistory.setBanner(new Banner(null, dto.getMainAimPathToBanner(), dto.getMediaType(), mappedHistory));
        return historyService.save(mappedHistory);
    }

    @SneakyThrows
    @Override
    public History saveHistoryEcoProductionBlock(EcoProductionRequestForAdd dto) {
        dto.setMediaType(ImageUtil.getMediaType(dto.getMainEcoProductionFileBanner()));
        if (dto.getMainEcoProductionId() != null) {
            History historyById = historyService.getById(dto.getMainEcoProductionId());
            if (dto.getMainEcoProductionPathToBanner().isEmpty() && historyById.getBanner() != null) {
                imageService.deleteByPath(historyById.getBanner().getPathToMedia());
            }

            if (dto.getMainEcoProductionFileBanner() != null) {
                String generatedPath = contextPath + "/page-main/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getMainEcoProductionFileBanner());
                dto.setMainEcoProductionPathToBanner(generatedPath);
            }

            if (historyById.getBanner() != null) {
                historyById.getBanner().setPathToMedia(dto.getMainEcoProductionPathToBanner());
                historyById.getBanner().setMediaType(dto.getMediaType());
            }
        }

        imageService.save(dto.getMainEcoProductionFileBanner(), dto.getMainEcoProductionPathToBanner());

        History mappedHistory = historyMainMapper.toEntityFromRequestForAdd(dto);
        if (dto.getMainEcoProductionId() == null)
            mappedHistory.setBanner(new Banner(null, dto.getMainEcoProductionPathToBanner(), dto.getMediaType(), mappedHistory));
        return historyService.save(mappedHistory);
    }

    @Override
    public History saveHistoryProductionBlock(ProductionBlockRequestForAdd dto) {
        return historyService.save(historyMainMapper.toEntityFromRequestForAdd(dto));
    }

    @Override
    public History saveHistoryNumberBlock(NumberBlockRequestForAdd dto) {
        return historyService.save(historyMainMapper.toEntityFromRequestForAdd(dto));
    }

    @SneakyThrows
    @Override
    public History saveHistoryFactoryBlock(FactoryBlockRequestForAdd dto) {
        if (dto.getMainFactoryId() != null) {
            History historyById = historyService.getById(dto.getMainFactoryId());
            if (dto.getFiles() != null) {
                List<HistoryMedia> mediasFromBd = historyById.getHistoryMedia();

                List<HistoryMedia> mediasToDelete = mediasFromBd.stream().filter(media -> dto.getFiles().stream().noneMatch(mediaDto -> mediaDto.getId() != null && mediaDto.getId().equals(media.getId())))
                        .toList();
                for (HistoryMedia media:mediasToDelete){
                    imageService.deleteByPath(media.getPathToImage());
                }

                for (HistoryMediaRequestForAdd mediaDto : dto.getFiles()) {
                    mediaDto.setMediaType(ImageUtil.getMediaType(mediaDto.getFileImage()));
                    HistoryMedia media = mediasFromBd.stream().filter(m -> m.getId().equals(mediaDto.getId())).findFirst().orElse(null);
                    if (mediaDto.getPathToImage().isEmpty() && media != null) {
                        imageService.deleteByPath(media.getPathToImage());
                    }

                    if (mediaDto.getFileImage() != null) {
                        String generatedPath = contextPath + "/page-main/" + mediaDto.getMediaType() + "/" + imageService.generateFileName(mediaDto.getFileImage());
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
        History mappedHistory = historyMainMapper.toEntityFromRequestForAdd(dto);
        return historyService.save(mappedHistory);
    }
}
