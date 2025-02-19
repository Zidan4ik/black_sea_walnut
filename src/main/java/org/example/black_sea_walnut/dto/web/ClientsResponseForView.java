package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.pages.catalog.PageCatalogResponseForAdd;
import org.example.black_sea_walnut.dto.pages.clients.request.ClientCategoryRequestForAdd;
import org.example.black_sea_walnut.dto.pages.clients.response.ClientBannerResponseForAdd;
import org.example.black_sea_walnut.dto.pages.clients.response.ClientCategoryResponseForAdd;
import org.example.black_sea_walnut.dto.pages.clients.response.ClientEcoProductionResponseForAdd;
import org.example.black_sea_walnut.dto.pages.main.response.NumberBlockResponseForAddInMain;

import java.util.List;

@Builder
@Data
public class ClientsResponseForView {
    private NumberBlockResponseForAddInMain numbers;
    private ClientEcoProductionResponseForAdd eco;
    private ClientBannerResponseForAdd banner;
    private ContactDtoForAdd contacts;
    private List<ClientCategoryResponseForAdd> categories;
}
