package chapters.ch11.helper;

import chapters.ch11.domain.environment.core.EnvironmentLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.trainer.core.ExperienceLunar;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.gadget.cond.Counter;
import core.foundation.util.cond.Conditionals;
import core.plotting.base.shared.FormattedAsString;
import core.plotting.base.shared.PlotSettings;
import core.plotting.chart_plotting.ChartSaverAndPlotter;
import core.plotting.plotting_2d.ManyLinesChartCreator;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * This class is used to evaluate the performance of an agent in the Lunar Lander domain.
 * It provides methods to calculate the fraction of failed episodes, and to plot and save a
 * chart of a single simulation.
 */

@AllArgsConstructor
@Log
public class AgentEvaluator {
    public static final int FONT_SIZE_AXIS = 15;

    private final TrainerDependencies dependencies;

    public static AgentEvaluator of(TrainerDependencies dependencies) {
        return new AgentEvaluator(dependencies);
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
            var startExperience= info.startExperience();
            var endExperience = info.endExperience();
            increaseFailCounterAndLogIfFailedEnd(endExperience, failCounter, startExperience);
            evalCounter.increase();
        }
        return (double) failCounter.getCount() / nEvals;
    }
    /**
     * Plots and saves a chart of a single simulation.
     *
     * This method creates a chart with four lines: force, acceleration, speed, and position.
     * The chart is then saved as a PNG file at the specified path and displayed using a Swing wrapper.
     *
     * @param fileName the name of the file to save the chart as
     */
    @SneakyThrows
    public void plotAndSavePicFromSimulation(String fileName) {
        var creator = EpisodeCreator.of(dependencies);
        var experiences = creator.createNotExploring();
        var ep = dependencies.environment().getParameters();
        var speeds = EpisodeInfo.of(experiences).speeds();
        var forces = EpisodeInfo.of(experiences).forces(ep);
        var accelerations = EpisodeInfo.of(experiences)
                .accelerations((EnvironmentLunar) dependencies.environment());
        var positions = EpisodeInfo.of(experiences).positions();
        String title = ""; //""Time end = " + getTimeEndAsString(experiences, ep) + " (s)";
        var cc = getChartCreator(experiences, ep, title);
    //    cc.addLine("Force (kN)", forces);
        cc.addLine("Acc.(m/s2)", accelerations);
        cc.addLine("Speed (m/s)", speeds);
        cc.addLine("Pos (m)", positions);
        var chart = cc.create();
        ChartSaverAndPlotter.showChartSaveInFolderActorCritic(chart, fileName);
    }


    private static void increaseFailCounterAndLogIfFailedEnd(ExperienceLunar endExperience,
                                                             Counter failCounter,
                                                             ExperienceLunar startExperience) {
        Conditionals.executeIfTrue(endExperience.isTransitionToFail(), () -> {
            failCounter.increase();
            log.info("FAIL: (start,end)=("+ startExperience.toStringShort()+
                    "," + endExperience.toStringShort()+")");
        });
    }

    private static String getTimeEndAsString(List<ExperienceLunar> experiencesNotExploring, LunarParameters ep) {
        var info= EpisodeInfo.of(experiencesNotExploring);
        double timeEnd=info.nSteps() * ep.dt();
        return FormattedAsString.getFormattedAsString(timeEnd, "#.#");
    }

    private static ManyLinesChartCreator getChartCreator(
            List<ExperienceLunar> experiencesNotExploring, LunarParameters ep, String title) throws IOException {
        var time = EpisodeInfo.of(experiencesNotExploring).times(ep.dt());
        var width = ProjectPropertiesReader.create().xyChartWidth1Col();
        var height = ProjectPropertiesReader.create().xyChartHeight()*1.5;
        Font ticksFont = new Font("Arial", Font.PLAIN, FONT_SIZE_AXIS);
        Font axisTitleFont = new Font("Arial", Font.BOLD, FONT_SIZE_AXIS);

        return ManyLinesChartCreator.of(
                PlotSettings.ofDefaults()
                        .withTitle(title).withXAxisLabel("Time (s)").withYAxisLabel("")
                        .withDefinedSpaceBetweenXTicks(false)
                        .withWidth(width).withHeight((int) height)
                        .withLegendTextFont(ticksFont)
                        .withAxisTitleFont(axisTitleFont)
                        .withAxisTicksFont(ticksFont)
                        .withColorRangeSeries(
                                new Color[]{Color.BLACK, Color.GRAY, Color.LIGHT_GRAY}),
                time);
    }


}
