package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.Country;
import org.example.black_sea_walnut.entity.Region;
import org.example.black_sea_walnut.repository.CountryRepository;
import org.example.black_sea_walnut.service.CountryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryServiceImp implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public List<Country> getAll() {
        return countryRepository.findAll();
    }

    @Override
    public Country getById(Long id) {
        return countryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Country with id:" + id + " was not found!"));
    }
}
