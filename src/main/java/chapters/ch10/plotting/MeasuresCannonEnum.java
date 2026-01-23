package chapters.ch10.plotting;


import core.foundation.util.unit_converter.UnitConverterUtil;
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
    MEAN("Mean angle (Degrees)", pm -> getDegrees(pm.mean())),
    STD("Std. angle (Degrees)", pm -> getDegrees(pm.std()));

    private static double getDegrees(double mean) {
        return UnitConverterUtil.convertRadiansToDegrees(mean);
    }

    public final String description;
    public final Function<MeasuresCannon, Double> mapFunction;
}
