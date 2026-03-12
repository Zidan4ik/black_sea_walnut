package org.example.black_sea_walnut.service.product;

import org.springframework.web.multipart.MultipartFile;

public interface ProductImages {
    MultipartFile getImage1();

    MultipartFile getImage2();

    MultipartFile getImage3();

    MultipartFile getImage4();

    MultipartFile getImageDescription();

    MultipartFile getImagePacking();

    MultipartFile getImagePayment();

    MultipartFile getImageDelivery();

    String getPathToImage1();

    String getPathToImage2();

    String getPathToImage3();

    String getPathToImage4();

    String getPathToImageDescription();

    String getPathToImagePacking();

    String getPathToImagePayment();

    String getPathToImageDelivery();
}
