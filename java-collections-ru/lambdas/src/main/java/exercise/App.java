package exercise;

import java.util.Arrays;
import java.util.stream.Stream;

// BEGIN
public class App {
    public static String[][] enlargeArrayImage(String[][] image) {
        return Arrays.stream(image)
                .map(arr -> Stream.of(arr).flatMap(elem -> Stream.of(elem, elem)).toArray(String[]::new))
                .flatMap(arr -> Stream.of(arr, arr))
                .toArray(String[][]::new);
    }
}
// END
