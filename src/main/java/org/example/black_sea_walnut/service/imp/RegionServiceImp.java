package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.Country;
import org.example.black_sea_walnut.entity.Region;
import org.example.black_sea_walnut.repository.RegionRepository;
import org.example.black_sea_walnut.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionServiceImp implements RegionService {
    private final RegionRepository regionRepository;

    @Override
    public Optional<Region> getById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return regionRepository.findById(id);
    }

    @Override
    public List<Region> getAll() {
        return regionRepository.findAll();
    }

    @Override
    public List<Region> getByCountry(Country country) {
        return regionRepository.getRegionsByCountry(country);
    }
}
