package core.foundation.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertiesLoader {

    static final String PATH = "src/main/resources/";
    static final String FILENAME = "relexp.properties";
    final Path baseDir;
    String filename;

    public static PropertiesLoader createForRelexp() {
        return of(Path.of(PATH), FILENAME);
    }

    public static PropertiesLoader of(Path baseDir, String filename) {
        return new PropertiesLoader(baseDir, filename);
    }

    public Properties load() {
        Path path = baseDir.resolve(filename);
        if (!Files.exists(path)) {
            return null;
        }

        try (InputStream in = Files.newInputStream(path)) {
            Properties p = new Properties();
            p.load(in);
            return p;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties: " + path, e);
        }
    }

}
