package org.example.black_sea_walnut.dto.web.user.address;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.dto.web.user.AddressDtoIndividual;
import org.example.black_sea_walnut.entity.Country;

import java.util.List;

@Builder
@Data
public class AddressPageResponseIndividual {
    private AddressDtoIndividual address;
    private List<Country> countries;
}
