package org.example.black_sea_walnut.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.black_sea_walnut.enums.MediaType;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
public class ResponseNewForAdd {
    private Long id;
    private Boolean isActive;
    private String titleUA;
    private String titleENG;
    private String descriptionUA;
    private String descriptionENG;
    private String dateOfPublication;
    private String pathToImage;
    private MultipartFile file;
    private MediaType mediaType;
}
