package core.foundation.config;

import java.util.Properties;

public final class ConfigReader {

    private final Properties p;

    public ConfigReader(Properties p) {
        this.p = p;
    }

    public int requireInt(String key) {
        String v = p.getProperty(key);
        if (v == null)
            throw new RuntimeException("Missing key: " + key);
        return Integer.parseInt(v);
    }

    public boolean requireBool(String key) {
        String v = p.getProperty(key);
        if (v == null)
            throw new RuntimeException("Missing key: " + key);
        return Boolean.parseBoolean(v);
    }
}
