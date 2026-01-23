package chapters.ch10.factory;

import chapters.ch10.cannon.domain.agent.AgentParametersCannon;
import core.foundation.gadget.math.MeanAndStd;
import core.foundation.util.unit_converter.UnitConverterUtil;
import lombok.experimental.UtilityClass;

/**
 * A utility class for creating AgentParametersCannon instances with different clipping and gradient settings.
 */
@UtilityClass
public class FactoryAgentParametersCannon {

    public static final double ANGLE_MAX_RAD = UnitConverterUtil.convertDegreesToRadians(45);
    public static final double INIT_ANGLE_MEAN = Math.PI / 16;  // 11 degrees
    public static final double INIT_ANGLE_STD = Math.PI / 16;   // 11 degrees
    public static final double GRADZ_MEAN_MAX = 1e-3;
    public static final double GRADZ_STD_MAX = 1e-3;


    public static AgentParametersCannon noClipping(double mean, double std) {
        return AgentParametersCannon.builder()
                .meanAndStdInit(MeanAndStd.of(mean, std))
                .meanAndStdMin(MeanAndStd.of(-Double.MAX_VALUE, Double.MIN_VALUE))
                .meanAndStdMax(MeanAndStd.of(Double.MAX_VALUE, Double.MAX_VALUE))
                .gradzMeanMax(Double.MAX_VALUE).gradzStdMax(Double.MAX_VALUE)
                .build();
    }

    public static AgentParametersCannon clipIn0And45Degrees(double mean, double std) {
        return AgentParametersCannon.builder()
                .meanAndStdInit(MeanAndStd.of(mean, std))
                .meanAndStdMin(MeanAndStd.of(0, Double.MIN_VALUE))
                .meanAndStdMax(MeanAndStd.of(ANGLE_MAX_RAD, Double.MAX_VALUE))
                .gradzMeanMax(Double.MAX_VALUE).gradzStdMax(Double.MAX_VALUE)
                .build();
    }

    public static AgentParametersCannon clipIn0And45DegreesAndGradient(double mean, double std) {
        return clipIn0And45Degrees(mean, std)
                .withGradzMeanMax(GRADZ_MEAN_MAX).withGradzStdMax(GRADZ_STD_MAX);
    }


    public static AgentParametersCannon forRunning() {
        return clipIn0And45DegreesAndGradient(INIT_ANGLE_MEAN,INIT_ANGLE_STD);
    }


    public static AgentParametersCannon forRunningNoMeanClipping() {
        return clipIn0And45Degrees(INIT_ANGLE_MEAN,INIT_ANGLE_STD);
    }



}
