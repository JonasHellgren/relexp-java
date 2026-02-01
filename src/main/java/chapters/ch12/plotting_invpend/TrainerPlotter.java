package chapters.ch12.plotting_invpend;

import chapters.ch12.domain.inv_pendulum.agent.evaluator.PendulumAgentEvaluator;
import chapters.ch12.domain.inv_pendulum.trainer.core.TrainerPendulum;
import chapters.ch12.factory.ManyLinesChartCreatorFactory;
import core.foundation.config.PlotConfig;
import core.plotting_core.chart_plotting.ChartSaver;
import core.plotting_core.plotting_2d.ManyLinesChartCreator;
import lombok.Builder;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Builder
public class TrainerPlotter {

    static final int N_WINDOWS_FILTERING = 10;
    public static final String PENDULUM_EVALUATION = "pendulum_evaluation_";
    public static final String START_STATE_NAME = "pendulum_action_values_s00_";
    public static final String TRAINING = "training_";

    private final TrainerPendulum trainer;
    private final PlotConfig plotConfig;
    private final String path;
    private final PendulumAgentEvaluator evaluator;


    public void plotTrainEvolution() {
        plotMeasure(MeasurePendulum.RETURN);
        plotMeasure(MeasurePendulum.TIME);
        plotMeasure(MeasurePendulum.LOSS);
        plottingActionValues00(START_STATE_NAME);
    }

    @SneakyThrows
    public void plotMeasure(MeasurePendulum measure) {
        var recorder = trainer.getRecorder();
        var plotter = ErrorBandPlotterNeuralPendulum.ofFiltering(
                recorder, path, TRAINING + measure.name(), N_WINDOWS_FILTERING, plotConfig);
        plotter.plotAndSave(List.of(measure));
    }

    public void plottingActionValues00(String startStateName) {
        String title = "";
        var recorder = trainer.getRecorder();
        var cc = ManyLinesChartCreatorFactory.createChartCreatorForActionValues(
                title, recorder.trajectory(MeasurePendulum.EPISODE), plotConfig);
        cc.addLine("ccw", recorder.trajectory(MeasurePendulum.Q0CCW));
        cc.addLine("n", recorder.trajectory(MeasurePendulum.Q0N));
        cc.addLine("cw", recorder.trajectory(MeasurePendulum.Q0CW));
        showAndSave(cc, startStateName + title);
    }


    public void plotTheta() {
        var recorder = evaluator.getRecorder();
        String name = "Theta (deg)";
        var cc = getManyLinesChartCreator(name, recorder);
        cc.addLine(name, recorder.trajectory(MeasuresPendulumSimulationEnum.ANGLE_DEG));
        showAndSave(cc, PENDULUM_EVALUATION + name);
    }

    public void plotTorque() {
        var recorder = evaluator.getRecorder();
        String name = "Torque (Nm)";
        var cc = getManyLinesChartCreator(name, recorder);
        cc.addLine(name, recorder.trajectory(MeasuresPendulumSimulationEnum.TORQUE));
        showAndSave(cc, PENDULUM_EVALUATION + name);
    }

    public void plotSpd() {
        var recorder = evaluator.getRecorder();
        String name = "Angular speed";
        String title = name + " (deg/s)";
        var cc = getManyLinesChartCreator(title, recorder);
        cc.addLine(name, recorder.trajectory(MeasuresPendulumSimulationEnum.ANGULAR_SPEED_DEG));
        showAndSave(cc, PENDULUM_EVALUATION + name);
    }

    @NotNull
    private ManyLinesChartCreator getManyLinesChartCreator(String title, RecorderPendulumMeasure recorder) {
        return ManyLinesChartCreatorFactory.createChartCreatorForEvaluator(
                title, recorder.trajectory(MeasuresPendulumSimulationEnum.TIME_MS), plotConfig);
    }

    private void showAndSave(ManyLinesChartCreator cc, String fileName) {
        ChartSaver.saveAndShowXYChart(cc.create(), path, fileName);
    }

}
