package ch4;


import chapters.ch4.implem.blocked_road_lane.factory.RoadRunnerFactory;
import static chapters.ch4.plotting.GridPlotHelper.showAndSavePlots;


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
        showAndSavePlots(dependencies.qlHighLearning(),
                trainers.qlHighLearning().getRecorder(), "_QlRoadHighLearning", NOF_DIGITS);
        showAndSavePlots(dependencies.qlLowLearning(),
                trainers.qlLowLearning().getRecorder(), "_QlRoadLowLearning", NOF_DIGITS);
        showAndSavePlots(dependencies.qlHighLearningDiscD9(),
                trainers.qlHighLearningDiscD9().getRecorder(), "_QlRoadHighLearningDiscD9", NOF_DIGITS);
        showAndSavePlots(dependencies.qlStochasticFailReward(),
                trainers.qlStochasticFailReward().getRecorder(), "_QlRoadHStochastic", NOF_DIGITS);
    }

}
