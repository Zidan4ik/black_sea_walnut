package org.example.black_sea_walnut.service.history.client;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientCategoryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.mapper.pages.HistoryClientsMapper;
import org.example.black_sea_walnut.service.ClientCategoryService;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryClientServiceImp implements HistoryClientService {

    private final ClientCategoryService clientCategoryService;
    private final HistoryClientsMapper clientsMapper;
    @Override
    public List<ClientCategoryResponseForAdd> getAllInResponseCategoryBlock() {
        LogUtil.logInfo("Fetching all ClientCategoryResponseForAdd");
        List<ClientCategoryResponseForAdd> response = clientCategoryService.getAllInResponseByIsActive(true);
        LogUtil.logInfo("Fetched ClientCategoryResponseForAdd: " + response);
        return response;
    }

    @Override
    public void saveHistoryCategoryBlock(List<ClientCategoryRequestForAdd> dto) {
        if (dto != null) {
            LogUtil.logInfo("Saving ClientCategory with DTO: " + dto);
            for (ClientCategoryRequestForAdd d : dto) {
                clientCategoryService.saveClientCategory(d,clientsMapper);
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Deleting ClientCategory with ID: " + id);
        clientCategoryService.deleteById(id);
    }
}
