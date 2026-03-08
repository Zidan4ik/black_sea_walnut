package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientCategoryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.entity.ClientCategory;
import org.example.black_sea_walnut.entity.History;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.mapper.pages.HistoryClientsMapper;
import org.example.black_sea_walnut.repository.ClientCategoryRepository;
import org.example.black_sea_walnut.service.ClientCategoryService;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.file.FileProcessable;
import org.example.black_sea_walnut.service.history.GenericsMapper;
import org.example.black_sea_walnut.service.history.HistoryFileRequest;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.util.ImageUtil;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.beans.factory.annotation.Value;
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

        handleImagesUpdate(entity, (ClientCategoryRequestForAdd) dto);

        dto.updateEntity(entity, mapper);

        save(entity);
    }

    private void handleImagesUpdate(ClientCategory entity, ClientCategoryRequestForAdd dto) throws IOException {

        if (dto.getClientsCategoryPathToImage() != null && dto.getClientsCategoryPathToImage().isEmpty()
                && !entity.getPathToImage().isEmpty()) {
            safeDelete(entity.getPathToImage());
        }


        String pathToImg = (dto.getClientsCategoryPathToImage() != null) ? dto.getClientsCategoryPathToImage() : null;
        if (dto.getClientsCategoryFileImage() != null && !dto.getClientsCategoryFileImage().isEmpty()) {
            pathToImg = imageService.generatePath(dto.getClientsCategoryFileImage(), dto);
            imageService.save(dto.getClientsCategoryFileImage(), pathToImg);
        }
        entity.setPathToImage(pathToImg);


        if (dto.getClientsCategoryPathToSvg() != null && dto.getClientsCategoryPathToSvg().isEmpty()
                && !entity.getPathToSvg().isEmpty()) {
            safeDelete(entity.getPathToSvg());
        }

        String pathToSvg = (dto.getClientsCategoryPathToSvg() != null) ? dto.getClientsCategoryPathToSvg() : null;
        if (dto.getClientsCategoryFileSvg() != null && !dto.getClientsCategoryFileSvg().isEmpty()) {
            pathToSvg = imageService.generatePath(dto.getClientsCategoryFileSvg(), dto);
            imageService.save(dto.getClientsCategoryFileSvg(), pathToSvg);
        }
        entity.setPathToSvg(pathToSvg);

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

    public ClientCategory getOrCreate(Long id) {
        return (id != null) ? getById(id) : new ClientCategory();
    }

//    @SneakyThrows
//    @Override
//    public void save(ClientCategoryRequestForAdd dto) {
//        LogUtil.logInfo("Starting to save ClientCategoryRequestForAdd: " + dto);
//
//        dto.setMediaTypeSvg(ImageUtil.getMediaType(dto.getClientsCategoryFileSvg()));
//        dto.setMediaTypeImage(ImageUtil.getMediaType(dto.getClientsCategoryFileImage()));
//
//        if (dto.getClientsCategoryId() != null) {
//            LogUtil.logInfo("ClientCategoryId provided: " + dto.getClientsCategoryId());
//            ClientCategory clientCategoryById = getById(dto.getClientsCategoryId());
//            LogUtil.logInfo("Found ClientCategory by ID: " + clientCategoryById);
//
//            if (dto.getClientsCategoryPathToImage().isEmpty()) {
//                LogUtil.logInfo("Deleting old image for category with ID: " + dto.getClientsCategoryId());
//                imageService.deleteByPath(clientCategoryById.getPathToImage());
//            }
//            if (dto.getClientsCategoryPathToSvg().isEmpty()) {
//                LogUtil.logInfo("Deleting old SVG for category with ID: " + dto.getClientsCategoryId());
//                imageService.deleteByPath(clientCategoryById.getPathToSvg());
//            }
//
//            if (dto.getClientsCategoryFileImage() != null) {
//                String generatedPath = contextPath + "/pages/clients/images/" + dto.getMediaTypeImage() + "/" + imageService.generateFileName(dto.getClientsCategoryFileImage());
//                dto.setClientsCategoryPathToImage(generatedPath);
//                LogUtil.logInfo("Generated image path for category: " + generatedPath);
//            }
//            if (dto.getClientsCategoryFileSvg() != null) {
//                String generatedPath = contextPath + "/pages/clients/images/" + dto.getMediaTypeSvg() + "/" + imageService.generateFileName(dto.getClientsCategoryFileSvg());
//                dto.setClientsCategoryPathToSvg(generatedPath);
//                LogUtil.logInfo("Generated SVG path for category: " + generatedPath);
//            }
//
//            clientCategoryById.setPathToImage(dto.getClientsCategoryPathToImage());
//            clientCategoryById.setPathToSvg(dto.getClientsCategoryPathToSvg());
//            clientCategoryById.setMediaTypeImage(dto.getMediaTypeImage());
//            clientCategoryById.setMediaTypeSvg(dto.getMediaTypeSvg());
//            clientCategoryById.setActive(dto.getClientsCategoryIsActive());
//            LogUtil.logInfo("Updated ClientCategory with new paths.");
//        }
//
//        imageService.save(dto.getClientsCategoryFileImage(), dto.getClientsCategoryPathToImage());
//        imageService.save(dto.getClientsCategoryFileSvg(), dto.getClientsCategoryPathToSvg());
//        LogUtil.logInfo("Images saved successfully for category.");
//
//        save(clientsMapper.toEntityFromRequestClientCategoryBlock(dto));
//        LogUtil.logInfo("ClientCategoryRequestForAdd saved successfully.");
//    }

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
}
