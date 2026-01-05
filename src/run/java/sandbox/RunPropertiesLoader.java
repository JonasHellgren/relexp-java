package sandbox;

import core.foundation.config.PathPicsConfig;
import core.foundation.config.PropertiesLoader;
import core.foundation.config.PlotConfig;

public class RunPropertiesLoader {
    public static void main(String[] args) {
        var loader = PropertiesLoader.createForRelexp();
        var plotCfg = PlotConfig.extract(loader.loadProperties());
        System.out.println("plotCfg = " + plotCfg);

        var picPathCfg = PathPicsConfig.extract(loader.loadProperties());

        System.out.println("picPathCfg = " + picPathCfg);


    }
}
