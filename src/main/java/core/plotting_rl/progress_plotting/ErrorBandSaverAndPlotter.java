package core.plotting_rl.progress_plotting;

import com.google.common.base.Preconditions;
import core.foundation.config.PathAndFile;
import core.foundation.config.PlotConfig;
import core.foundation.configOld.ProjectPropertiesReader;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.plotting_2d.ErrorBandCreator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import javax.swing.*;
import java.awt.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorBandSaverAndPlotter {

    public static void showAndSave(ErrorBandCreator creator, PathAndFile pathAndFile) {
        Preconditions.checkArgument(pathAndFile.path() != null, "pathPics is null");
        Preconditions.checkArgument(pathAndFile.name() != null, "measure is null");
        SwingUtilities.invokeLater(() -> {
            JFrame frame = creator.createFrame();
            frame.setVisible(true);
            creator.saveAsPicture(pathAndFile.fullName());
        });
    }

    @SneakyThrows
    public static PlotSettings getSettings(String yLabel,
                                           String xLabel,
                                           boolean showLegend,
                                           boolean showMarker,
                                           PlotConfig plotConfig) {
                return PlotSettings.ofDefaults()
                .withAxisTicksFont(plotConfig.fontLarge())
                .withAxisTitleFont(plotConfig.fontLargeBold())
                .withLegendTextFont(plotConfig.fontLargeBold())
                .withMarkeSize(10)
                .withWidth(plotConfig.width()).withHeight(plotConfig.height())
                .withTitle("").withYAxisLabel(yLabel).withXAxisLabel(xLabel)
                .withShowLegend(showLegend).withShowMarker(showMarker);
    }

}
