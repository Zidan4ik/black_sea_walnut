package org.example.black_sea_walnut.service.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.black_sea_walnut.entity.Region;
import org.example.black_sea_walnut.repository.RegionRepository;
import org.example.black_sea_walnut.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionServiceImp implements RegionService {
    private final RegionRepository regionRepository;

    @Override
    public Region getById(Long id) {
        return regionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Region with id:" + id + " was not found!"));
    }

    @Override
    public List<Region> getAll() {
        return regionRepository.findAll();
    }
}
