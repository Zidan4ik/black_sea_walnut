package org.example.black_sea_walnut.dto.pages.main;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.pages.main.request.*;

@Builder
@Getter
public class PageMainRequestForAdd {
    @Valid
    private AimBlockRequestForAdd requestAimBlockForAdd;
    @Valid
    private EcoProductionRequestForAdd requestEcoProductionForAdd;
    @Valid
    private FactoryBlockRequestForAdd requestFactoryForAdd;
    @Valid
    private MainBlockRequestForAdd requestMainForAdd;
    @Valid
    private ProductionBlockRequestForAdd requestProductionBlockForAdd;
    @Valid
    private NumberBlockRequestForAdd requestNumberBlockForAdd;
}
