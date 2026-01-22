package chapters.ch8.domain.environment.param;

import chapters.ch8.domain.environment.core.StateParking;
import core.foundation.util.rand.RandUtil;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record ParkingParameters(
        int nSpots,
        double feeCharging,
        double feeNoCharging,
        double probArriving,
        double probDeparting,
        double probChargingRequest,
        int maxSteps
) {

    public boolean isArriving() {
        return RandUtil.randomNumberBetweenZeroAndOne() < probArriving();
    }

    public boolean isDeparting() {
        return RandUtil.randomNumberBetweenZeroAndOne() < probDeparting();
    }

    public boolean isMaxStepsExceeded(StateParking state) {
        return state.nSteps() >= maxSteps();
    }

    public int nOccupAsInt(double nOccup) {
       return  (int) Math.ceil(nOccup);
    }
}


