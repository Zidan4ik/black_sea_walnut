package org.example.black_sea_walnut.util;

import org.example.black_sea_walnut.entity.Product;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.service.ImageService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

public class ImageUtil {
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
}
