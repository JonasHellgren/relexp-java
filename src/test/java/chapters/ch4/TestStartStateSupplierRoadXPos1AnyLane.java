package chapters.ch4;

import chapters.ch4.implem.blocked_road_lane.core.EnvironmentRoad;
import chapters.ch4.implem.blocked_road_lane.start_state_suppliers.StartStateSupplierRoadXPos1AnyLane;
import core.foundation.gadget.cond.Counter;
import core.foundation.util.cond.ConditionalsUtil;
import core.gridrl.StateGrid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestStartStateSupplierRoadXPos1AnyLane {

    @Test
    void testCreate() {
        var supplier = StartStateSupplierRoadXPos1AnyLane.create();
        assertNotNull(supplier);
    }

    @Test
    void testEnvironmentName() {
        StartStateSupplierRoadXPos1AnyLane supplier = StartStateSupplierRoadXPos1AnyLane.create();
        assertEquals(EnvironmentRoad.NAME, supplier.environmentName());
    }

    @Test
    void testGetStartState() {
        StartStateSupplierRoadXPos1AnyLane supplier = StartStateSupplierRoadXPos1AnyLane.create();
        StateGrid startState = supplier.getStartState();
        assertTrue(startState.equals(StateGrid.of(1, 0)) || startState.equals(StateGrid.of(1, 1)));
    }

    @Test
    void testGetStartStateMultipleCalls() {
        StartStateSupplierRoadXPos1AnyLane supplier = StartStateSupplierRoadXPos1AnyLane.create();

        var counter0= Counter.empty();
        var counter1= Counter.empty();
        for (int i = 0; i <100 ; i++) {
            StateGrid startState = supplier.getStartState();
            ConditionalsUtil.executeIfTrue(startState.equals(StateGrid.of(1, 0)), () -> counter0.increase());
            ConditionalsUtil.executeIfTrue(startState.equals(StateGrid.of(1, 1)), () -> counter1.increase());
        }

        System.out.println("counter0 = " + counter0);
        System.out.println("counter1 = " + counter1);

        Assertions.assertTrue(counter0.getCount() > 0);
        Assertions.assertTrue(counter1.getCount() > 0);



    }

}
