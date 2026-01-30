package chapters.ch12.domain.inv_pendulum.environment.startstate_supplier;

import chapters.ch12.domain.inv_pendulum.environment.core.StatePendulum;
import chapters.ch12.domain.inv_pendulum.environment.param.PendulumParameters;
import core.foundation.util.rand.RandUtil;
import lombok.extern.java.Log;


/***
 * Method of used only when PendulumParameters are needed
 * Enum representing different methods for supplying start states to the inverted pendulum environment.
 * Each method is associated with a specific way of generating the initial state of the pendulum.
 *
 */

@Log
public enum StartStateSupplierEnum {
    ZERO_ANGLE_ZERO_SPEED,
    SMALL_ANGLE_ZERO_SPEED,
    GIVEN_ANGLE_ZERO_SPEED,
    RANDOM_SMALL_ANGLE_ZERO_SPEED,
    RANDOM_FEASIBLE_ANGLE_ZERO_SPEED,
    RANDOM_FEASIBLE_ANGLE_AND_SPEED;


    public static final String EXCEPTION_MESSAGE =
            "Wrong method used in StartStateSupplierEnum, use create/of instead";
    public static final double ANGLE_CAN_RECOVER = 0.01;
    public static final double K_ANGLE = 0.1;
    public static final double K_SPD = 0.2;

    public StartStateSupplierI create() {
        return switch (this) {
            case ZERO_ANGLE_ZERO_SPEED -> () -> StatePendulum.ofStart(0, 0);
            case SMALL_ANGLE_ZERO_SPEED -> () -> StatePendulum.ofStart(ANGLE_CAN_RECOVER, 0);
            case RANDOM_SMALL_ANGLE_ZERO_SPEED -> () -> StatePendulum.ofStart(
                    RandUtil.doubleInInterval(-ANGLE_CAN_RECOVER, ANGLE_CAN_RECOVER),
            0);
            default -> throw new IllegalArgumentException(EXCEPTION_MESSAGE);
        };
    }

    public StartStateSupplierI ofStartAngle(double angle) {
        return () -> StatePendulum.ofStart(angle, 0);
    }

    public StartStateSupplierI of(PendulumParameters pp) {
        return switch (this) {
            case RANDOM_FEASIBLE_ANGLE_ZERO_SPEED -> () -> StatePendulum.ofStart(
                    RandUtil.doubleInInterval(-pp.angleMax()* K_ANGLE, pp.angleMax())* K_ANGLE, 0);
            case RANDOM_FEASIBLE_ANGLE_AND_SPEED -> () -> StatePendulum.ofStart(
                    RandUtil.doubleInInterval(-pp.angleMax()* K_ANGLE, pp.angleMax()* K_ANGLE),
                    RandUtil.doubleInInterval(-pp.angleSpeedMax()* K_SPD, pp.angleSpeedMax())* K_SPD);
            default -> throw new IllegalArgumentException(EXCEPTION_MESSAGE);
        };
    }




}
