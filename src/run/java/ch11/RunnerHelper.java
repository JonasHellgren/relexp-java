package ch11;

import chapters.ch11.domain.agent.core.AgentLunar;
import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.core.EnvironmentLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.environment.startstate_suppliers.StartStateSupplierI;
import chapters.ch11.domain.environment.startstate_suppliers.StartStateSupplierRandomAndClipped;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import chapters.ch11.domain.trainer.core.TrainerI;
import chapters.ch11.domain.trainer.param.TrainerParameters;
import chapters.ch11.helper.AgentEvaluator;
import chapters.ch11.plotting.PlotterHeatMapsAgent;
import core.foundation.configOld.ProjectPropertiesReader;
import core.foundation.util.collections.ListConverterUtil;
import core.foundation.util.collections.ListCreatorUtil;
import core.foundation.util.collections.ListUtil;
import core.plotting_core.chart_plotting.ChartSaverAndPlotter;
import core.plotting_rl.progress_plotting.PlotterProgressMeasures;
import core.plotting_rl.progress_plotting.ProgressMeasureEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.math3.util.Pair;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RunnerHelper {

    public static final String FILE_NAME_N_STEP_EVAL = "lunar_step_horizon_evaluator.png";
    public static final double MAX_START_SPD = -2d;

    record ResultsNStep(Map<Integer, List<Double>> resultMap) {
        public static ResultsNStep empty() {
            return new ResultsNStep(new HashMap<>());
        }

        public void add(int nSteps, double sumRewardAverage) {
            resultMap.computeIfAbsent(nSteps, k -> ListCreatorUtil.emptyDouble()).add(sumRewardAverage);
        }

        public double average(int nSteps) {
            return ListUtil.findAverage(resultMap.get(nSteps)).orElseThrow();
        }

        public double maxMinusMin(int nSteps) {
            double max = ListUtil.findMax(resultMap.get(nSteps)).orElseThrow();
            double min = ListUtil.findMin(resultMap.get(nSteps)).orElseThrow();
            return max - min;
        }
    }

    @SneakyThrows
    static void plotNStepResults(ResultsNStep results, List<Integer> nStepsList) {
        var xList = ListConverterUtil.integer2Double(nStepsList);
        var yList = nStepsList.stream()
                .map(nSteps -> results.average(nSteps)).toList();
        var errList = nStepsList.stream()
                .map(nSteps -> results.maxMinusMin(nSteps)).toList();
        var chart = new XYChartBuilder()
                .width(600).height(200)
                .title("").xAxisTitle("N - Step horizon").yAxisTitle("Average return")
                .build();
        var styler = chart.getStyler();
        styler.setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        styler.setChartTitleVisible(false);
        styler.setLegendVisible(false);
        styler.setAxisTitlesVisible(true);
        styler.setXAxisDecimalPattern("0");
        styler.setChartBackgroundColor(Color.WHITE);
        XYSeries series = chart.addSeries("a", xList, yList, errList);
        series.setMarkerColor(Color.GRAY);
        series.setMarker(SeriesMarkers.SQUARE);
        ChartSaverAndPlotter.showChartSaveInFolderActorCritic(chart,FILE_NAME_N_STEP_EVAL);
    }


    @SneakyThrows
    public static void evaluate(TrainerDependencies trainerDependencies,
                                StartStateSupplierI startStateSupplier,
                                int nEvals) {
        trainerDependencies = trainerDependencies.withStartStateSupplier(startStateSupplier);
        var evaluatorFails = AgentEvaluator.of(trainerDependencies);
        double fractionFails = evaluatorFails.fractionFails(nEvals);
        System.out.println("fractionFails = " + fractionFails);
    }

    static TrainerDependencies getDependencies(LunarParameters ep, int stepHorizon, int nEpisodes) {
        var p = AgentParameters.newDefault(ep);
        var tp = TrainerParameters.newDefault().withNStepsHorizon(stepHorizon).withNEpisodes(nEpisodes);
        return TrainerDependencies.builder()
                .agent(AgentLunar.zeroWeights(p, ep))
                .environment(EnvironmentLunar.of(ep))
                .trainerParameters(tp)
                .startStateSupplier(StartStateSupplierRandomAndClipped.create(
                        ep, Pair.create(ep.yMax(), ep.yMax()), Pair.create(-ep.spdMax(), MAX_START_SPD)))
                .build();
    }

    @SneakyThrows
    static void plot(TrainerI trainer, TrainerDependencies trainerDependencies) {
        var pathPics = ProjectPropertiesReader.create().pathActorCriticPics();
        var progressPlotter = PlotterProgressMeasures.of(trainer.getRecorder(), pathPics);
        progressPlotter.plotAndSave(
                List.of(ProgressMeasureEnum.RETURN,
                        ProgressMeasureEnum.STD_ACTOR,
                        ProgressMeasureEnum.TD_BEST_ACTION,
                        ProgressMeasureEnum.GRADIENT_ACTOR_MEAN));
        var agentPlotter = PlotterHeatMapsAgent.of(trainerDependencies);
        agentPlotter.plotAndSaveAll();
    }

    static void simulateTrainedAgent(TrainerDependencies trainerDependencies,
                                     double startHeight,
                                     double startSpd,
                                     String fileName) {

        var ep = trainerDependencies.environment().getParameters();
        trainerDependencies = trainerDependencies.withStartStateSupplier(new StartStateSupplierRandomAndClipped(
        ep, Pair.create(startHeight, startHeight), Pair.create(startSpd, startSpd)));
        var evaluatorSim = AgentEvaluator.of(trainerDependencies);
        evaluatorSim.plotAndSavePicFromSimulation(fileName);

    }

}
