package ch5;


import chapters.ch5.domain.policy_evaluator.StatePolicyEvaluationMc;
import chapters.ch5.factory.StatePolicyEvaluationFactory;
import chapters.ch5.plotting.ValueMemoryMcPlotter;
import chapters.ch5.implem.walk.EnvironmentWalk;
import core.foundation.gadget.timer.CpuTimer;

public class RunnerPolicyEvaluationWalk {

    public static void main(String[] args) {
        var timer= CpuTimer.empty();
        var evaluator = StatePolicyEvaluationFactory.createWalk(StatePolicyEvaluationMc.DEFAULT_SETTINGS);
        evaluator.evaluate();
        timer.printInMs();
        var memory = evaluator.getMemory();
        var plotter= ValueMemoryMcPlotter.of(
                memory,
                ValueMemoryMcPlotter.Settings.builder()
                        .nofDigits(1)
                        .yLabel("y").xLabel("x")
                        .fileNameAddOn("Walk")
                        .envName(EnvironmentWalk.NAME)
                        .nRows(3).nCols(6)
                        .startRow(0).startColumn(1)
                        .build());
        plotter.plotStateValues();
    }

}
