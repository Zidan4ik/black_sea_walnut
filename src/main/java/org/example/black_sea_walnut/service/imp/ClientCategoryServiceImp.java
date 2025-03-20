package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientCategoryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.entity.ClientCategory;
import org.example.black_sea_walnut.mapper.pages.HistoryClientsMapper;
import org.example.black_sea_walnut.repository.ClientCategoryRepository;
import org.example.black_sea_walnut.service.ClientCategoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.util.ImageUtil;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientCategoryServiceImp implements ClientCategoryService {
    private final ClientCategoryRepository clientCategoryRepository;
    private final HistoryClientsMapper clientsMapper;
    private final ImageService imageService;
    @Value("${upload.path}")
    private String contextPath;

    @Override
    public void save(ClientCategory entity) {
        LogUtil.logInfo("Saving ClientCategory entity: " + entity);
        clientCategoryRepository.save(entity);
        LogUtil.logInfo("ClientCategory entity saved: " + entity);
    }

    @SneakyThrows
    @Override
    public void save(ClientCategoryRequestForAdd dto) {
        LogUtil.logInfo("Starting to save ClientCategoryRequestForAdd: " + dto);

        dto.setMediaTypeSvg(ImageUtil.getMediaType(dto.getClientsCategoryFileSvg()));
        dto.setMediaTypeImage(ImageUtil.getMediaType(dto.getClientsCategoryFileImage()));

        if (dto.getClientsCategoryFileImage() != null) {
            String generatedPath = contextPath + "/page-clients/" + dto.getMediaTypeImage() + "/" + imageService.generateFileName(dto.getClientsCategoryFileImage());
            dto.setClientsCategoryPathToImage(generatedPath);
            LogUtil.logInfo("Generated image path for category: " + generatedPath);
        }
        if (dto.getClientsCategoryFileSvg() != null) {
            String generatedPath = contextPath + "/page-clients/" + dto.getMediaTypeSvg() + "/" + imageService.generateFileName(dto.getClientsCategoryFileSvg());
            dto.setClientsCategoryPathToSvg(generatedPath);
            LogUtil.logInfo("Generated SVG path for category: " + generatedPath);
        }

        if (dto.getClientsCategoryId() != null) {
            LogUtil.logInfo("ClientCategoryId provided: " + dto.getClientsCategoryId());
            ClientCategory clientCategoryById = getById(dto.getClientsCategoryId());
            LogUtil.logInfo("Found ClientCategory by ID: " + clientCategoryById);

            if (dto.getClientsCategoryPathToImage().isEmpty()) {
                LogUtil.logInfo("Deleting old image for category with ID: " + dto.getClientsCategoryId());
                imageService.deleteByPath(clientCategoryById.getPathToImage());
            }
            if (dto.getClientsCategoryPathToSvg().isEmpty()) {
                LogUtil.logInfo("Deleting old SVG for category with ID: " + dto.getClientsCategoryId());
                imageService.deleteByPath(clientCategoryById.getPathToSvg());
            }

            clientCategoryById.setPathToImage(dto.getClientsCategoryPathToImage());
            clientCategoryById.setPathToSvg(dto.getClientsCategoryPathToSvg());
            clientCategoryById.setMediaTypeImage(dto.getMediaTypeImage());
            clientCategoryById.setMediaTypeSvg(dto.getMediaTypeSvg());
            LogUtil.logInfo("Updated ClientCategory with new paths.");
        }

        imageService.save(dto.getClientsCategoryFileImage(), dto.getClientsCategoryPathToImage());
        imageService.save(dto.getClientsCategoryFileSvg(), dto.getClientsCategoryPathToSvg());
        LogUtil.logInfo("Images saved successfully for category.");

        save(clientsMapper.toEntityFromRequestClientCategoryBlock(dto));
        LogUtil.logInfo("ClientCategoryRequestForAdd saved successfully.");
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

    @Override
    public void deleteById(Long id) {
        LogUtil.logInfo("Attempting to delete ClientCategory with ID: " + id);
        clientCategoryRepository.deleteById(id);
        LogUtil.logInfo("ClientCategory with ID: " + id + " deleted successfully.");
    }
}
