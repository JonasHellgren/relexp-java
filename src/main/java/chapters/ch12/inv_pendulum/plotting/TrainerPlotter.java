package chapters.ch12.inv_pendulum.plotting;

import chapters.ch12.inv_pendulum.domain.agent.evaluator.PendulumAgentEvaluator;
import chapters.ch12.inv_pendulum.domain.trainer.core.TrainerPendulum;
import chapters.ch12.inv_pendulum.factory.ManyLinesChartCreatorFactory;
import core.foundation.config.ProjectPropertiesReader;
import core.plotting.chart_plotting.ChartSaverAndPlotter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import java.util.List;

@UtilityClass
public class TrainerPlotter {

    static final int N_WINDOWS_FILTERING = 10;
    public static final String X_AXIS_LABEL_EVAL = "Time (ms)";

    public static void plotTrainEvolution(TrainerPendulum trainer) {
        plotMeasure(trainer, "pendulum_training_return_", MeasuresPendulumTrainingEnum.RETURN);
        plotMeasure(trainer, "pendulum_training_time_standing_", MeasuresPendulumTrainingEnum.TIME);
        plotMeasure(trainer, "pendulum_training_loss_", MeasuresPendulumTrainingEnum.LOSS);
        plottingActionValues00(trainer, "pendulum_action_values_s00_");
    }

    @SneakyThrows
    public static void plotMeasure(TrainerPendulum trainer,
                                    String pendulumTrainingReturn,
                                    MeasuresPendulumTrainingEnum measuresPendulumTrainingEnum) {
        var recorder = trainer.getRecorder();
        var path = ProjectPropertiesReader.create().pathDeepRl();
        var plotter = ErrorBandPlotterNeuralPendulum.ofFiltering(
                recorder, path, pendulumTrainingReturn, N_WINDOWS_FILTERING);
        plotter.plotAndSave(List.of(measuresPendulumTrainingEnum));
    }

    public static void plottingActionValues00(TrainerPendulum trainer, String s) {
        String title = "";
        var recorder = trainer.getRecorder();
        var cc = ManyLinesChartCreatorFactory.createChartCreatorForActionValues(
                title, recorder.trajectory(MeasuresPendulumTrainingEnum.EPISODE),true);
        cc.addLine("ccw", recorder.trajectory(MeasuresPendulumTrainingEnum.Q0CCW));
        cc.addLine("n", recorder.trajectory(MeasuresPendulumTrainingEnum.Q0N));
        cc.addLine("cw", recorder.trajectory(MeasuresPendulumTrainingEnum.Q0CW));
        var chart = cc.create();
        ChartSaverAndPlotter.showChartSaveInFolderDeepRl(chart, s + title);
    }

    public static void plotTheta(PendulumAgentEvaluator evaluator) {
        var recorder=evaluator.getRecorder();
        String name = "Theta (deg)";
        var cc = ManyLinesChartCreatorFactory.createChartCreatorForEvaluator(
                name, recorder.trajectory(MeasuresPendulumSimulationEnum.TIME_MS), X_AXIS_LABEL_EVAL);
        cc.addLine(name, recorder.trajectory(MeasuresPendulumSimulationEnum.ANGLE_DEG));
        var chart = cc.create();
        ChartSaverAndPlotter.showChartSaveInFolderDeepRl(chart, "pendulum_evaluation_"+name);
    }

    public static void plotTorque(PendulumAgentEvaluator evaluator) {
        var recorder=evaluator.getRecorder();
        String name = "Torque (Nm)";
        var cc = ManyLinesChartCreatorFactory.createChartCreatorForEvaluator(
                name, recorder.trajectory(MeasuresPendulumSimulationEnum.TIME_MS), X_AXIS_LABEL_EVAL);
        cc.addLine(name, recorder.trajectory(MeasuresPendulumSimulationEnum.TORQUE));
        var chart = cc.create();
        ChartSaverAndPlotter.showChartSaveInFolderDeepRl(chart, "pendulum_evaluation_"+name);
    }

    public static void plotSpd(PendulumAgentEvaluator evaluator) {
        var recorder=evaluator.getRecorder();
        String name = "Angular speed";
        String title=name+" (deg/s)";
        var cc = ManyLinesChartCreatorFactory.createChartCreatorForEvaluator(
                title, recorder.trajectory(MeasuresPendulumSimulationEnum.TIME_MS), X_AXIS_LABEL_EVAL);
        cc.addLine(name, recorder.trajectory(MeasuresPendulumSimulationEnum.ANGULAR_SPEED_DEG));
        var chart = cc.create();
        ChartSaverAndPlotter.showChartSaveInFolderDeepRl(chart, "pendulum_evaluation_"+name);
    }


}
