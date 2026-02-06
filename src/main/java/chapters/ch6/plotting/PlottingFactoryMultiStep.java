package chapters.ch6.plotting;

import core.foundation.config.ConfigFactory;
import core.foundation.configOld.ProjectPropertiesReader;
import core.plotting_core.base.shared.PlotSettings;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import java.awt.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlottingFactoryMultiStep {

    @SneakyThrows
    public static PlotSettings getFrameSettings(String yLabel, String xLabel) {
        var plotConfig= ConfigFactory.plotConfig();
        return PlotSettings.ofDefaults()
                .withLineWidth(2).withShowMarker(true)
                .withAxisTicksFont(plotConfig.fontLargePlain())
                .withAxisTitleFont(plotConfig.fontLargeBold())
                .withWidth(plotConfig.frameWidth()).withHeight(plotConfig.frameHeight())
                .withTitle("").withYAxisLabel(yLabel).withXAxisLabel(xLabel).withShowLegend(false);
    }
}
