package core.foundation.config;

import com.google.common.base.Preconditions;
import org.bytedeco.opencv.presets.opencv_core;

import java.util.Properties;

public final class ConfigReader {

    private final Properties p;

    public ConfigReader(Properties p) {
        this.p = p;
    }

    public int requireInt(String key) {
        String v = p.getProperty(key);
        validate(key, v);
        return Integer.parseInt(v);
    }

    public String requireStr(String key) {
        String v = p.getProperty(key);
        validate(key, v);
        return v;
    }

    public boolean requireBool(String key) {
        String v = p.getProperty(key);
        validate(key, v);
        return Boolean.parseBoolean(v);
    }


    private static void validate(String key, String v) {
        Preconditions.checkArgument(v != null, "Missing key: " + key);
    }
}
