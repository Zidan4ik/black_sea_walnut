package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.City;
import org.example.black_sea_walnut.entity.Region;
import org.example.black_sea_walnut.repository.CityRepository;
import org.example.black_sea_walnut.service.CityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CityServiceImp implements CityService {
    private final CityRepository cityRepository;

    @Override
    public Optional<City> getById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return cityRepository.findById(id);
    }

    @Override
    public List<City> getAll() {
        return cityRepository.findAll();
    }

    @Override
    public List<City> getByRegion(Region region) {
        return cityRepository.getAllByRegion(region);
    }
}
