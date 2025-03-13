package org.example.black_sea_walnut.dto.web.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddressDtoIndividual {
    private Long id;
    private Long idCountry;
    private Long idRegion;
    private Long idCity;
    private String address;
}
