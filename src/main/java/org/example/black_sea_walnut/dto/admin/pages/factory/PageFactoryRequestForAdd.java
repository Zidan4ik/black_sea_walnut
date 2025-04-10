package org.example.black_sea_walnut.dto.admin.pages.factory;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.admin.pages.factory.request.BannerBlockRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.request.BlockRequestForAdd;

@Builder
@Getter
public class PageFactoryRequestForAdd {
    @Valid
    private BannerBlockRequestForAdd requestFactoryBannerForAdd;
    @Valid
    private BlockRequestForAdd requestFactoryBlockForAdd;
}