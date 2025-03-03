package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.entity.Country;
import org.example.black_sea_walnut.entity.Region;

import java.util.List;
import java.util.Optional;

public interface RegionService {
    Optional<Region> getById(Long id);

    List<Region> getAll();

    List<Region> getByCountry(Country country);
}
