package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.pages.clients.request.ClientBannerRequestForAdd;
import org.example.black_sea_walnut.dto.pages.clients.request.ClientCategoryRequestForAdd;
import org.example.black_sea_walnut.dto.pages.clients.request.ClientEcoProductionRequestForAdd;
import org.example.black_sea_walnut.dto.pages.clients.response.ClientBannerResponseForAdd;
import org.example.black_sea_walnut.dto.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.dto.pages.clients.response.ClientEcoProductionResponseForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;
import org.example.black_sea_walnut.mapper.pages.HistoryClientsMapper;
import org.example.black_sea_walnut.service.ClientCategoryService;
import org.example.black_sea_walnut.service.HistoryClientService;
import org.example.black_sea_walnut.service.HistoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.util.ImageUtil;
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
        return clientsMapper.toResponseBannerBlockForAdd(historyService.getByPageType(type));
    }

    @Override
    public ClientEcoProductionResponseForAdd getByPageTypeInResponseEcoProductionBlock(PageType type) {
        return clientsMapper.toResponseEcoProductionBlockForAdd(historyService.getByPageType(type));
    }

    @Override
    public List<ClientCategoryResponseForAdd> getAllInResponseCategoryBlock() {
        return clientCategoryService.getAllInResponse();
    }


    @SneakyThrows
    @Override
    public History saveHistoryBannerBlock(ClientBannerRequestForAdd dto) {
        if (dto.getClientsBannerId() != null) {
            History historyById = historyService.getById(dto.getClientsBannerId());
            if (dto.getClientsBannerPathToBanner().isEmpty() && historyById.getBanner() != null) {
                imageService.deleteByPath(historyById.getBanner().getPathToMedia());
            }
            dto.setMediaType(ImageUtil.getMediaType(dto.getClientsBannerFile()));
            if (dto.getClientsBannerFile() != null) {
                String generatedPath = contextPath + "/page-clients/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getClientsBannerFile());
                dto.setClientsBannerPathToBanner(generatedPath);
            }
            if (historyById.getBanner() != null) {
                historyById.getBanner().setPathToMedia(dto.getClientsBannerPathToBanner());
                historyById.getBanner().setMediaType(dto.getMediaType());
            }
        }

        imageService.save(dto.getClientsBannerFile(), dto.getClientsBannerPathToBanner());

        History mappedHistory = clientsMapper.toEntityFromRequestBannerBlock(dto);
        return historyService.save(mappedHistory);
    }

    @Override
    public void saveHistoryCategoryBlock(List<ClientCategoryRequestForAdd> dto) {
        if(dto!=null){
            for (ClientCategoryRequestForAdd d : dto) {
                clientCategoryService.save(d);
            }
        }
    }

    @SneakyThrows
    @Override
    public History saveHistoryEcoProductionBlock(ClientEcoProductionRequestForAdd dto) {
        if (dto.getClientsEcoProductionId() != null) {
            History historyById = historyService.getById(dto.getClientsEcoProductionId());
            if (dto.getClientsEcoProductionPathToBanner().isEmpty() && historyById.getBanner() != null) {
                imageService.deleteByPath(historyById.getBanner().getPathToMedia());
            }
            dto.setMediaType(ImageUtil.getMediaType(dto.getClientsEcoProductionFile()));
            if (dto.getClientsEcoProductionFile() != null) {
                String generatedPath = contextPath + "/page-clients/" + dto.getMediaType() + "/" + imageService.generateFileName(dto.getClientsEcoProductionFile());
                dto.setClientsEcoProductionPathToBanner(generatedPath);
            }
            if (historyById.getBanner() != null) {
                historyById.getBanner().setPathToMedia(dto.getClientsEcoProductionPathToBanner());
                historyById.getBanner().setMediaType(dto.getMediaType());
            }
        }

        imageService.save(dto.getClientsEcoProductionFile(), dto.getClientsEcoProductionPathToBanner());

        History mappedHistory = clientsMapper.toEntityFromRequestClientEcoProductionBlock(dto);
        return historyService.save(mappedHistory);
    }

    @Override
    public void deleteById(Long id) {
        clientCategoryService.deleteById(id);
    }
}
