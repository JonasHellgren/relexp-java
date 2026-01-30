package ch11;

import chapters.ch11.factory.DependencyFactory;
import chapters.ch11.factory.LunarEnvParamsFactory;
import chapters.ch11.domain.environment.core.RadialBasisAdapter;
import com.google.common.collect.Lists;
import core.foundation.configOld.ProjectPropertiesReader;
import core.nextlevelrl.radial_basis.RbfNetwork;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.chart_plotting.ChartSaverAndPlotter;
import core.plotting_core.plotting_2d.ScatterWithLineChartCreator;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.knowm.xchart.XYChart;
import java.util.List;


public class RunnerKernelsPlotter {
    public static final String FILE_NAME = "kernels_centers";

    record XYPositions(List<Double> x, List<Double> y) {

        public static XYPositions of(int nKernels) {
            return new XYPositions(
                    Lists.newArrayListWithCapacity(nKernels),
                    Lists.newArrayListWithCapacity(nKernels));
        }
    }

    public static void main(String[] args) {
        var xyPositions = getKernelXyPositions(getRbfNetwork());
        ChartSaverAndPlotter.showChartSaveInFolderActorCritic(getChart(xyPositions), FILE_NAME);
    }

    @SneakyThrows
    private static XYChart getChart(XYPositions xyPositions) {
        var properties= ProjectPropertiesReader.create();
        var settings= PlotSettings.ofDefaults()
                .withWidth(properties.xyChartWidth1Col()).withHeight(properties.xyChartHeight())
                .withTitle("Kernel center positions")
                .withXAxisLabel("Speed (m/s)").withYAxisLabel("Position (m)");
        var chartCreator= ScatterWithLineChartCreator.of(settings);
        chartCreator.addScatter(xyPositions.x(), xyPositions.y());
        return chartCreator.create();
    }

    @NotNull
    private static XYPositions getKernelXyPositions(RbfNetwork rbf) {
        var xyPositions=XYPositions.of(rbf.nKernels());
        for (int i = 0; i < rbf.nKernels(); i++) {
            var kernel= rbf.getKernels().get(i);
            xyPositions.x.add(RadialBasisAdapter.asSpd(kernel));
            xyPositions.y.add(RadialBasisAdapter.asYPos(kernel));
        }
        return xyPositions;
    }

    private static RbfNetwork getRbfNetwork() {
        int dummyValue=0;
        var ep = LunarEnvParamsFactory.produceDefault();
        var tp = DependencyFactory.produce(ep, dummyValue, dummyValue);
        var agent= tp.agent();
        var memory=agent.getCriticMemory();
        return memory.getMemory();
    }

}
