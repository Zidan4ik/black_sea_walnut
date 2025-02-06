package org.example.black_sea_walnut.service;

import org.example.black_sea_walnut.entity.Region;

import java.util.List;

public interface RegionService {
    Region getById(Long id);

    List<Region> getAll();
}
