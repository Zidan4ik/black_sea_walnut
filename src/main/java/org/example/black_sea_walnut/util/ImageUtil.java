package org.example.black_sea_walnut.util;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.service.ImageService;
import org.example.black_sea_walnut.service.Uploadable;
import org.example.black_sea_walnut.service.file.FileProcessable;
import org.example.black_sea_walnut.service.file.ImageEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ImageUtil {
    private final ImageService imageService;
    public static MediaType getMediaType(MultipartFile file) {
        if (file != null) {
            if (Objects.requireNonNull(file.getContentType()).contains("image")) {
                return org.example.black_sea_walnut.enums.MediaType.image;
            } else if (Objects.requireNonNull(file.getContentType()).contains("video")) {
                return org.example.black_sea_walnut.enums.MediaType.video;
            }
        }
        return null;
    }

    public static void deleteImageIfEmpty(Product product, String fieldName, ImageService imageService) {
        try {
            Field field = Product.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            String path = (String) field.get(product);
            imageService.deleteByPath(path);
            LogUtil.logInfo("Deleted image at path: " + path);
        } catch (NoSuchFieldException | IllegalAccessException | IOException e) {
            LogUtil.logError("Error accessing field: " + fieldName, e);
            throw new RuntimeException("Error accessing image path field: " + fieldName);
        }
    }

    public void safeDelete(String path, ImageService imageService) {
        try {
            if (path != null && !path.isEmpty()) {
                imageService.deleteByPath(path);
            }
        } catch (Exception e) {
            org.example.black_sea_walnut.util.LogUtil.logError("Failed to delete file at: " + path, e);
        }
    }

    public void handleImage(ImageEntity entity, FileProcessable dto, Uploadable u) {

        if (dto.getPathToImage() != null && dto.getPathToImage().isEmpty()
                && entity.getPathToImage() != null && !entity.getPathToImage().isEmpty()) {
            safeDelete(entity.getPathToImage(),imageService);
        }

        String pathToImg = (dto.getPathToImage() != null) ? dto.getPathToImage() : null;
        if (dto.getFileImage() != null && !dto.getFileImage().isEmpty()) {
            pathToImg = imageService.generatePath(dto.getFileImage(), u);
            imageService.save(dto.getFileImage(), pathToImg);
        }
        dto.setPathToImage(pathToImg);
    }

    public void handleSvg(ImageEntity entity, FileProcessable dto, Uploadable u) {

        if (dto.getPathToSvg() != null && dto.getPathToSvg().isEmpty()
                && entity.getPathToSvg() != null && !entity.getPathToSvg().isEmpty()) {
            safeDelete(entity.getPathToSvg(),imageService);
        }

        String pathToSvg = (dto.getPathToSvg() != null) ? dto.getPathToSvg() : null;
        if (dto.getFileSvg() != null && !dto.getFileSvg().isEmpty()) {
            pathToSvg = imageService.generatePath(dto.getFileSvg(), u);
            imageService.save(dto.getFileSvg(), pathToSvg);
        }
        dto.setPathToSvg(pathToSvg);
    }
}
