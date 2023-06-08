package exercise;

import lombok.SneakyThrows;

import java.nio.file.Path;
import java.nio.file.Files;

// BEGIN
public class App {
    @SneakyThrows
    public static void save(Path path, Car car) {
        Files.write(path, car.serialize().getBytes());
    }

    @SneakyThrows
    public static Car extract(Path path) {
        String json = new String(Files.readAllBytes(path));
        return Car.unserialize(json);
    }
}
// END
