package exercise;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

class App {
    private static final Logger LOGGER = Logger.getLogger("AppLogger");

    // BEGIN
    public static Map<String, Integer> getMinMax(int[] arr) {
        Map<String, Integer> map = new HashMap<>();
        MaxThread maxThread = new MaxThread(arr);
        MinThread minThread = new MinThread(arr);
        maxThread.start();
        minThread.start();
        try {
            maxThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            minThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        map.put("min", minThread.getResult());
        map.put("max", maxThread.getResult());
        return map;
    }
    // END
}
