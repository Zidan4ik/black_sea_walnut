package org.example.black_sea_walnut.dto.admin.pages.main;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.admin.pages.main.response.*;

@Builder
@Getter
public class PageMainResponseForAdd {
    private AimBlockResponseForAddInMain responseAimBlock;
    private EcoProductionResponseForAddInMain responseEcoProductionBlock;
    private FactoryBlockResponseForAddInMain responseFactoryBlock;
    private BlockResponseForAddInMain responseMainBlock;
    private ProductionResponseForAddInMain responseProductionBlock;
    private NumberBlockResponseForAddInMain responseNumberBlock;
}