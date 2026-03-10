package org.example.black_sea_walnut.service.file;

public interface ImageEntity {
    String getPathToImage();

    default String getPathToSvg() {
        return "";
    }
}
