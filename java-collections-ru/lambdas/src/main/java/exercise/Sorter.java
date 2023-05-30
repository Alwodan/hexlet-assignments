package exercise;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// BEGIN
public class Sorter {
    public static List<String> takeOldestMans(List<Map<String, String>> users) {
        if (users.isEmpty()) {
            return new ArrayList<>();
        }
        return users.stream()
                .filter(m -> m.get("gender").equals("male"))
                .sorted(Comparator.comparing(m -> LocalDate.parse(m.get("birthday"))))
                .map(m -> m.get("name"))
                .collect(Collectors.toList());
    }
}
// END
