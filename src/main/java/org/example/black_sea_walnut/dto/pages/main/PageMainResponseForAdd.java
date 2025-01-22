package org.example.black_sea_walnut.dto.pages.main;

import lombok.Builder;
import lombok.Getter;
import org.example.black_sea_walnut.dto.pages.main.response.*;

@Builder
@Getter
public class PageMainResponseForAdd {
    private AimBlockResponseForAdd responseAimBlock;
    private EcoProductionResponseForAdd responseEcoProductionBlock;
    private FactoryBlockResponseForAdd responseFactoryBlock;
    private MainBlockResponseForAdd responseMainBlock;
    private ProductionBlockResponseForAdd responseProductionBlock;
    private NumberBlockResponseForAdd responseNumberBlock;
}