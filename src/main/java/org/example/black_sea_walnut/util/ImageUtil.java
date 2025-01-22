package org.example.black_sea_walnut.util;

import org.example.black_sea_walnut.enums.MediaType;
import org.springframework.web.multipart.MultipartFile;

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
}
