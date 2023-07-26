package exercise;

import exercise.daytimes.Daytime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// BEGIN
@RestController
public class WelcomeController {
    @Autowired
    Meal meal;

    @Autowired
    Daytime timeOfDay;

    @GetMapping("/daytime")
    public String dayTime() {
        return "It is " + timeOfDay.getName() + " now. Enjoy your " + meal.getMealForDaytime(timeOfDay.getName());
    }
}
// END
