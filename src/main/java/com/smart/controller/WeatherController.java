package com.smart.controller;

import com.smart.entities.Weatherreport;
import com.smart.exceptionhandler.CityNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class WeatherController {

    @GetMapping("/current-weather")
    @Cacheable(value="current-weather")
    public String getCurrentWeather(@RequestParam("city")String city) throws CityNotFoundException {
        try {
        if(city.isEmpty()){
            throw new CityNotFoundException("please enter the correct city");
        }
        HttpHeaders headers =new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        String uri = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=aba4f9b66f164254f0966203cf710433";
        RestTemplate restTemplate = new RestTemplate();


            Weatherreport weatherreport = restTemplate.getForObject(uri,Weatherreport.class);

            return "City = "+weatherreport.getName()+", Temperature = "+weatherreport.getMain().getTemp()+", Humidity = "+weatherreport.getMain().getHumidity()+", Wind Speed = "+weatherreport.getWind().getSpeed();
        }catch (CityNotFoundException cnfe){
            return cnfe.getMessage();
        }catch (Exception e){
            System.out.println(e.getMessage()+"-----------------------------");
            return  "Something went wrong";
        }



    }
}
