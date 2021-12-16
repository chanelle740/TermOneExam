package rw.ac.rca.termOneExam.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.dto.CreateCityDTO;
import rw.ac.rca.termOneExam.service.CityService;
import rw.ac.rca.termOneExam.utils.CustomException;
import rw.ac.rca.termOneExam.utils.JsonUtil;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CityController.class)
public class CityControllerTest {
    @MockBean
    private CityService cityServiceMock;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllCities_Success() throws Exception {
        List<City> asList = Arrays.asList(new City(1,"Kigali City",23,23.90),
                new City(2,"Kigali City",23,89.90));

        when(cityServiceMock.getAll()).thenReturn(asList);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/cities/all")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1, \"name\":\"Kigali City\",\"weather\":23,\"fahrenheit\":23.90} ,{\"id\":2, \"name\":\"Kigali City\",\"weather\":23,\"fahrenheit\":89.90} ]"))
                .andReturn();

    }


    @Test
    public void getOneCity_Success() throws Exception {
        City city =new City(2,"Kigali City",23,89.90);
        when(cityServiceMock.getById(2)).thenReturn(java.util.Optional.of(city));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/cities/id/2")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":2, \"name\":\"Kigali City\",\"weather\":23,\"fahrenheit\":89.90}"))
                .andReturn();
    }

    @Test
    public void getOneCity_404() throws Exception {
        City city =new City(2,"Kigali City",23,89.90);
        when(cityServiceMock.getById(city.getId())).thenReturn(java.util.Optional.of(city));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/cities/id/1")
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc
                .perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"status\":false,\"message\":\"City not found\"}"))
                .andReturn();
    }


    @Test
    public void createCityTest() throws Exception{
        City city = new City(2,"Kigali City",23,89.90);
        when(cityServiceMock.save(new CreateCityDTO())).thenReturn(city);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/api/cities/add")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(city));

        MvcResult result = mockMvc
                .perform(request)
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void create_test_duplicateCity() throws Exception {

        when(cityServiceMock.save(any(CreateCityDTO.class))).thenThrow(new CustomException("City name already registered", HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/cities/id/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":2, \"name\":\"Kigali City\",\"weather\":23,\"fahrenheit\":89.90}");

        mockMvc
                .perform(request)
                .andExpect(status()
                        .isNotFound())
                .andExpect(content()
                        .string("City name already registered"))
                .andReturn();




    }


}
