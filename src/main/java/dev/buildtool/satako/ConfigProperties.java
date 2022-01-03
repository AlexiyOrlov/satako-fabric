package dev.buildtool.satako;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class ConfigProperties {

    private final HashMap<String, String> properties;
    private Path file;

    public ConfigProperties(Path file) {
        this.properties = new HashMap<>(3);
        this.file = file;
        if (!Files.exists(file)) {
            try {
                this.file = Files.createFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                List<String> strings = Files.readAllLines(file);
                strings.forEach(s -> {
                    if (!s.isEmpty()) {
                        String[] strs = s.split("=");
                        properties.put(strs[0], strs[1]);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getProperty(String key, Object object) {
        key = key.replace(' ', '_');
        properties.putIfAbsent(key, object.toString());
        return properties.get(key);
    }

    public void save() {
        try {
            Files.write(file, properties.entrySet().stream().map(stringObjectEntry -> stringObjectEntry.getKey() + "=" + stringObjectEntry.getValue()).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
