package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.entity.Country;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    List<Country> getAll();

    Optional<Country> getById(Long id);

    void save(Country country);
}
