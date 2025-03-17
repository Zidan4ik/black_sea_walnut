package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientCategoryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.entity.ClientCategory;

import java.util.List;

public interface ClientCategoryService {
    void save(ClientCategory entity);
    void save(ClientCategoryRequestForAdd dto);
    List<ClientCategory> getAll();
    List<ClientCategoryResponseForAdd> getAllInResponse();
    ClientCategory getById(Long id);
    void deleteById(Long id);
}
