package core.foundation.config;


import core.foundation.util.cond.ConditionalsUtil;
import lombok.extern.java.Log;

import java.awt.*;
import java.util.Properties;

@Log
public record PlotConfig(
        int width,
        int height,
        int frameWidth,
        int frameHeight,
        int fixedNumberForTest,
        int xyChartWidth1Col,
        int xyChartWidth2Col,
        int xyChartWidth3Col,
        int xyChartHeight,
        Font font,
        Font fontBold,
        Font fontLarge,
        Font fontLargeBold,
        Font fontLargePlain
) {

    public static final Font FONT = new Font("Arial", Font.PLAIN, 12);

    public static PlotConfig extract(Properties props) {
        ConditionalsUtil.executeIfTrue(props == null,
                () -> log.info("properties is null, using defaults"));

        return (props == null)
                ? PlotConfig.defaults()
                : PlotConfig.extract(new ConfigReader(props));
    }

    public static PlotConfig extract(ConfigReader r) {
        return new PlotConfig(
                r.requireInt("plot.width"),
                r.requireInt("plot.height"),
                r.requireInt("frame.width"),
                r.requireInt("frame.height"),
                r.requireInt("fixedNumberForTest"),
                r.requireInt("xyChart.width1Col"),
                r.requireInt("xyChart.width2Col"),
                r.requireInt("xyChart.width3Col"),
                r.requireInt("xyChart.height"),
                new Font(r.requireStr("fontName"), Font.PLAIN, r.requireInt("fontSize")),
                new Font(r.requireStr("fontName"), Font.BOLD, r.requireInt("fontSize")),
                new Font(r.requireStr("fontName"), Font.PLAIN, r.requireInt("fontSizeLarge")),
                new Font(r.requireStr("fontName"), Font.BOLD, r.requireInt("fontSizeLarge")),
                new Font(r.requireStr("fontName"), Font.PLAIN, r.requireInt("fontSizeLarge"))
        );
    }

    public static PlotConfig defaults() {
        return new PlotConfig(800, 500,800, 500, 0, 800, 800, 800, 500,
                FONT, FONT, FONT, FONT,FONT);
    }

}
