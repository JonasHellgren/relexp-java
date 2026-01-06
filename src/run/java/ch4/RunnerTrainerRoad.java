package ch4;


import chapters.ch4.implem.blocked_road_lane.factory.RoadRunnerFactory;
import core.foundation.config.ConfigFactory;

import static chapters.ch4.plotting.GridPlotShowAndSave.showAndSavePlots;


public class RunnerTrainerRoad {

    public static final int NOF_DIGITS = 2;

    static RoadRunnerFactory.Dependencies dependencies;
    static RoadRunnerFactory.Trainers trainers;

    public static void main(String[] args) {
        dependencies= RoadRunnerFactory.produceDependencies();
        trainers=RoadRunnerFactory.produceTrainers(dependencies);
        trainers.train();
        plot();
    }

    private static void plot() {
        var picPath = ConfigFactory.pathPicsConfig().ch4();
        var plotCfg=ConfigFactory.plotConfig();

        showAndSavePlots(dependencies.qlHighLearning(),
                trainers.qlHighLearning().getRecorder(), "_QlRoadHighLearning", NOF_DIGITS, picPath,plotCfg);
        showAndSavePlots(dependencies.qlLowLearning(),
                trainers.qlLowLearning().getRecorder(), "_QlRoadLowLearning", NOF_DIGITS, picPath,plotCfg);
        showAndSavePlots(dependencies.qlHighLearningDiscD9(),
                trainers.qlHighLearningDiscD9().getRecorder(), "_QlRoadHighLearningDiscD9", NOF_DIGITS, picPath,plotCfg);
        showAndSavePlots(dependencies.qlStochasticFailReward(),
                trainers.qlStochasticFailReward().getRecorder(), "_QlRoadHStochastic", NOF_DIGITS, picPath,plotCfg);
    }

}
