package ch10;

import chapters.ch10.cannon.domain.envrionment.EnvironmentCannon;
import chapters.ch10.cannon.factory.FactoryEnvironmentParametersCannon;
import core.foundation.util.unit_converter.MyUnitConverter;
import core.plotting.chart_plotting.ChartCreatorFactory;
import core.plotting.chart_plotting.ChartSaverAndPlotter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import static core.foundation.util.collections.ListCreator.createFromStartToEndWithNofItems;

public class RunnerCannonDistancePlotter {

    static String FILE_NAME = "cannon_distance";
    static Integer N_POINTS=100;

    @SneakyThrows
    public static void main(String[] args) {
        var parameters = FactoryEnvironmentParametersCannon.createDefault();
        var environment = EnvironmentCannon.of(parameters);
        var anglesInDegrees= createFromStartToEndWithNofItems(0,90, N_POINTS);
        var distances = getDistances(anglesInDegrees, environment);
        var factory= ChartCreatorFactory.produceLine("Angle (Degrees)", "Distance (m)");
        factory.addLine(anglesInDegrees,distances);
        ChartSaverAndPlotter.showChartSaveInFolderPolGrad(factory.create(), FILE_NAME);
    }

    @NotNull
    private static List<Double> getDistances(List<Double> anglesInDegrees, EnvironmentCannon environment) {
        List<Double> distances= new ArrayList<>();
        for (int i = 0; i < N_POINTS ; i++) {
            double angle = MyUnitConverter.convertDegreesToRadians(anglesInDegrees.get(i));
            var sr= environment.step(angle);
            distances.add(sr.distance());
        }
        return distances;
    }

}
