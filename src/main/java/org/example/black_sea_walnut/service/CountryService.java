package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.entity.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAll();

    Country getById(Long id);
}
