package org.example.black_sea_walnut.dto.gallery;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GalleryResponseForAdd {
    private Long id;
    private String title;
    private String description;
    private boolean isActive;
    private String pathToMediaFile;
}
