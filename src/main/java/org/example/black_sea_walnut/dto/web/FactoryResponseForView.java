package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.gallery.GalleryResponseForAdd;
import org.example.black_sea_walnut.dto.new_.NewRequestForAdd;
import org.example.black_sea_walnut.dto.pages.factory.response.BlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.factory.response.FactoryBannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.pages.main.response.EcoProductionResponseForAddInMain;
import org.example.black_sea_walnut.dto.pages.main.response.NumberBlockResponseForAddInMain;

import java.util.List;

@Builder
@Data
public class FactoryResponseForView {
    private FactoryBannerBlockResponseForAdd banner;
    private BlockResponseForAdd block;
    private List<GalleryResponseForAdd> gallery;
    private ContactDtoForAdd contacts;
    private List<NewRequestForAdd> news;
    private EcoProductionResponseForAddInMain ecology;
    private NumberBlockResponseForAddInMain numbers;
}
