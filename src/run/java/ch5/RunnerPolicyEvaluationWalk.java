package ch5;


import chapters.ch5.domain.memory.StateMemoryMcI;
import chapters.ch5.domain.policy_evaluator.StatePolicyEvaluationMc;
import chapters.ch5.factory.HeatMapWithTextFactoryWalk;
import chapters.ch5.factory.WalkDependenciesFactory;
import core.foundation.config.ConfigFactory;
import core.foundation.config.PathAndFile;
import core.foundation.gadget.timer.CpuTimer;
import core.plotting_core.chart_saving_and_plotting.ChartSaver;

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
        ChartSaver.saveHeatMapChart(
                chart,
                PathAndFile.of(ConfigFactory.pathPicsConfig().ch5(), name));

    }

}


