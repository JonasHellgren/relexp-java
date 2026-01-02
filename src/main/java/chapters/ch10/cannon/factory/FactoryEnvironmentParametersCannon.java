package chapters.ch10.cannon.factory;

import chapters.ch10.cannon.domain.envrionment.EnvironmentParametersCannon;
import lombok.experimental.UtilityClass;

/**
 * Factory class for creating EnvironmentParametersCannon instances.
 * This class provides a method for creating environment parameters with default settings.
 */
@UtilityClass
public class FactoryEnvironmentParametersCannon {

    public static EnvironmentParametersCannon createDefault() {
        return EnvironmentParametersCannon.builder()
                .windResistanceCoefficient(1e-5)
                .speedInit(300.0)
                .constantOfGravity(9.81)
                .distanceTarget(800)
                .build();
    }

}
