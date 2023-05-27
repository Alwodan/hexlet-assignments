package exercise;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Map.Entry;

// BEGIN
public class App {
    public static List<Map<String, String>> findWhere(List<Map<String, String>> books, Map<String, String> where) {
        List<Map<String, String>> result = new ArrayList<>(books);
        for (Map<String, String> book : books) {
            for (Entry<String, String> pair : where.entrySet()) {
                if (!book.get(pair.getKey()).equals(pair.getValue())) {
                    result.remove(book);
                    break;
                }
            }
        }
        return result;
    }
}
//END
