package org.example.black_sea_walnut.dto.web.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddressDtoLegal {
    private Long id;
    private Long idCountry;
    private Long idRegion;
    private Long idCity;
    private Long idCountryLegal;
    private Long idRegionLegal;
    private Long idCityLegal;
    private String address;
    private String addressLegal;
    private String index;
    private String okpo;
}
