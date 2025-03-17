package org.example.black_sea_walnut.dto.admin.pages.clients;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientBannerResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.response.ClientEcoProductionResponseForAdd;

import java.util.List;

@Builder
@Getter
public class PageClientResponseForAdd {
    private ClientBannerResponseForAdd responseClientBannerForAdd;
    private ClientEcoProductionResponseForAdd responseClientEcoProductionForAdd;
    private List<ClientCategoryResponseForAdd> responseClientCategoryForAdd;
}
