package exercise;

import java.util.ArrayList;
import java.util.List;

// BEGIN
public class App {
    public static List<String> buildApartmentsList(List<Home> homes, int n) {
        if (n > homes.size()) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>(homes.stream().sorted().map(Object::toString).toList());

        return result.subList(0, n);
    }
}
// END
