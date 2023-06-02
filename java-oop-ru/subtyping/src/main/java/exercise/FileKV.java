package exercise;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// BEGIN
public class FileKV implements KeyValueStorage {
    File file;
    String path;
    Map<String, String> map;

    public FileKV(String path, Map<String, String> map) throws IOException {
        file = new File(path);
        this.path = path;
        this.map = new HashMap<>(map);
        file.createNewFile();
        Utils.writeFile(path, Utils.serialize(this.map));
    }

    @Override
    public void set(String key, String value) {
        map = new HashMap<>(Utils.unserialize(Utils.readFile(path)));
        map.put(key, value);
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Utils.writeFile(path, Utils.serialize(this.map));
    }

    @Override
    public void unset(String key) {
        map = new HashMap<>(Utils.unserialize(Utils.readFile(path)));
        map.remove(key);
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Utils.writeFile(path, Utils.serialize(this.map));
    }

    @Override
    public String get(String key, String defaultValue) {
        map = new HashMap<>(Utils.unserialize(Utils.readFile(path)));
        return map.getOrDefault(key, defaultValue);
    }

    @Override
    public Map<String, String> toMap() {
        return new HashMap<>(Utils.unserialize(Utils.readFile(path)));
    }
}
// END
