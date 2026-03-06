package org.example.black_sea_walnut.dto.web.user;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.service.user.adress.HasCountry;
import org.example.black_sea_walnut.service.user.adress.HasMainAddress;

@Builder
@Data
public class AddressDtoIndividual implements Saveable, HasMainAddress, HasCountry {
    private Long id;
    private Long idCountry;
    private Long idRegion;
    private Long idCity;
    private String address;

    @Override
    public Long getCityForDeliveryId() {
        return idCity;
    }

    @Override
    public Long getRegionForDeliveryId() {
        return this.idRegion;
    }
}
