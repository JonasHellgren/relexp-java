package chapters.ch6.plotting;

import core.foundation.configOld.ProjectPropertiesReader;
import core.plotting.base.shared.PlotSettings;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import java.awt.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PlottingFactoryMultiStep {

    @SneakyThrows
    public static PlotSettings getFrameSettings(String yLabel, String xLabel) {
        var weight = ProjectPropertiesReader.create().frameWidth2Col();
        var height = ProjectPropertiesReader.create().frameHeight();
        return PlotSettings.ofDefaults()
                .withLineWidth(2).withShowMarker(true)
                .withAxisTicksFont(new Font("Arial", Font.PLAIN, 24))
                .withAxisTitleFont(new Font("Arial", Font.BOLD, 24))
                .withWidth(weight).withHeight(height)
                .withTitle("").withYAxisLabel(yLabel).withXAxisLabel(xLabel).withShowLegend(false);
    }
}
