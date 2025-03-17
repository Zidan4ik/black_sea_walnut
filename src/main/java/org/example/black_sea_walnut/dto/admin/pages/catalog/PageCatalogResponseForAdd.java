package org.example.black_sea_walnut.dto.admin.pages.catalog;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.BannerBlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.catalog.response.EcologicallyBlockResponseForAdd;

@Builder
@Getter
public class PageCatalogResponseForAdd {
    private BannerBlockResponseForAdd bannerBlockResponseForAdd;
    private EcologicallyBlockResponseForAdd ecologicallyBlockResponseForAdd;
}
