package org.example.black_sea_walnut.service.imp;

import org.example.black_sea_walnut.entity.Country;
import org.example.black_sea_walnut.entity.Region;
import org.example.black_sea_walnut.repository.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RegionServiceImpTest {
    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionServiceImp regionService;

    private Region region;
    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country();
        country.setId(1L);
        country.setName("Ukraine");

        region = new Region();
        region.setId(1L);
        region.setName("Kyiv");
        region.setCountry(country);
    }

    @Test
    void testGetById_RegionFound() {
        when(regionRepository.findById(1L)).thenReturn(Optional.of(region));

        Optional<Region> result = regionService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals(region, result.get());
        verify(regionRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_RegionNotFound() {
        when(regionRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Region> result = regionService.getById(2L);

        assertFalse(result.isPresent());
        verify(regionRepository, times(1)).findById(2L);
    }

    @Test
    void testGetById_NullId() {
        Optional<Region> result = regionService.getById(null);

        assertFalse(result.isPresent());
        verify(regionRepository, never()).findById(any());
    }

    @Test
    void testGetAll() {
        List<Region> regions = List.of(region);
        when(regionRepository.findAll()).thenReturn(regions);

        List<Region> result = regionService.getAll();

        assertEquals(1, result.size());
        assertEquals(region, result.get(0));
        verify(regionRepository, times(1)).findAll();
    }

    @Test
    void testGetByCountry_RegionsFound() {
        List<Region> regions = List.of(region);
        when(regionRepository.getRegionsByCountry(country)).thenReturn(regions);

        List<Region> result = regionService.getByCountry(country);

        assertEquals(1, result.size());
        assertEquals(region, result.get(0));
        verify(regionRepository, times(1)).getRegionsByCountry(country);
    }

    @Test
    void testGetByCountry_NoRegionsFound() {
        when(regionRepository.getRegionsByCountry(country)).thenReturn(List.of());

        List<Region> result = regionService.getByCountry(country);

        assertTrue(result.isEmpty());
        verify(regionRepository, times(1)).getRegionsByCountry(country);
    }

    @Test
    void testGetByCountry_NullCountry() {
        List<Region> result = regionService.getByCountry(null);

        assertTrue(result.isEmpty());
        verify(regionRepository, never()).getRegionsByCountry(any());
    }
}