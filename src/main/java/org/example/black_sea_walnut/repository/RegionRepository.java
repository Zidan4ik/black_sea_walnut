package org.example.black_sea_walnut.repository;

import org.example.black_sea_walnut.entity.Country;
import org.example.black_sea_walnut.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    List<Region> getRegionsByCountry(Country country);
}
