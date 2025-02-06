package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.City;
import org.example.black_sea_walnut.repository.CityRepository;
import org.example.black_sea_walnut.service.CityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImp implements CityService {
    private final CityRepository cityRepository;

    @Override
    public City getById(Long id) {
        return cityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("City with id:" + id + " was not found!"));
    }

    @Override
    public List<City> getAll() {
        return cityRepository.findAll();
    }
}
