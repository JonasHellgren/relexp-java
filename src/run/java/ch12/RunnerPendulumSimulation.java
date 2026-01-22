package ch12;


import chapters.ch12.inv_pendulum.domain.environment.core.ActionPendulum;
import chapters.ch12.inv_pendulum.domain.environment.core.EnvironmentPendulum;
import chapters.ch12.inv_pendulum.domain.environment.core.StatePendulum;
import chapters.ch12.inv_pendulum.domain.environment.startstate_supplier.StartStateSupplierEnum;
import chapters.ch12.inv_pendulum.factory.ManyLinesChartCreatorFactory;
import chapters.ch12.inv_pendulum.factory.PendulumParametersFactory;
import chapters.ch12.inv_pendulum.plotting.MeasuresPendulumSimulation;
import chapters.ch12.inv_pendulum.plotting.MeasuresPendulumSimulationEnum;
import chapters.ch12.inv_pendulum.plotting.PendulumRecorder;
import core.foundation.util.cond.ConditionalsUtil;
import core.foundation.util.formatting.NumberFormatterUtil;
import core.foundation.util.unit_converter.UnitConverterUtil;
import core.plotting_core.chart_plotting.ChartSaverAndPlotter;

public class RunnerPendulumSimulation {
    public static final double MAX_ANGLE_FRACTION = 0.2;  //0.8 or 0.2
    public static final int NOF_DIGITS_TITLETEXT = 2;
    public static final int N_STEPS = 150;
    public static final int N_STEPS_BETWEEN_RECORDING = 10;  //trick to get a nicer plot
    public static final boolean SHOW_LEGEND = true;

    public static void main(String[] args) {
        var parameters = PendulumParametersFactory.createForTest();
        var environment = EnvironmentPendulum.of(parameters);
        var startState = StartStateSupplierEnum.SMALL_ANGLE_ZERO_SPEED.create().getStartState();
        var recorder = PendulumRecorder.empty();
        simulate(startState, environment, recorder);
        double angleTorqueActivation = parameters.angleMax() * MAX_ANGLE_FRACTION;
        plotting(angleTorqueActivation, recorder);
    }

    private static void simulate(StatePendulum startState,
                                 EnvironmentPendulum environment,
                                 PendulumRecorder recorder) {
        var parameters= environment.getParameters();
        double angleTorqueActivation = parameters.angleMax() * MAX_ANGLE_FRACTION;
        var state = startState;
        for (int i = 0; i < N_STEPS; i++) {
            var action = (state.angle() > angleTorqueActivation)
                    ? ActionPendulum.CCW
                    : ActionPendulum.N;
            var sr = environment.step(state, action);
            var measures = MeasuresPendulumSimulation.of(state, action, parameters);
            state = sr.stateNew();
            ConditionalsUtil.executeIfTrue(i % N_STEPS_BETWEEN_RECORDING == 0, () -> recorder.add(measures));
        }
    }

    private static void plotting(double angleTorqueActivation, PendulumRecorder recorder) {
        String title = "Theta limit (deg) = " + NumberFormatterUtil.getRoundedNumberAsString(
                UnitConverterUtil.convertRadiansToDegrees(angleTorqueActivation), NOF_DIGITS_TITLETEXT);
        var cc = ManyLinesChartCreatorFactory.createChartCreatorForSimulation(
                title, recorder.trajectory(MeasuresPendulumSimulationEnum.TIME), SHOW_LEGEND);
        cc.addLine("Theta (deg)", recorder.trajectory(MeasuresPendulumSimulationEnum.ANGLE_DEG));
        cc.addLine("Theta. spd. (deg/s)", recorder.trajectory(MeasuresPendulumSimulationEnum.ANGULAR_SPEED_DEG));
        cc.addLine("Theta. max (deg)", recorder.trajectory(MeasuresPendulumSimulationEnum.ANGLE_MAX_DEG));
        var chart = cc.create();
        ChartSaverAndPlotter.showChartSaveInFolderDeepRl(chart, "pendulum_simulation_"+title);
    }


}
