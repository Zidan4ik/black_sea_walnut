package org.example.black_sea_walnut.service.user;

import org.springframework.web.multipart.MultipartFile;

public interface FileProcessable {
    MultipartFile getFileImage();
    String getPathToImage();
    void setPathToImage(String path);
}
