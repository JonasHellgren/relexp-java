package core.foundation.config;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ConfigFactory {


    public static PathPicsConfig pathPicsConfig() {
        var loader = getPropertiesLoader();
        return PathPicsConfig.extract(loader.loadProperties());
    }

    @NotNull
    private static PropertiesLoader getPropertiesLoader() {
        return PropertiesLoader.createForRelexp();
    }

    public static PlotConfig plotConfig() {
        var loader = getPropertiesLoader();
        return PlotConfig.extract(loader.loadProperties());
    }
}
