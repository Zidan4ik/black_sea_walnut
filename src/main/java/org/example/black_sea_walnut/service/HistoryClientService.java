package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientBannerRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientCategoryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientEcoProductionRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientBannerResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientEcoProductionResponseForAdd;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.PageType;

import java.util.List;

public interface HistoryClientService {
    ClientBannerResponseForAdd getByPageTypeInResponseBannerBlock(PageType type);

    ClientEcoProductionResponseForAdd getByPageTypeInResponseEcoProductionBlock(PageType type);

    List<ClientCategoryResponseForAdd> getAllInResponseCategoryBlock();

    History saveHistoryBannerBlock(ClientBannerRequestForAdd dto);

    void saveHistoryCategoryBlock(List<ClientCategoryRequestForAdd> dto);

    History saveHistoryEcoProductionBlock(ClientEcoProductionRequestForAdd dto);

    void deleteById(Long id);
}
