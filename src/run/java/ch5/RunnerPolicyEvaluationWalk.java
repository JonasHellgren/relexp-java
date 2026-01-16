package ch5;


import chapters.ch5.domain.memory.StateMemoryMcI;
import chapters.ch5.domain.policy_evaluator.StatePolicyEvaluationMc;
import chapters.ch5.factory.HeatMapWithTextFactoryWalk;
import chapters.ch5.factory.WalkDependenciesFactory;
import core.foundation.config.ConfigFactory;
import core.foundation.gadget.timer.CpuTimer;
import core.plotting.chart_plotting.ChartSaverAndPlotter;

public class RunnerPolicyEvaluationWalk {

    public static void main(String[] args) {
        var timer = CpuTimer.empty();
        var evaluator = StatePolicyEvaluationMc.of(WalkDependenciesFactory.produce());
        evaluator.evaluate();
        timer.printInMs();
        var memory = evaluator.getDependencies().stateMemory();
        saveAndPlot(memory, "Walk");
    }

    private static void saveAndPlot(StateMemoryMcI memory, String name) {
        var cfg = ConfigFactory.plotConfig();
        var chart = HeatMapWithTextFactoryWalk.produce(memory, cfg);
        ChartSaverAndPlotter.showAndSaveHeatMapFolderMonteCarlo(
                chart, "values_", name);
    }

}


