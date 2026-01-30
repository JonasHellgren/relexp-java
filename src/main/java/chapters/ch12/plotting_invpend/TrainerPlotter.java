package chapters.ch12.plotting_invpend;

import chapters.ch12.domain.inv_pendulum.agent.evaluator.PendulumAgentEvaluator;
import chapters.ch12.domain.inv_pendulum.trainer.core.TrainerPendulum;
import chapters.ch12.factory.ManyLinesChartCreatorFactory;
import core.foundation.config.ConfigFactory;
import core.foundation.config.PlotConfig;
import core.foundation.configOld.ProjectPropertiesReader;
import core.plotting_core.chart_plotting.ChartSaver;
import core.plotting_core.chart_plotting.ChartSaverAndPlotter;
import core.plotting_core.plotting_2d.ManyLinesChartCreator;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@UtilityClass
public class TrainerPlotter {

    static final int N_WINDOWS_FILTERING = 10;
    public static final String PENDULUM_EVALUATION = "pendulum_evaluation_";

    public static void plotTrainEvolution(TrainerPendulum trainer, String path) {
        plotMeasure(trainer, "pendulum_training_return_", MeasuresPendulumTrainingEnum.RETURN, path);
        plotMeasure(trainer, "pendulum_training_time_standing_", MeasuresPendulumTrainingEnum.TIME, path);
        plotMeasure(trainer, "pendulum_training_loss_", MeasuresPendulumTrainingEnum.LOSS, path);
        plottingActionValues00(trainer, "pendulum_action_values_s00_");
    }

    @SneakyThrows
    public static void plotMeasure(TrainerPendulum trainer,
                                   String pendulumTrainingReturn,
                                   MeasuresPendulumTrainingEnum measuresPendulumTrainingEnum,
                                   String path) {
        var recorder = trainer.getRecorder();
        var plotter = ErrorBandPlotterNeuralPendulum.ofFiltering(
                recorder, path, pendulumTrainingReturn, N_WINDOWS_FILTERING);
        plotter.plotAndSave(List.of(measuresPendulumTrainingEnum));
    }

    public static void plottingActionValues00(TrainerPendulum trainer, String startStateName) {
        String title = "";
        var recorder = trainer.getRecorder();
        var plotConfig = ConfigFactory.plotConfig();
        var cc = ManyLinesChartCreatorFactory.createChartCreatorForActionValues(
                title, recorder.trajectory(MeasuresPendulumTrainingEnum.EPISODE), plotConfig);
        cc.addLine("ccw", recorder.trajectory(MeasuresPendulumTrainingEnum.Q0CCW));
        cc.addLine("n", recorder.trajectory(MeasuresPendulumTrainingEnum.Q0N));
        cc.addLine("cw", recorder.trajectory(MeasuresPendulumTrainingEnum.Q0CW));
        showAndSave(cc, startStateName + title);
    }


    public static void plotTheta(PendulumAgentEvaluator evaluator) {
        var recorder = evaluator.getRecorder();
        var plotConfig = ConfigFactory.plotConfig();
        String name = "Theta (deg)";
        var cc = getManyLinesChartCreator(name, recorder, plotConfig);
        cc.addLine(name, recorder.trajectory(MeasuresPendulumSimulationEnum.ANGLE_DEG));
        showAndSave(cc, PENDULUM_EVALUATION + name);
    }

    public static void plotTorque(PendulumAgentEvaluator evaluator) {
        var recorder = evaluator.getRecorder();
        String name = "Torque (Nm)";
        var plotConfig = ConfigFactory.plotConfig();
        var cc = getManyLinesChartCreator(name, recorder, plotConfig);
        cc.addLine(name, recorder.trajectory(MeasuresPendulumSimulationEnum.TORQUE));
        showAndSave(cc, PENDULUM_EVALUATION + name);
    }

    public static void plotSpd(PendulumAgentEvaluator evaluator) {
        var recorder = evaluator.getRecorder();
        String name = "Angular speed";
        String title = name + " (deg/s)";
        var plotConfig = ConfigFactory.plotConfig();
        var cc = getManyLinesChartCreator(title, recorder, plotConfig);
        cc.addLine(name, recorder.trajectory(MeasuresPendulumSimulationEnum.ANGULAR_SPEED_DEG));
        showAndSave(cc, PENDULUM_EVALUATION + name);
    }

    @NotNull
    private static ManyLinesChartCreator getManyLinesChartCreator(String title, PendulumRecorder recorder, PlotConfig plotConfig) {
        return ManyLinesChartCreatorFactory.createChartCreatorForEvaluator(
                title, recorder.trajectory(MeasuresPendulumSimulationEnum.TIME_MS), plotConfig);
    }

    private static void showAndSave(ManyLinesChartCreator cc, String fileName) {
        var path = ConfigFactory.pathPicsConfig().ch12();
        ChartSaver.saveAndShowXYChart(cc.create(), path, fileName);
    }

}
