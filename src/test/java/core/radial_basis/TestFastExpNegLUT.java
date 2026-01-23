package core.radial_basis;

import core.foundation.util.rand.RandUtil;
import core.nextlevelrl.radial_basis.FastExpNegInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TestFastExpNegLUT {

    private FastExpNegInput lut;

    @BeforeEach
    void setUp() {
        lut = FastExpNegInput.createDefault();
    }

    @Test
    void fastExp_returnsExactValueForZero() {
        double result = lut.fastExp(0.0);
        assertEquals(1.0, result, 0.0);
    }

    @Test
    void fastExp_returnsZeroForNegativeValuesBelowXMin() {
        double result = lut.fastExp(-100);
        assertEquals(0.0, result, 0.0);
    }

    @Test
    void fastExp_returnsApproximationForPositiveValues() {
        double randomValue = RandUtil.randomNumberBetweenZeroAndOne();
        double result = lut.fastExp(-randomValue);
        assertEquals(Math.exp(-randomValue), result, 0.01);
    }



}
