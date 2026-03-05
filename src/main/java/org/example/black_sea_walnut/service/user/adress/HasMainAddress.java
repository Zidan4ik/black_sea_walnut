package org.example.black_sea_walnut.service.user.adress;

public interface HasMainAddress {
    Long getCityForDeliveryId();
    Long getRegionForDeliveryId();
    String getAddress();
}
