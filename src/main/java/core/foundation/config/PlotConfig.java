package core.foundation.config;


import core.foundation.util.cond.Conditionals;
import lombok.extern.java.Log;

import java.awt.*;
import java.util.Properties;

@Log
public record PlotConfig(
        int width,
        int height,
        int fixedNumberForTest,
        int xyChartWidth1Col,
        int xyChartWidth2Col,
        int xyChartWidth3Col,
        int xyChartHeight,
        Font font
) {

    public static PlotConfig extract(Properties props) {
        Conditionals.executeIfTrue(props == null,
                () -> log.info("properties is null, using defaults"));

        return (props == null)
                ? PlotConfig.defaults()
                : PlotConfig.extract(new ConfigReader(props));
    }

    public static PlotConfig extract(ConfigReader r) {
        return new PlotConfig(
                r.requireInt("plot.width"),
                r.requireInt("plot.height"),
                r.requireInt("fixedNumberForTest"),
                r.requireInt("xyChart.width1Col"),
                r.requireInt("xyChart.width2Col"),
                r.requireInt("xyChart.width3Col"),
                r.requireInt("xyChart.height"),
                new Font(r.requireStr("fontName"),  Font.PLAIN, r.requireInt("fontSize"))
        );
    }

    public static PlotConfig defaults() {
        return new PlotConfig(800, 500, 0, 800, 800, 800, 500,new Font("Arial",  Font.PLAIN, 12));
    }
}
