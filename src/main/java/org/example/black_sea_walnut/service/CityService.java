package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.entity.City;
import org.example.black_sea_walnut.entity.Region;

import java.util.List;
import java.util.Optional;

public interface CityService {
    Optional<City> getById(Long id);

    List<City> getAll();

    List<City> getByRegion(Region region);

    City save(City city);
}