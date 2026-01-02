package chapters.ch8.factory;

import chapters.ch8.domain.environment.param.ParkingParameters;
import lombok.experimental.UtilityClass;

/**
 * A utility class for creating ParkingParameters instances.
 *
 * Used by: EnvironmentParking
 */
@UtilityClass
public class ParkingParametersFactory {

    public static ParkingParameters forTest() {
        return ParkingParameters.builder()
                .nSpots(5)
                .feeCharging(2.0)
                .feeNoCharging(1.0)
                .probArriving(0.2)
                .probDeparting(0.1)
                .probChargingRequest(0.5)
                .maxSteps(10)
                .build();
    }

    public static ParkingParameters forRunning() {
        return forTest()
                .withNSpots(5)
                .withMaxSteps(100_000).withFeeCharging(2.0).withFeeNoCharging(1)
                .withProbArriving(1.0).withProbDeparting(0.2);
    }


}
