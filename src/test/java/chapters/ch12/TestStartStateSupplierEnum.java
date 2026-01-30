package chapters.ch12;

import chapters.ch12.domain.inv_pendulum.environment.startstate_supplier.StartStateSupplierEnum;
import chapters.ch12.domain.inv_pendulum.environment.startstate_supplier.StartStateSupplierI;
import chapters.ch12.factory.PendulumParametersFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestStartStateSupplierEnum {

    public static final double TOL = 1e-6;

    @Test
    void testGetSupplier_ZERO_ANGLE_ZERO_SPEED() {
        var supplier = StartStateSupplierEnum.ZERO_ANGLE_ZERO_SPEED.create();
        var state = supplier.getStartState();
        assertEquals(0, state.angle(), TOL);
        assertEquals(0, state.angularSpeed(), TOL);
    }

    @Test
    void testGetSupplier_SMALL_ANGLE_ZERO_SPEED() {
        var supplier = StartStateSupplierEnum.SMALL_ANGLE_ZERO_SPEED.create();
        var state = supplier.getStartState();
        assertEquals(0.01, state.angle(), TOL);
        assertEquals(0, state.angularSpeed(), TOL);
    }

    @Test
    void testGetSupplier_RANDOM_FEASIBLE_ANGLE_ZERO_SPEED() {
        var pp= PendulumParametersFactory.createForTest();
        StartStateSupplierI supplier = StartStateSupplierEnum.RANDOM_FEASIBLE_ANGLE_ZERO_SPEED.of(pp);
        var state = supplier.getStartState();
        assertEquals(0, state.angularSpeed(), TOL);
        assertTrue(Math.abs(state.angle()) < pp.angleMax());
    }

}
