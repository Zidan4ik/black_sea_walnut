package org.example.black_sea_walnut.dto.admin.pages.catalog;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.admin.pages.catalog.request.BannerBlockRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.request.EcologicallyBlockRequestForAdd;

@Builder
@Getter
public class PageCatalogRequestForAdd {
    @Valid
    private BannerBlockRequestForAdd requestBannerForAdd;
    @Valid
    private EcologicallyBlockRequestForAdd requestEcologicallyForAdd;
}
