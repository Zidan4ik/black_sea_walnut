package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.dto.admin.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.admin.gallery.GalleryResponseForAdd;

@Builder
@Data
public class GalleryResponseForPage {
    private GalleryResponseForAdd gallery;
    private ContactDtoForAdd contacts;
}
