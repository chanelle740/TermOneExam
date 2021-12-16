package rw.ac.rca.termOneExam.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.dto.CreateCityDTO;
import rw.ac.rca.termOneExam.repository.ICityRepository;
import rw.ac.rca.termOneExam.utils.CustomException;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CityServiceTest {

    @Mock
    ICityRepository cityRepository;

    @InjectMocks
    CityService cityService;


    @Test
    public void returnCities() {
        when(cityRepository.findAll()).thenReturn(Arrays.asList(
                new City(1, "Kigali City", 23, 23.90),
                new City(2, "Rubavu Cityyy", 23, 23.90),
                new City(3, "Huye City", 23, 23.90)));
        assertEquals(2, cityService.getAll().get(1).getId());
    }

    @Test
    public void createCity() {
        when(cityRepository.save(ArgumentMatchers.any(City.class))).thenReturn(new City(3, "Huye City", 23, 23.90));
        assertEquals("Huye City",cityService.save( new CreateCityDTO()).getName());
    }

    @Test
    public void createCityDuplicate(){

        when(cityRepository.existsByName(anyString())).thenReturn(true);

        Exception exception = assertThrows(CustomException.class, () -> cityService.save(new CreateCityDTO()));

        assertEquals("City name already registered", exception.getMessage());
    }


    @Test
    public void getCity_ByID(){
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(new City(120, "Huye City", 23, 23.90)));

        assertEquals("Huye City", cityService.getById(120).get().getName());
    }

    @Test
    public void getCity_ByID_404() {
        when(cityRepository.findById(anyLong())).thenReturn(null);
        assertEquals(null, cityService.getById(109));
    }

    @Test
    public void existsByName_test() {
        when(cityRepository.existsByName(anyString())).thenReturn(true);
        assertEquals(true, cityService.existsByName("Musanze"));
    }

    @Test
    public void existsByName_NotFound() {
        when(cityRepository.existsByName(anyString())).thenReturn(false);
        assertEquals(false, cityService.existsByName("kgli"));
    }







}