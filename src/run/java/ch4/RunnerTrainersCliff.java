package ch4;


import chapters.ch4.implem.cliff_walk.factory.CliffRunnerFactory;
import core.foundation.config.ConfigFactory;
import core.foundation.gadget.timer.CpuTimer;

import static chapters.ch4.plotting.GridPlotShowAndSave.showAndSavePlots;

public class RunnerTrainersCliff {

    public static final int NOF_DIGITS = 0;

    static CliffRunnerFactory.Dependencies dependencies;
    static CliffRunnerFactory.Trainers trainers;

    public static void main(String[] args) {
        var timer= CpuTimer.empty();
        setUp();
        trainers.train();
        timer.printInMs();
        plot();
    }

    static void setUp() {
        dependencies = CliffRunnerFactory.produceDependencies();
        trainers = CliffRunnerFactory.produceTrainers(dependencies);
    }

    private static void plot() {
        var picPath = ConfigFactory.pathPicsConfig().ch4();
        var plotCfg= ConfigFactory.plotConfig();

        showAndSavePlots(dependencies.qlearning(), trainers.qlearning().getRecorder(), "_QlearningCliff",
                NOF_DIGITS, picPath,plotCfg);
        showAndSavePlots(dependencies.sarsa(), trainers.sarsa().getRecorder(), "_sarsaCliff",
                NOF_DIGITS, picPath,plotCfg);
    }


}
