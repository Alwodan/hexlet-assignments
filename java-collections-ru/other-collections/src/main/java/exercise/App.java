package exercise;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

// BEGIN
public class App {
    public static Map<String, String> genDiff(Map<String, Object> data1, Map<String, Object> data2) {
        Set<String> keys = new TreeSet<>(data1.keySet());
        keys.addAll(data2.keySet());
        Map<String, String> result = new HashMap<>();

        keys.forEach(key -> {
            if (data1.containsKey(key) && !data2.containsKey(key)) {
                result.put(key, "deleted");
            } else if (!data1.containsKey(key) && data2.containsKey(key)) {
                result.put(key, "added");
            } else if (data1.get(key).equals(data2.get(key))) {
                result.put(key, "unchanged");
            } else if (!data1.get(key).equals(data2.get(key))) {
                result.put(key, "changed");
            }
        });
        return result;
    }
}
//END
