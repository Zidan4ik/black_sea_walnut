package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.entity.City;

import java.util.List;

public interface CityService {
    City getById(Long id);

    List<City> getAll();
}
