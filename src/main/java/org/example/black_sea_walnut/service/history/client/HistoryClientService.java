package org.example.black_sea_walnut.service.history.client;

import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientCategoryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientCategoryResponseForAdd;

import java.util.List;

public interface HistoryClientService {

    List<ClientCategoryResponseForAdd> getAllInResponseCategoryBlock();

    void saveHistoryCategoryBlock(List<ClientCategoryRequestForAdd> dto);

    void deleteById(Long id);
}
