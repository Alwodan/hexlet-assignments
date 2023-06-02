package exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

// BEGIN
public class App {
    public static void swapKeyValue(KeyValueStorage map) {
        List<Entry<String, String>> kal = new ArrayList<>(map.toMap().entrySet());

        for (Entry<String, String> set : kal) {
            map.unset(set.getKey());
            map.set(set.getValue(), set.getKey());
        }
    }
}
// END
