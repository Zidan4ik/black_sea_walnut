package org.example.black_sea_walnut.dto.historyMedia;


import lombok.*;
import org.example.black_sea_walnut.enums.MediaType;
import org.example.black_sea_walnut.validator.annotation.MediaValidation;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class HistoryMediaRequestForAdd {
    private Long id;
    private MediaType mediaType;
    private String pathToImage;
    @MediaValidation(message = "{error.file.valid}", allowedTypes = {"image/png", "image/jpg", "image/jpeg"})
    private MultipartFile fileImage;
}