package setup;

import core.foundation.config.PropertiesLoader;
import core.foundation.config.PlotConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPropertiesLoader {
    public static final int DEFAULT = 0;
    public static final int IN_FILE = 500;
    PropertiesLoader loader;

    @BeforeEach
    void init() {
        loader = PropertiesLoader.createForRelexp();
    }

    @Test
    void correctPlotConfig() {
        var props = loader.loadProperties();
        var plotCfg = PlotConfig.extract(props);
        Assertions.assertEquals(IN_FILE, plotCfg.fixedNumberForTest());
        Assertions.assertNotEquals(DEFAULT, plotCfg.fixedNumberForTest());
    }

}
