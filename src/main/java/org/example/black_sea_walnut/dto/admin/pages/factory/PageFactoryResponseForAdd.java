package org.example.black_sea_walnut.dto.admin.pages.factory;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.admin.pages.factory.response.BlockResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.factory.response.FactoryBannerBlockResponseForAdd;

@Builder
@Getter
public class PageFactoryResponseForAdd {
    private FactoryBannerBlockResponseForAdd responseFactoryBannerForAdd;
    private BlockResponseForAdd responseFactoryBlockForAdd;
}