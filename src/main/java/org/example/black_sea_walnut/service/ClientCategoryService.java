package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientCategoryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.entity.ClientCategory;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.user.Saveable;

import java.util.List;

public interface ClientCategoryService {
    void save(ClientCategory entity);

    <M extends GenericsMapper> void saveClientCategory(Saveable<ClientCategory, M> dto, M mapper);

    ClientCategory getOrCreate(Long id);

    List<ClientCategory> getAll();

    List<ClientCategoryResponseForAdd> getAllInResponse();

    List<ClientCategoryResponseForAdd> getAllInResponseByIsActive(boolean isActive);

    ClientCategory getById(Long id);

    void deleteById(Long id);
}
