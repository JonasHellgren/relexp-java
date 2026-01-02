package chapters.ch10.cannon.plotting;


import core.foundation.util.unit_converter.MyUnitConverter;
import lombok.AllArgsConstructor;
import java.util.function.Function;

@AllArgsConstructor
public enum MeasuresCannonEnum {
    RETURN_MINUS_BASE("ReturnMinusBase", pm -> pm.returnMinusBase()),
    BASE("Base (m)", pm -> pm.base()),
    ANGLE("Angle", pm -> getDegrees(pm.angle())),
    DISTANCE("Distance (m)", pm -> pm.distance()),
    GRADLOGZMEAN("Grad log zm", pm -> pm.gradLogZMean()),
    GRADLOGZSTD("Grad log zs", pm ->  pm.gradLogZStd()),
    MEAN("Mean nOccupied (Degrees)", pm -> getDegrees(pm.mean())),
    STD("Std. nOccupied (Degrees)", pm -> getDegrees(pm.std()));

    private static double getDegrees(double mean) {
        return MyUnitConverter.convertRadiansToDegrees(mean);
    }

    public final String description;
    public final Function<MeasuresCannon, Double> mapFunction;
}
