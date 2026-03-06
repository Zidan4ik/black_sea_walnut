package org.example.black_sea_walnut.dto.web.user;

import lombok.Builder;
import lombok.Data;
import org.example.black_sea_walnut.entity.User;
import org.example.black_sea_walnut.mapper.UserMapper;
import org.example.black_sea_walnut.service.user.Saveable;
import org.example.black_sea_walnut.service.user.adress.HasAdditionalAddress;
import org.example.black_sea_walnut.service.user.adress.HasCountry;
import org.example.black_sea_walnut.service.user.adress.HasMainAddress;

@Builder
@Data
public class AddressDtoLegal implements Saveable<User,UserMapper>, HasMainAddress, HasAdditionalAddress, HasCountry {
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

    @Override
    public void updateEntity(User user, UserMapper mapper) {
        mapper.updateEntityFromRequest(this, user);
    }

    @Override
    public Long getCityAdditionallyId() {
        return this.idCityLegal;
    }

    @Override
    public Long getRegionAdditionallyId() {
        return this.idRegionLegal;
    }

    @Override
    public String getAddressAdditionally() {
        return this.addressLegal;
    }

    @Override
    public Long getCityForDeliveryId() {
        return this.idCity;
    }

    @Override
    public Long getRegionForDeliveryId() {
        return this.idRegion;
    }
}
