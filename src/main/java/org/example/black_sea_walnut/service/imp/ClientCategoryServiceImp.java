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
        clientCategoryRepository.save(entity);
    }

    @SneakyThrows
    @Override
    public void save(ClientCategoryRequestForAdd dto) {
        dto.setMediaTypeSvg(ImageUtil.getMediaType(dto.getClientsCategoryFileSvg()));
        dto.setMediaTypeImage(ImageUtil.getMediaType(dto.getClientsCategoryFileImage()));

        if (dto.getClientsCategoryFileImage() != null) {
            String generatedPath = contextPath + "/page-clients/" + dto.getMediaTypeImage() + "/" + imageService.generateFileName(dto.getClientsCategoryFileImage());
            dto.setClientsCategoryPathToImage(generatedPath);
        }
        if (dto.getClientsCategoryFileSvg() != null) {
            String generatedPath = contextPath + "/page-clients/" + dto.getMediaTypeImage() + "/" + imageService.generateFileName(dto.getClientsCategoryFileSvg());
            dto.setClientsCategoryPathToSvg(generatedPath);
        }

        if (dto.getClientsCategoryId() != null) {
            ClientCategory clientCategoryById = getById(dto.getClientsCategoryId());
            if (dto.getClientsCategoryPathToImage().isEmpty()) {
                imageService.deleteByPath(clientCategoryById.getPathToImage());
            }
            if (dto.getClientsCategoryPathToSvg().isEmpty()) {
                imageService.deleteByPath(clientCategoryById.getPathToSvg());
            }

            clientCategoryById.setPathToImage(dto.getClientsCategoryPathToImage());
            clientCategoryById.setPathToSvg(dto.getClientsCategoryPathToSvg());
            clientCategoryById.setMediaTypeImage(dto.getMediaTypeImage());
            clientCategoryById.setMediaTypeSvg(dto.getMediaTypeSvg());
        }

        imageService.save(dto.getClientsCategoryFileImage(), dto.getClientsCategoryPathToImage());
        imageService.save(dto.getClientsCategoryFileSvg(), dto.getClientsCategoryPathToSvg());

        save(clientsMapper.toEntityFromRequestClientCategoryBlock(dto));
    }

    @Override
    public List<ClientCategory> getAll() {
        return clientCategoryRepository.findAll();
    }

    @Override
    public List<ClientCategoryResponseForAdd> getAllInResponse() {
        return getAll().stream().map(clientsMapper::toResponseCategoryForAdd).toList();
    }

    @Override
    public ClientCategory getById(Long id) {
        return clientCategoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ClientCategory with id: " + id + " was not found!"));
    }

    @Override
    public void deleteById(Long id) {
        clientCategoryRepository.deleteById(id);
    }
}
