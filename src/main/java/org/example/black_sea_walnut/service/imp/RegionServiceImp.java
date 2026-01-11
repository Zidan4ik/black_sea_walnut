package org.example.black_sea_walnut.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.Country;
import org.example.black_sea_walnut.entity.Region;
import org.example.black_sea_walnut.repository.RegionRepository;
import org.example.black_sea_walnut.service.RegionService;
import org.example.black_sea_walnut.util.LogUtil;
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
            LogUtil.logWarning("Attempted to fetch region with null ID");
            return Optional.empty();
        }
        LogUtil.logInfo("Fetching region by ID: " + id);
        return regionRepository.findById(id);
    }

    @Override
    public List<Region> getAll() {
        LogUtil.logInfo("Fetching all regions");
        return regionRepository.findAll();
    }

    @Override
    public List<Region> getByCountry(Country country) {
        if (country == null) {
            LogUtil.logWarning("Attempted to fetch regions with null country");
            return List.of();
        }
        LogUtil.logInfo("Fetching regions for country: " + country.getName());
        return regionRepository.getRegionsByCountry(country);
    }

    @Override
    public Region save(Region region) {
        return regionRepository.save(region);
    }
}
