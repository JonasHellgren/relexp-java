package chapters.ch11.helper;

import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch9.radial_basis_old.Kernel;
import lombok.experimental.UtilityClass;
import java.util.List;

@UtilityClass
public class RadialBasisAdapter {

    public static List<Double> asInput(StateLunar state) {
          return List.of(state.y(),state.spd());
    }

    public static double asYPos(Kernel kernel) {
        return kernel.centerCoordinates()[0];
    }


    public static double asSpd(Kernel kernel) {
        return kernel.centerCoordinates()[1];
    }



}
