package org.example.black_sea_walnut.service.imp;

import org.example.black_sea_walnut.entity.City;
import org.example.black_sea_walnut.entity.Region;
import org.example.black_sea_walnut.repository.CityRepository;
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
class CityServiceImpTest {
    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityServiceImp cityService;

    private City city;
    private Region region;

    @BeforeEach
    void setUp() {
        region = new Region();
        region.setName("Test Region");

        city = new City();
        city.setId(1L);
        city.setName("Test City");
        city.setRegion(region);
    }

    @Test
    void testGetById_WhenIdIsNull_ShouldReturnEmpty() {
        assertTrue(cityService.getById(null).isEmpty());
        verifyNoInteractions(cityRepository);
    }

    @Test
    void testGetById_WhenCityExists_ShouldReturnCity() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        Optional<City> result = cityService.getById(1L);
        assertTrue(result.isPresent());
        assertEquals(city, result.get());
        verify(cityRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_WhenCityDoesNotExist_ShouldReturnEmpty() {
        when(cityRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<City> result = cityService.getById(1L);
        assertTrue(result.isEmpty());
        verify(cityRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAll_ShouldReturnCitiesList() {
        when(cityRepository.findAll()).thenReturn(List.of(city));
        List<City> result = cityService.getAll();
        assertEquals(1, result.size());
        assertEquals(city, result.get(0));
        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void testGetByRegion_WhenRegionIsNull_ShouldReturnEmptyList() {
        List<City> result = cityService.getByRegion(null);
        assertTrue(result.isEmpty());
        verifyNoInteractions(cityRepository);
    }

    @Test
    void testGetByRegion_WhenRegionExists_ShouldReturnCitiesList() {
        when(cityRepository.getAllByRegion(region)).thenReturn(List.of(city));
        List<City> result = cityService.getByRegion(region);
        assertEquals(1, result.size());
        assertEquals(city, result.get(0));
        verify(cityRepository, times(1)).getAllByRegion(region);
    }
}