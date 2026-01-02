// src/test/java/f_non_episodic/domain/environment/startstate_supplier/StartStateSupplierEnumTest.java

package chapters.ch8;

import chapters.ch8.domain.environment.core.FeeEnum;
import chapters.ch8.domain.environment.core.StateParking;
import chapters.ch8.domain.environment.param.ParkingParameters;
import chapters.ch8.domain.environment.startstate_supplier.StartStateSupplier;
import chapters.ch8.domain.environment.startstate_supplier.StartStateSupplierI;
import chapters.ch8.factory.ParkingParametersFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestStartStateSupplierEnum {


    ParkingParameters parameters;

     @BeforeEach
      void init() {
        parameters= ParkingParametersFactory.forTest();
      }

    @Test
    void testOfDefined() {
        int nOccup = 5;
        FeeEnum feeEnum = FeeEnum.NotCharging;
        StartStateSupplierI supplier = StartStateSupplier.DEFINED.ofDefined(nOccup, feeEnum);
        StateParking state = supplier.state();
        assertEquals(nOccup, state.nOccupied());
        assertEquals(feeEnum, state.fee());
    }

    @Test
    void testOf_RANDOMOCCUP_RANDOMFEE() {
        StartStateSupplierI supplier = StartStateSupplier.RANDOMOCCUP_RANDOMFEE.of(parameters);
        StateParking state = supplier.state();
        assertTrue(state.nOccupied() >= 0 && state.nOccupied() <= parameters.nSpots());
        assertNotNull(FeeEnum.allFees().contains(state.fee()));
    }

    @Test
    void testOf_ZEROOCCUP_RANDOMFEE() {
        StartStateSupplierI supplier = StartStateSupplier.ZEROOCCUP_RANDOMFEE.of(parameters);
        StateParking state = supplier.state();
        assertEquals(0, state.nOccupied());
        assertNotNull(FeeEnum.allFees().contains(state.fee()));
    }

    @Test
    void testOf_InvalidEnum() {
        assertThrows(IllegalArgumentException.class, () -> StartStateSupplier.DEFINED.of(parameters));
    }
}