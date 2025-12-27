package sandbox;

import core.foundation.config.PropertiesLoader;
import core.plotting.PlotConfig;

public class RunPropertiesLoader {
    public static void main(String[] args) {
        var loader = PropertiesLoader.createForRelexp();
        var props = loader.load();
        var plotCfg = PlotConfig.extract(props);
        System.out.println("plotCfg = " + plotCfg);
    }
}
