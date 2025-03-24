package org.example.black_sea_walnut.service.imp;

import org.junit.jupiter.api.extension.ExtendWith;

import org.example.black_sea_walnut.entity.Country;
import org.example.black_sea_walnut.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceImpTest {
    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryServiceImp countryService;

    private Country country;

    @BeforeEach
    void setUp() {
        country = new Country();
        country.setId(1L);
        country.setName("Ukraine");
    }

    @Test
    void testGetAllCountries() {
        when(countryRepository.findAll()).thenReturn(List.of(country));

        List<Country> countries = countryService.getAll();

        assertNotNull(countries);
        assertEquals(1, countries.size());
        assertEquals("Ukraine", countries.get(0).getName());
        verify(countryRepository, times(1)).findAll();
    }

    @Test
    void testGetById_WhenCountryExists() {
        when(countryRepository.findById(1L)).thenReturn(Optional.of(country));

        Optional<Country> foundCountry = countryService.getById(1L);

        assertTrue(foundCountry.isPresent());
        assertEquals("Ukraine", foundCountry.get().getName());
        verify(countryRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_WhenCountryDoesNotExist() {
        when(countryRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Country> foundCountry = countryService.getById(2L);

        assertFalse(foundCountry.isPresent());
        verify(countryRepository, times(1)).findById(2L);
    }

    @Test
    void testGetById_WhenIdIsNull() {
        Optional<Country> foundCountry = countryService.getById(null);

        assertFalse(foundCountry.isPresent());
        verifyNoInteractions(countryRepository);
    }
}