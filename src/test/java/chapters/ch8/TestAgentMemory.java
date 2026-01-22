package chapters.ch8;

import chapters.ch8.domain.agent.memory.AgentMemory;
import chapters.ch8.domain.agent.memory.StateActionParking;
import chapters.ch8.domain.agent.param.AgentParkingParameters;
import chapters.ch8.domain.environment.core.ActionParking;
import chapters.ch8.domain.environment.core.FeeEnum;
import chapters.ch8.domain.environment.core.StateParking;
import chapters.ch8.domain.environment.param.ParkingParameters;
import chapters.ch8.domain.environment.startstate_supplier.StartStateSupplier;
import chapters.ch8.factory.AgentMemoryFactory;
import chapters.ch8.factory.AgentParkingParametersFactory;
import chapters.ch8.factory.ParkingParametersFactory;
import core.foundation.gadget.cond.Counter;
import core.foundation.util.rand.RandUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestAgentMemory {

    public static final double TOL = 1e-6;
    private AgentParkingParameters agentParameters;
    private AgentMemory agentMemory;

    @BeforeEach
    public void setup() {
        agentParameters = AgentParkingParametersFactory.forTest(); // Initialize with default values
        agentMemory = AgentMemory.of(agentParameters);
    }

    @Test
    public void testReadActionValues() {
        StateParking state = StateParking.ofStart(0, FeeEnum.NotCharging);
        Map<ActionParking, Double> actionValues = agentMemory.readActionValues(state);
        assertEquals(ActionParking.allActions().size(), actionValues.size());
    }

    @Test
    public void testValue() {
        StateParking state = StateParking.ofStart(0, FeeEnum.NotCharging);
        double maxValue = agentMemory.value(state);
        assertEquals(agentParameters.defaultValueStateAction(), maxValue, TOL);
    }

    @Test
    public void testFit() {
        StateParking state = StateParking.ofStart(0, FeeEnum.NotCharging);
        ActionParking action = ActionParking.allActions().get(0);
        StateActionParking sa = StateActionParking.of(state, action);
        double valueTar = 10.0;
        double learningRate = 0.1;
        double err = agentMemory.fit(sa, valueTar, learningRate);
        assertNotEquals(0.0, err);
        assertNotEquals(0.0, agentMemory.read(sa));
    }

    @Test
    public void testWriteAndRead() {
        StateParking state = StateParking.ofStart(0, FeeEnum.NotCharging);
        ActionParking action = ActionParking.allActions().get(0); // Choose the first action
        StateActionParking sa = StateActionParking.of(state, action);
        double value = 10.0;
        agentMemory.write(sa, value);
        double readValue = agentMemory.read(sa);
        assertEquals(value, readValue, TOL);
    }

    @Test
    public void testReadUnknownStateAction() {
        StateParking state = StateParking.ofStart(0, FeeEnum.NotCharging);
        ActionParking action = ActionParking.allActions().get(0); // Choose the first action
        StateActionParking sa = StateActionParking.of(state, action);
        double readValue = agentMemory.read(sa);
        assertEquals(0.0, readValue, TOL); // Default value should be 0.0
    }

    @Test
    void correctHashing_thenCorrectNofItemsMemory() {
        var parameters = ParkingParametersFactory.forTest();
        var counter = Counter.empty();
        AgentMemoryFactory.fillMemory(parameters, counter, agentMemory);
        Assertions.assertEquals(counter.getCount(), agentMemory.getNumberOfFittedStateActions());
    }

    /**
     * A very important test, checks correct hashing. Fails if no equals/hashcode is implemented in StateParking.
     */
    @Test
    void correctHashing_thenCorrectValue() {
        var parameters = ParkingParametersFactory.forTest();
        var counter = Counter.empty();
        AgentMemoryFactory.fillMemory(parameters, counter, agentMemory);
        for (int i = 0; i < 100 ; i++) {
            assertRandomActionValue(parameters);
        }
    }

    /**
     * Tricky one. S2 has different nof steps but they shall give same memory location.
     */

     @Test
      void noDuplicateStateActionObjectsInMemory() {
         var parameters = ParkingParametersFactory.forTest();
         var s1 = StartStateSupplier.RANDOMOCCUP_RANDOMFEE.of(parameters).state();
         int nSteps = 1;
         var s2 = StateParking.of(s1.nOccupied(),s1.fee(), nSteps);  //has different nof steps
         var a=ActionParking.random();

         var sa1 = StateActionParking.of(s1, a);
         var sa2 = StateActionParking.of(s2, a);
         double value = RandUtil.randomNumberBetweenZeroAndOne();
         agentMemory.write(sa1, value);
         agentMemory.write(sa2, value);

         assertEquals(1, agentMemory.getNumberOfFittedStateActions());


     }


    private void assertRandomActionValue(ParkingParameters parameters) {
        var supplier = StartStateSupplier.RANDOMOCCUP_RANDOMFEE;
        var stateRandom = supplier.of(parameters).state();
        for (ActionParking a : ActionParking.allActions()) {
            var value = agentMemory.read(stateRandom, a);
            Assertions.assertEquals(value, AgentMemoryFactory.getMockedValue(stateRandom, a, parameters), TOL);
        }
    }


}