package exercise;

import java.util.HashMap;
import java.util.Map;

// BEGIN
public class App {
    public static Map<String, Integer> getWordCount(String sentence) {
        if (sentence.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, Integer> result = new HashMap<>();
        String[] words = sentence.split(" ");
        for (String word : words) {
            result.merge(word, 1, Integer::sum);
        }
        return result;
    }

    public static String toString(Map<String, Integer> map) {
        if (map.isEmpty()) {
            return "{}";
        }
        StringBuilder result = new StringBuilder("{\n");
        for (String key : map.keySet()) {
            result.append("  ").append(key).append(": ").append(map.get(key)).append("\n");
        }
        return result.toString() + "}";
    }
}
//END
