package chapters.ch8;

import chapters.ch8.domain.environment.core.FeeEnum;
import chapters.ch8.factory.ParkingParametersFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFeeEnum {

    @Test
    void testGetFee_NotCharging() {
        var parameters = ParkingParametersFactory.forTest();
        double fee = FeeEnum.NotCharging.feeValue(parameters);
        assertEquals(parameters.feeNoCharging(), fee);
    }

    @Test
    void testGetFee_Charging() {
        var parameters = ParkingParametersFactory.forTest();
        double fee = FeeEnum.Charging.feeValue(parameters);
        assertEquals(parameters.feeCharging(), fee);
    }
    @Test
    void testGetFee_OrdinalNotCh() {
        assertEquals(0, FeeEnum.NotCharging.ordinal());
    }

    @Test
    void testGetFee_OrdinalCh() {
        assertEquals(1, FeeEnum.Charging.ordinal());
    }


}