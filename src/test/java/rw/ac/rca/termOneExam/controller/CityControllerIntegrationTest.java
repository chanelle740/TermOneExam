package rw.ac.rca.termOneExam.controller;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.utils.APICustomResponse;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@WebMvcTest(CityController.class)
public class CityControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl = "/api/cities";

    @Test
    public void getAll_success() throws JSONException {
        String res = this.restTemplate.getForObject(baseUrl+"/all",String.class);

        JSONAssert.assertEquals("[{id:101},{id:102},{id:103},{id:104}]",res,false);

    }

    @Test
    public void getById_success() throws JSONException {
        ResponseEntity<City> myCity = this.restTemplate.getForEntity(baseUrl+"/id/101",City.class);

        assertTrue(myCity.getStatusCode().is2xxSuccessful());
        assertEquals("Kigali",myCity.getBody().getName());
        assertEquals(24,myCity.getBody().getWeather());

    }

    @Test
    public void getById_failure() throws JSONException {
        ResponseEntity<APICustomResponse> res = this.restTemplate.getForEntity(baseUrl+"/id/500",APICustomResponse.class);

        assertTrue(res.getStatusCodeValue()==404);
        assertFalse(res.getBody().isStatus());
        assertEquals("City can't be found by id 500",res.getBody().getMessage());

    }

    @Test
    public void addCity_success() throws JSONException {
        City requestBody = new City(105,"Nairobi",70,12);
        ResponseEntity<City> myCity = this.restTemplate.postForEntity(baseUrl+"/add",requestBody, City.class);

        assertTrue(myCity.getStatusCode().is2xxSuccessful());
        assertEquals("Nairobi",myCity.getBody().getName());

    }

    @Test
    public void addCity_failure() throws JSONException {
        City requestBody = new City(101,"Kigali",70,12);
        ResponseEntity<APICustomResponse> res = this.restTemplate.postForEntity(baseUrl+"/add",requestBody,APICustomResponse.class);

        assertTrue(res.getStatusCodeValue()==400);
        assertFalse(res.getBody().isStatus());
        assertEquals("City name 'Kigali' is there already",res.getBody().getMessage());

    }

}
