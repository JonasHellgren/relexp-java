package core.plotting_rl.progress_plotting;

import com.google.common.base.Preconditions;
import core.foundation.config.PathAndFile;
import core.foundation.configOld.ProjectPropertiesReader;
import core.plotting.base.shared.PlotSettings;
import core.plotting.plotting_2d.ErrorBandCreator;
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
    public static PlotSettings getSettings(String yLabel, String xLabel, boolean showLegend, boolean showMarker) {
        var weight = ProjectPropertiesReader.create().frameWidth2Col();
        var height = ProjectPropertiesReader.create().frameHeight();
        return PlotSettings.ofDefaults()
                .withAxisTicksFont(new Font("Arial", Font.PLAIN, 24))
                .withAxisTitleFont(new Font("Arial", Font.BOLD, 24))
                .withLegendTextFont(new Font("Arial", Font.BOLD, 24))
                .withMarkeSize(10)
                .withWidth(weight).withHeight(height)
                .withTitle("").withYAxisLabel(yLabel).withXAxisLabel(xLabel)
                .withShowLegend(showLegend).withShowMarker(showMarker);
    }

}
