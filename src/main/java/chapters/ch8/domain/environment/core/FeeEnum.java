package chapters.ch8.domain.environment.core;

import chapters.ch8.domain.environment.param.ParkingParameters;
import core.foundation.util.rand.RandUtil;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Enum representing the possible fees in a parking environment.
 */
@AllArgsConstructor
public enum FeeEnum {
    NotCharging, Charging;

    public double feeValue(ParkingParameters parameters) {
        return this.equals(Charging)
                ? parameters.feeCharging()
                : parameters.feeNoCharging();
    }

    public static List<FeeEnum> allFees() {
        return List.of(FeeEnum.NotCharging, FeeEnum.Charging);
    }

    public static int nofFees() {
        return allFees().size();
    }

    public static FeeEnum random() {
        return allFees().get(RandUtil.getRandomIntNumber(0, nofFees()));
    }


}
