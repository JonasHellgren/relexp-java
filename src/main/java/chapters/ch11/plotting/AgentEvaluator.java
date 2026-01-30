package chapters.ch11.plotting;

import chapters.ch11.domain.environment.core.EnvironmentLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.environment.startstate_suppliers.StartStateSupplierI;
import chapters.ch11.domain.trainer.core.ExperienceLunar;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.domain.trainer.core.EpisodeCreator;
import chapters.ch11.domain.trainer.core.EpisodeInfo;
import core.foundation.config.PathAndFile;
import core.foundation.config.PlotConfig;
import core.foundation.gadget.cond.Counter;
import core.foundation.util.cond.ConditionalsUtil;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.chart_plotting.ChartSaver;
import core.plotting_core.plotting_2d.ManyLinesChartCreator;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import java.awt.*;
import java.util.List;

/**
 * This class is used to evaluate the performance of an agent in the Lunar Lander domain.
 * It provides methods to calculate the fraction of failed episodes, and to plot and save a
 * chart of a single simulation.
 */

@AllArgsConstructor
@Log
public class AgentEvaluator {
    private final TrainerDependencies dependencies;
    public static AgentEvaluator of(TrainerDependencies dependencies) {
        return new AgentEvaluator(dependencies);
    }

    public static AgentEvaluator of(TrainerDependencies trainerDependencies,
                                    StartStateSupplierI startStateSupplier) {
        trainerDependencies = trainerDependencies.withStartStateSupplier(startStateSupplier);
        return AgentEvaluator.of(trainerDependencies);
    }

    /**
     * Calculates the fraction of failed episodes out of a total number of evaluations.
     *
     * @param nEvals the total number of evaluations to perform
     * @return the fraction of failed episodes
     */
    public double fractionFails(int nEvals) {
        var creator = EpisodeCreator.of(dependencies);
        var failCounter = Counter.empty();
        var evalCounter = Counter.ofMaxCount(nEvals);
        while (evalCounter.isNotExceeded()) {
            var experiencesNotExploring = creator.createNotExploring();
            var info = EpisodeInfo.of(experiencesNotExploring);
            var startExperience = info.startExperience();
            var endExperience = info.endExperience();
            increaseFailCounterAndLogIfFailedEnd(endExperience, failCounter, startExperience);
            evalCounter.increase();
        }
        return (double) failCounter.getCount() / nEvals;
    }

    /**
     * Plots and saves a chart of a single simulation.
     * <p>
     * This method creates a chart with four lines: force, acceleration, speed, and position.
     * The chart is then saved as a PNG file at the specified path and displayed using a Swing wrapper.
     */
    @SneakyThrows
    public void plotAndSavePicFromSimulation(PathAndFile pathAndFile, PlotConfig plotConfig) {
        var creator = EpisodeCreator.of(dependencies);
        var experiences = creator.createNotExploring();
        var ep = dependencies.environment().getParameters();
        var speeds = EpisodeInfo.of(experiences).speeds();
        var accelerations = EpisodeInfo.of(experiences)
                .accelerations((EnvironmentLunar) dependencies.environment());
        var positions = EpisodeInfo.of(experiences).positions();
        var cc = getChartCreator(experiences, ep, plotConfig);
        cc.addLine("Acc.(m/s2)", accelerations);
        cc.addLine("Speed (m/s)", speeds);
        cc.addLine("Pos (m)", positions);
        var chart = cc.create();
        ChartSaver.saveAndShowXYChart(chart, pathAndFile);
    }


    private static void increaseFailCounterAndLogIfFailedEnd(ExperienceLunar endExperience,
                                                             Counter failCounter,
                                                             ExperienceLunar startExperience) {
        ConditionalsUtil.executeIfTrue(endExperience.isTransitionToFail(), () -> {
            failCounter.increase();
            log.info("FAIL: (start,end)=("
                    + startExperience.toStringShort()
                    + "," + endExperience.toStringShort() + ")");
        });
    }


    private static ManyLinesChartCreator getChartCreator(
            List<ExperienceLunar> experiencesNotExploring, LunarParameters ep, PlotConfig plotConfig) {
        var width = plotConfig.xyChartWidth2Col();
        var height = plotConfig.xyChartHeight();
        Font ticksFont = plotConfig.fontBold();
        Font axisTitleFont = plotConfig.fontBold();
        var time = EpisodeInfo.of(experiencesNotExploring).times(ep.dt());
        return ManyLinesChartCreator.of(
                PlotSettings.ofDefaults()
                        .withTitle("").withXAxisLabel("Time (s)").withYAxisLabel("")
                        .withDefinedSpaceBetweenXTicks(false)
                        .withWidth(width)
                        .withHeight(height)
                        .withLegendTextFont(ticksFont)
                        .withAxisTitleFont(axisTitleFont)
                        .withAxisTicksFont(ticksFont)
                        .withColorRangeSeries(new Color[]{Color.BLACK, Color.GRAY, Color.LIGHT_GRAY}),
                time);
    }


}
