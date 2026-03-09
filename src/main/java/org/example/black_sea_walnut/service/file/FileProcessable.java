package org.example.black_sea_walnut.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileProcessable {
    MultipartFile getFileImage();

    default MultipartFile getFileSvg() {
        return null;
    }

    String getPathToImage();

    default String getPathToSvg() {
        return "";
    }

    void setPathToImage(String path);

    default void setPathToSvg(String path) {
    }
}
