package exercise;

import java.time.LocalDateTime;

import exercise.daytimes.Daytime;
import exercise.daytimes.Morning;
import exercise.daytimes.Day;
import exercise.daytimes.Evening;
import exercise.daytimes.Night;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// BEGIN
@Configuration
public class MyApplicationConfig {

    @Bean
    public Daytime timeOfDay() {
        LocalDateTime time = LocalDateTime.now();
        if (isBetween(6, 12, time)) {
            return new Morning();
        } else if (isBetween(12, 18, time)) {
            return new Day();
        } else if (isBetween(18, 23, time)) {
            return new Evening();
        } else {
            return new Night();
        }
    }

    private static boolean isBetween(int hour1, int hour2, LocalDateTime time) {
        return time.isAfter(LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), hour1, 0)) &&
                time.isBefore(LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), hour2, 0));
    }
}
// END
