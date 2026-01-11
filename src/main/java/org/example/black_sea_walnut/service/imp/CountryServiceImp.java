package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.Country;
import org.example.black_sea_walnut.repository.CountryRepository;
import org.example.black_sea_walnut.service.CountryService;
import org.example.black_sea_walnut.util.LogUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryServiceImp implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public List<Country> getAll() {
        LogUtil.logInfo("Fetching all countries.");
        List<Country> countries = countryRepository.findAll();
        LogUtil.logInfo("Fetched " + countries.size() + " countries.");
        return countries;
    }

    @Override
    public Optional<Country> getById(Long id) {
        LogUtil.logInfo("Fetching country with ID: " + id);
        if (id == null) {
            LogUtil.logWarning("Provided ID is null, returning empty Optional.");
            return Optional.empty();
        }
        Optional<Country> country = countryRepository.findById(id);
        if (country.isPresent()) {
            LogUtil.logInfo("Found country with ID: " + id);
        } else {
            LogUtil.logWarning("Country with ID: " + id + " was not found.");
        }
        return country;
    }

    @Override
    public Country save(Country country) {
        return countryRepository.save(country);
    }
}
