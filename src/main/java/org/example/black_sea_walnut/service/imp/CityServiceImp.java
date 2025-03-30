package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.City;
import org.example.black_sea_walnut.entity.Region;
import org.example.black_sea_walnut.repository.CityRepository;
import org.example.black_sea_walnut.service.CityService;
import org.example.black_sea_walnut.util.LogUtil;
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
            LogUtil.logWarning("Attempted to fetch a city with a null ID.");
            return Optional.empty();
        }
        LogUtil.logInfo("Fetching city with ID: " + id);
        Optional<City> city = cityRepository.findById(id);
        if (city.isPresent()) {
            LogUtil.logInfo("Found city with ID: " + id);
        } else {
            LogUtil.logWarning("No city found with ID: " + id);
        }
        return city;
    }

    @Override
    public List<City> getAll() {
        LogUtil.logInfo("Fetching all cities.");
        List<City> cities = cityRepository.findAll();
        LogUtil.logInfo("Fetched " + cities.size() + " cities.");
        return cities;
    }

    @Override
    public List<City> getByRegion(Region region) {
        if (region == null) {
            LogUtil.logWarning("Attempted to fetch cities for a null region.");
            return List.of();
        }
        LogUtil.logInfo("Fetching cities for region: " + region.getName());
        List<City> cities = cityRepository.getAllByRegion(region);
        LogUtil.logInfo("Fetched " + cities.size() + " cities for region: " + region.getName());
        return cities;
    }

    @Override
    public void save(City city) {
        cityRepository.save(city);
    }
}

