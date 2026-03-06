package org.example.black_sea_walnut.service.user.adress;

public interface HasCountry {
    Long getIdCountry();
    default Long getIdCountryLegal(){return null;};
}
