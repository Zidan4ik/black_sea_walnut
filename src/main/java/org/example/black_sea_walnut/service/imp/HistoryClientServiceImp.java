package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientBannerRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientCategoryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientEcoProductionRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientBannerResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientEcoProductionResponseForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryClientsMapper;
import org.example.black_sea_walnut.service.ClientCategoryService;
import org.example.black_sea_walnut.service.HistoryClientService;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.util.ImageUtil;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryClientServiceImp implements HistoryClientService {
    private final HistoryService historyService;
    private final HistoryClientsMapper clientsMapper;
    private final ClientCategoryService clientCategoryService;
    private final ImageService imageService;
    @Value("${upload.path}")
    private String contextPath;

    @Override
    public ClientBannerResponseForAdd getByPageTypeInResponseBannerBlock(PageType type) {
        LogUtil.logInfo("Fetching ClientBanner for page type: " + type);
        ClientBannerResponseForAdd response = clientsMapper.toResponseBannerBlockForAdd(historyService.getByPageType(type));
        LogUtil.logInfo("Fetched ClientBanner: " + response);
        return response;
    }

    @Override
    public ClientEcoProductionResponseForAdd getByPageTypeInResponseEcoProductionBlock(PageType type) {
        LogUtil.logInfo("Fetching ClientEcoProduction for page type: " + type);
        ClientEcoProductionResponseForAdd response = clientsMapper.toResponseEcoProductionBlockForAdd(historyService.getByPageType(type));
        LogUtil.logInfo("Fetched ClientEcoProduction: " + response);
        return response;
    }

    @Override
    public List<ClientCategoryResponseForAdd> getAllInResponseCategoryBlock() {
        LogUtil.logInfo("Fetching all ClientCategoryResponseForAdd");
        List<ClientCategoryResponseForAdd> response = clientCategoryService.getAllInResponse();
        LogUtil.logInfo("Fetched ClientCategoryResponseForAdd: " + response);
        return response;
    }

    @SneakyThrows
    @Override
    public History saveHistoryBannerBlock(ClientBannerRequestForAdd dto) {
        LogUtil.logInfo("Saving ClientBanner with DTO: " + dto);
        if (dto.getClientsBannerId() != null) {
            History historyById = historyService.getById(dto.getClientsBannerId());
            if (dto.getClientsBannerPathToBanner().isEmpty() && historyById.getBanner() != null) {
                LogUtil.logInfo("Deleting previous image for ClientBanner ID: " + dto.getClientsBannerId());
                imageService.deleteByPath(historyById.getBanner().getPathToMedia());
            }
            dto.setMediaType(ImageUtil.getMediaType(dto.getClientsBannerFile()));
            if (dto.getClientsBannerFile() != null) {
                String generatedPath = contextPath + "/pages/clients/banner-block/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getClientsBannerFile());
                dto.setClientsBannerPathToBanner(generatedPath);
                LogUtil.logInfo("Generated media file path for ClientBanner: " + generatedPath);
            }
            if (historyById.getBanner() != null) {
                historyById.getBanner().setPathToMedia(dto.getClientsBannerPathToBanner());
                historyById.getBanner().setMediaType(dto.getMediaType());
            }
        }

        imageService.save(dto.getClientsBannerFile(), dto.getClientsBannerPathToBanner());

        History mappedHistory = clientsMapper.toEntityFromRequestBannerBlock(dto);
        LogUtil.logInfo("Saved ClientBanner with ID: " + mappedHistory.getId());
        return historyService.save(mappedHistory);
    }

    @Override
    public void saveHistoryCategoryBlock(List<ClientCategoryRequestForAdd> dto) {
        if (dto != null) {
            LogUtil.logInfo("Saving ClientCategory with DTO: " + dto);
            for (ClientCategoryRequestForAdd d : dto) {
                clientCategoryService.save(d);
            }
        }
    }

    @SneakyThrows
    @Override
    public History saveHistoryEcoProductionBlock(ClientEcoProductionRequestForAdd dto) {
        LogUtil.logInfo("Saving ClientEcoProduction with DTO: " + dto);
        if (dto.getClientsEcoProductionId() != null) {
            History historyById = historyService.getById(dto.getClientsEcoProductionId());
            if (dto.getClientsEcoProductionPathToBanner().isEmpty() && historyById.getBanner() != null) {
                LogUtil.logInfo("Deleting previous image for ClientEcoProduction ID: " + dto.getClientsEcoProductionId());
                imageService.deleteByPath(historyById.getBanner().getPathToMedia());
            }
            dto.setMediaType(ImageUtil.getMediaType(dto.getClientsEcoProductionFile()));
            if (dto.getClientsEcoProductionFile() != null) {
                String generatedPath = contextPath + "/pages/clients/eco-production-block/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getClientsEcoProductionFile());
                dto.setClientsEcoProductionPathToBanner(generatedPath);
                LogUtil.logInfo("Generated media file path for ClientEcoProduction: " + generatedPath);
            }
            if (historyById.getBanner() != null) {
                historyById.getBanner().setPathToMedia(dto.getClientsEcoProductionPathToBanner());
                historyById.getBanner().setMediaType(dto.getMediaType());
            }
        }

        imageService.save(dto.getClientsEcoProductionFile(), dto.getClientsEcoProductionPathToBanner());

        History mappedHistory = clientsMapper.toEntityFromRequestClientEcoProductionBlock(dto);
        LogUtil.logInfo("Saved ClientEcoProduction with ID: " + mappedHistory.getId());
        return historyService.save(mappedHistory);
    }

    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting ClientCategory with ID: " + id);
        clientCategoryService.deleteById(id);
    }
}
