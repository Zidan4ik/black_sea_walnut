package org.example.black_sea_walnut.dto.admin.pages.clients;

import jakarta.validation.Valid;
import lombok.Data;
import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientBannerRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientCategoryRequestForAdd;
import org.example.black_sea_walnut.dto.admin.pages.clients.request.ClientEcoProductionRequestForAdd;

import java.util.List;

@Data
public class PageClientsRequestForAdd {
    @Valid
    private ClientBannerRequestForAdd requestClientBannerForAdd;
    @Valid
    private ClientEcoProductionRequestForAdd requestClientEcoProductionForAdd;
    @Valid
    private List<ClientCategoryRequestForAdd> requestClientCategoryForAdd;
}
