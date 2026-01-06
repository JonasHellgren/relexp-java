package ch4;

import chapters.ch4.implem.blocked_road_lane.core.EnvironmentRoad;
import chapters.ch4.implem.blocked_road_lane.factory.FactoryEnvironmentParametersRoad;
import chapters.ch4.implem.cliff_walk.core.EnvironmentCliff;
import chapters.ch4.implem.cliff_walk.factory.FactoryEnvironmentParametersCliff;
import chapters.ch4.implem.treasure.core.EnvironmentTreasure;
import chapters.ch4.implem.treasure.factory.EnvironmentParametersTreasureFactor;
import chapters.ch4.plotting.GridEnvironmentPlotter;
import core.foundation.config.ConfigFactory;
import core.gridrl.EnvironmentGridI;

import java.util.List;

public class RunnerPlotterGridEnvironment {
    public static final int INDEX_ENVIRONMENT_TO_SHOW = 2;

    public static void main(String[] args) {
        var environments = getEnvironments();
        var plotter= GridEnvironmentPlotter.of(environments.get(INDEX_ENVIRONMENT_TO_SHOW));
        plotter.plot(ConfigFactory.plotConfig());
    }

    private static List<EnvironmentGridI> getEnvironments() {
        return List.of(
                EnvironmentRoad.of(FactoryEnvironmentParametersRoad.produceRoadFixedFailReward()),
                EnvironmentCliff.of(FactoryEnvironmentParametersCliff.produce()),
                EnvironmentTreasure.of(EnvironmentParametersTreasureFactor.produce())
        );
    }
}
