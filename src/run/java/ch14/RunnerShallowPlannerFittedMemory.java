package ch14;

import chapters.ch14.domain.interfaces.LongMemory;
import chapters.ch14.domain.trainer.Trainer;
import chapters.ch14.domain.trainer.TrainerDependencies;
import chapters.ch14.implem.pong.ActionPong;
import chapters.ch14.implem.pong.ExecutorPong;
import chapters.ch14.implem.pong.StateLongPong;
import chapters.ch14.implem.pong.StatePong;
import chapters.ch14.pong_animation.PongGraphicsServer;
import chapters.ch14.factory.FactoryDependencies;
import chapters.ch14.factory.FactoryPlanner;
import chapters.ch14.factory.FactoryPlotData;
import chapters.ch14.plotting.MeasuresCombLPEnum;
import chapters.ch14.plotting.Recorder;
import chapters.ch14.plotting.RecorderPlotter;
import core.foundation.config.ConfigFactory;
import core.foundation.config.PathAndFile;
import core.plotting_core.base.shared.PlotSettings;
import core.plotting_core.chart_saving_and_plotting.ChartSaver;
import core.plotting_core.plotting_3d.HeatMapChartCreator;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.knowm.xchart.SwingWrapper;
import java.io.IOException;
import java.util.List;

/**
 * Skip animation  by comment line with server=...
 */

@Log
public class RunnerShallowPlannerFittedMemory {
    static final int HEAT_MAP_RESOLUTION = 100;
    static final int N_TRIALS = 10;
    static final int MAX_STEPS_PER_TRIAL = 200;
    static final int DEPTH_EXECUTOR = 3;
    static TrainerDependencies<StateLongPong, StatePong, ActionPong> dependencies;
    static PongGraphicsServer server;

    @SneakyThrows
    public static void main(String[] args) {
        var animationConfig=ConfigFactory.getAnimationConfig();
        server = PongGraphicsServer.of(animationConfig.sleepTimeAnimationMs(), animationConfig.socketPort());
        dependencies = FactoryDependencies.forRunning();
        var trainer = Trainer.of(dependencies);
        log.info("Execution of planner with zero memory started");
        runExecutorWithAnimation(dependencies, "execution_empty_memory");
        log.info("Execution finished. Time (s) = " + trainer.timeInSecondsAsString());
        log.info("Training started");
        trainer.resetTimer();
        trainer.train();
        log.info("Training finished. Time (s) = " + trainer.timeInSecondsAsString());
        showMemory(dependencies.longMemory());
        log.info("Execution of planner with long memory started");
        trainer.resetTimer();
        runExecutorWithAnimation(dependencies, "execution_fitted_memory");
        log.info("Execution finished. Time (s) = " + trainer.timeInSecondsAsString());
    }

    private static void runExecutorWithAnimation(TrainerDependencies<StateLongPong, StatePong, ActionPong> dependencies
            , String name) throws IOException {
        var shallowPlanner = FactoryPlanner.forExecutor(
                DEPTH_EXECUTOR,
                dependencies.timeToHitCalculator(),
                dependencies.trainerSettings());
        var executor = ExecutorPong.of(dependencies, shallowPlanner, server, name);
        executor.execute(N_TRIALS, MAX_STEPS_PER_TRIAL);
        plotting(executor.getRecorder(), name);
    }

    @SneakyThrows
    private static void showMemory(LongMemory<StateLongPong> memory) {
        var settings = PlotSettings.defaultBuilder()
                .title("Memory values").showDataValues(false)
                .xAxisLabel("deltaX").yAxisLabel("timeHit (s)")
                .showAxisTicks(true).build();
        var creator = HeatMapChartCreator.of(settings,
                FactoryPlotData.getGridData(memory, HEAT_MAP_RESOLUTION, dependencies.envSettings()),
                FactoryPlotData.getXData(HEAT_MAP_RESOLUTION, dependencies.envSettings()),
                FactoryPlotData.getYData(HEAT_MAP_RESOLUTION, dependencies.envSettings()));
        var chart = creator.create();
        var path = ConfigFactory.pathPicsConfig().ch14();
        ChartSaver.saveHeatMapChart(chart, PathAndFile.ofPng(path, "long_memory"));
        new SwingWrapper<>(chart).displayChart();
    }


    static void plotting(Recorder recorder, String fileName) throws IOException {
        var path=ConfigFactory.pathPicsConfig().ch14();
        var plotConfig = ConfigFactory.plotConfig();
        var plotter = RecorderPlotter.of(recorder, path, fileName, plotConfig);
        plotter.plotAndSave(List.of(MeasuresCombLPEnum.N_STEPS, MeasuresCombLPEnum.SUM_REWARDS));
    }

}
