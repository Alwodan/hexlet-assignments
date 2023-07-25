package exercise.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import exercise.CityNotFoundException;
import exercise.model.City;
import exercise.repository.CityRepository;
import exercise.service.WeatherService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class CityController {

    private final CityRepository cityRepository;

    private final WeatherService weatherService;

    // BEGIN
    @GetMapping("/cities/{id}")
    public Map<String, String> getCity(@PathVariable long id) throws JsonProcessingException {
        return weatherService.getWeather(cityRepository.findById(id).orElseThrow(() -> new CityNotFoundException("a")));
    }

    @GetMapping("/search")
    public List<Map<String, String>> searchCity(@RequestParam(name = "name", required = false) String param) throws JsonProcessingException {
        List<City> temp;
        List<Map<String, String>> result = new ArrayList<>();
        if (param == null) {
             temp = cityRepository.findAllByOrderByNameAsc();
        } else {
            temp = cityRepository.findByNameIgnoreCaseStartingWith(param);
        }
        for (City city : temp) {
            Map<String, String> superTemp = new HashMap<>();
            superTemp.put("temperature", weatherService.getWeather(city).get("temperature"));
            superTemp.put("name", city.getName());
            result.add(superTemp);
        }
        return result;
    }
    // END
}

