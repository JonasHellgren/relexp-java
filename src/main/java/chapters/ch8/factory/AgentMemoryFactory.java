package chapters.ch8.factory;

import chapters.ch8.domain.agent.memory.AgentMemory;
import chapters.ch8.domain.agent.memory.StateActionParking;
import chapters.ch8.domain.environment.core.ActionParking;
import chapters.ch8.domain.environment.core.FeeEnum;
import chapters.ch8.domain.environment.core.StateParking;
import chapters.ch8.domain.environment.param.ParkingParameters;
import core.foundation.gadget.cond.Counter;
import lombok.experimental.UtilityClass;

/**
 * Factory class for creating and initializing agent memories.
 */

@UtilityClass
public class AgentMemoryFactory {

    /**
     * Fills the agent memory with mocked values for all possible state-action pairs.
     *
     * @param parameters Parking parameters used to calculate mocked values.
     * @param counter Counter to keep track of the number of state-action pairs written.
     * @param am Agent memory to fill with mocked values.
     */
    public static void fillMemory(ParkingParameters parameters, Counter counter, AgentMemory am) {
        for (ActionParking a : ActionParking.allActions()) {
            for (FeeEnum f : FeeEnum.values()) {
                for (int nOcc = 0; nOcc <= parameters.nSpots(); nOcc++) {
                    var s = StateParking.ofStart(nOcc, f);
                    var sa = StateActionParking.of(s, a);
                    am.write(sa, getMockedValue(s, a, parameters));
                    counter.increase();
                }
            }
        }
    }

    public static double getMockedValue(StateParking stateRandom, ActionParking a, ParkingParameters parameters) {
        return stateRandom.fee().feeValue(parameters) * stateRandom.nOccupied() + a.ordinal();
    }

    public static AgentMemory createMock(ParkingParameters parameters) {
        var agentParameters = AgentParkingParametersFactory.forTest();
        var memory= AgentMemory.of(agentParameters);
        fillMemory(parameters, new Counter(), memory);
        return memory;
    }
}
