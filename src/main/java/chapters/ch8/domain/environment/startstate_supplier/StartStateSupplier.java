package chapters.ch8.domain.environment.startstate_supplier;

import chapters.ch8.domain.environment.core.FeeEnum;
import chapters.ch8.domain.environment.core.StateParking;
import chapters.ch8.domain.environment.param.ParkingParameters;
import core.foundation.util.rand.RandUtil;
import lombok.extern.java.Log;


/***
 * Method of used only when PendulumParameters are needed
 * Enum representing different methods for supplying start states to the inverted pendulum environment.
 * Each method is associated with a specific way of generating the initial state of the pendulum.
 *
 */

@Log
public enum StartStateSupplier {
    DEFINED,
    ZEROOCCUP_RANDOMFEE,
    RANDOMOCCUP_RANDOMFEE;


    public static final String EXCEPTION_MESSAGE =
            "Wrong method used in StartStateSupplierEnum, use ofDefined instead";

    public StartStateSupplierI ofDefined(int nOccup, FeeEnum feeEnum) {
        return () -> StateParking.ofStart(nOccup, feeEnum);
    }

    public StartStateSupplierI of(ParkingParameters pp) {
        return switch (this) {
            case RANDOMOCCUP_RANDOMFEE -> () -> StateParking.ofStart(
                    RandUtil.getRandomIntNumber(0,pp.nSpots()+1),
                    FeeEnum.random());
            case ZEROOCCUP_RANDOMFEE -> () -> StateParking.ofStart(
                    0,
                    FeeEnum.random());
            default -> throw new IllegalArgumentException(EXCEPTION_MESSAGE);
        };
    }




}
