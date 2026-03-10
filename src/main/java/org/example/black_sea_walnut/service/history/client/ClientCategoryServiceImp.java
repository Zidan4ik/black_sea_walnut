package org.example.black_sea_walnut.service.history.client;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.entity.ClientCategory;
import org.example.black_sea_walnut.mapper.pages.HistoryClientsMapper;
import org.example.black_sea_walnut.repository.ClientCategoryRepository;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.file.FileProcessable;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientCategoryServiceImp implements ClientCategoryService {
    private final ClientCategoryRepository clientCategoryRepository;
    private final HistoryClientsMapper clientsMapper;
    private final ImageService imageService;

    @Override
    public void save(ClientCategory entity) {
        LogUtil.logInfo("Saving ClientCategory entity: " + entity);
        clientCategoryRepository.save(entity);
        LogUtil.logInfo("ClientCategory entity saved: " + entity);
    }

    @SneakyThrows
    public <M extends GenericsMapper> void saveClientCategory(Saveable<ClientCategory, M> dto, M mapper) {
        ClientCategory entity = getOrCreate(dto.getId());
        if (dto instanceof FileProcessable dtos && dto instanceof Uploadable u) {
            handleImagesUpdate(entity, dtos, u);
        }
        dto.updateEntity(entity, mapper);
        save(entity);
    }

    @Override
    public ClientCategory getOrCreate(Long id) {
        return (id != null) ? getById(id) : new ClientCategory();
    }

    @Override
    public List<ClientCategory> getAll() {
        LogUtil.logInfo("Fetching all ClientCategories.");
        List<ClientCategory> clientCategories = clientCategoryRepository.findAll();
        LogUtil.logInfo("Fetched " + clientCategories.size() + " ClientCategories.");
        return clientCategories;
    }

    @Override
    public List<ClientCategoryResponseForAdd> getAllInResponse() {
        LogUtil.logInfo("Converting all ClientCategories to response DTOs.");
        List<ClientCategoryResponseForAdd> response = getAll().stream().map(clientsMapper::toResponseCategoryForAdd).toList();
        LogUtil.logInfo("Converted " + response.size() + " ClientCategories to response DTOs.");
        return response;
    }

    @Override
    public List<ClientCategoryResponseForAdd> getAllInResponseByIsActive(boolean isActive) {
        LogUtil.logInfo("Converting all ClientCategories to response DTOs.");
        List<ClientCategoryResponseForAdd> response = clientCategoryRepository.getAllByIsActive(isActive).stream().map(clientsMapper::toResponseCategoryForAdd).toList();
        LogUtil.logInfo("Converted " + response.size() + " ClientCategories to response DTOs.");
        return response;
    }

    @Override
    public ClientCategory getById(Long id) {
        LogUtil.logInfo("Fetching ClientCategory with ID: " + id);
        ClientCategory clientCategory = clientCategoryRepository.findById(id)
                .orElseThrow(() -> {
                    LogUtil.logError("ClientCategory with ID: " + id + " not found!", null);
                    return new EntityNotFoundException("ClientCategory with id: " + id + " was not found!");
                });
        LogUtil.logInfo("Found ClientCategory with ID: " + id);
        return clientCategory;
    }

    @SneakyThrows
    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Attempting to delete ClientCategory with ID: " + id);
        ClientCategory client = getById(id);
        imageService.deleteByPath(client.getPathToImage());
        imageService.deleteByPath(client.getPathToSvg());
        clientCategoryRepository.deleteById(id);
        LogUtil.logInfo("ClientCategory with ID: " + id + " deleted successfully.");
    }

    private void handleImagesUpdate(ClientCategory entity, FileProcessable dto, Uploadable u) {

        if (dto.getPathToImage() != null && dto.getPathToImage().isEmpty()
                && entity.getPathToImage() != null && !entity.getPathToImage().isEmpty()) {
            safeDelete(entity.getPathToImage());
        }

        String pathToImg = (dto.getPathToImage() != null) ? dto.getPathToImage() : null;
        if (dto.getFileImage() != null && !dto.getFileImage().isEmpty()) {
            pathToImg = imageService.generatePath(dto.getFileImage(), u);
            imageService.save(dto.getFileImage(), pathToImg);
        }
        dto.setPathToImage(pathToImg);

        if (dto.getPathToSvg() != null && dto.getPathToSvg().isEmpty()
                && entity.getPathToSvg() != null && !entity.getPathToSvg().isEmpty()) {
            safeDelete(entity.getPathToSvg());
        }

        String pathToSvg = (dto.getPathToSvg() != null) ? dto.getPathToSvg() : null;
        if (dto.getFileSvg() != null && !dto.getFileSvg().isEmpty()) {
            pathToSvg = imageService.generatePath(dto.getFileSvg(), u);
            imageService.save(dto.getFileSvg(), pathToSvg);
        }
        dto.setPathToSvg(pathToSvg);
    }

    private void safeDelete(String path) {
        try {
            if (path != null && !path.isEmpty()) {
                imageService.deleteByPath(path);
            }
        } catch (IOException e) {
            LogUtil.logError("Failed to delete media at path: " + path, e);
        }
    }
}
