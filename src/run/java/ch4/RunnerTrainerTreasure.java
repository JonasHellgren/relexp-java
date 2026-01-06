package ch4;


import chapters.ch4.implem.treasure.factory.TreasureRunnerFactory;
import core.foundation.config.ConfigFactory;
import core.foundation.gadget.timer.CpuTimer;

import static chapters.ch4.plotting.GridPlotShowAndSave.showAndSavePlots;

public class RunnerTrainerTreasure {

    public static final int NOF_DIGITS = 0;

    static TreasureRunnerFactory.Dependencies dependencies;
    static TreasureRunnerFactory.Trainers trainers;

    public static void main(String[] args) {
        var timer= CpuTimer.empty();
        setUp();
        trainers.train();
        timer.printInMs();
        plot();
    }

    static void setUp() {
        dependencies = TreasureRunnerFactory.produceDependencies();
        trainers = TreasureRunnerFactory.produceTrainers(dependencies);
    }

    private static void plot() {
        var picPath = ConfigFactory.pathPicsConfig().ch4();
        var plotCfg= ConfigFactory.plotConfig();

        showAndSavePlots(
                dependencies.lowExploration(),
                trainers.lowExploration().getRecorder(),
                "_treasureLowExpl",
                NOF_DIGITS, picPath,plotCfg);
        showAndSavePlots(
                dependencies.highExploration(),
                trainers.highExploration().getRecorder(),
                "_treasureHighExpl",
                NOF_DIGITS, picPath,plotCfg);
    }

}
