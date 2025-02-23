package org.example.black_sea_walnut.dto.web;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.dto.contact.ContactDtoForAdd;
import org.example.black_sea_walnut.dto.new_.NewRequestForAdd;
import org.example.black_sea_walnut.dto.nut.NutResponseForAdd;
import org.example.black_sea_walnut.dto.pages.main.response.*;

import java.util.List;

@Builder
@Data
public class MainResponseForView {
    private List<ProductResponseForViewInTable> products;
    private BlockResponseForAddInMain banner;
    private ProductionResponseForAddInMain production;
    private FactoryBlockResponseForAddInMain factory;
    private NumberBlockResponseForAddInMain number;
    private AimBlockResponseForAddInMain aim;
    private EcoProductionResponseForAddInMain eco;
    private List<NutResponseForAdd> nuts;
    private List<NewRequestForAdd> news;
    private ContactDtoForAdd contacts;
}
