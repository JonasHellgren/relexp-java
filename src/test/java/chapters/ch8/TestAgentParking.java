package chapters.ch8;

import chapters.ch8.domain.agent.core.AgentParking;
import chapters.ch8.domain.agent.core.ExperienceParking;
import chapters.ch8.domain.agent.memory.AgentMemory;
import chapters.ch8.domain.agent.param.AgentParkingParameters;
import chapters.ch8.domain.environment.core.ActionParking;
import chapters.ch8.domain.environment.core.FeeEnum;
import chapters.ch8.domain.environment.core.StateParking;
import chapters.ch8.domain.environment.param.ParkingParameters;
import chapters.ch8.domain.environment.startstate_supplier.StartStateSupplier;
import chapters.ch8.factory.AgentMemoryFactory;
import chapters.ch8.factory.AgentParkingParametersFactory;
import chapters.ch8.factory.ParkingParametersFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAgentParking {

    private ParkingParameters parkingParameters;
    private AgentParkingParameters agentParameters;
    private AgentMemory memory;
    private AgentParking agentParking;

    @BeforeEach
    void init() {
        parkingParameters = ParkingParametersFactory.forTest();
        agentParameters = AgentParkingParametersFactory.forTest();
        memory = AgentMemoryFactory.createMock(parkingParameters);
        agentParking = AgentParking.of(agentParameters);
        agentParking.setMemory(memory);
    }

    @Test
    void testChooseActionRandom() {
        var stateParking = StateParking.ofStart(0, FeeEnum.NotCharging);
        ActionParking chosenAction = agentParking.chooseAction(stateParking, 0.5);
        assertTrue(ActionParking.allActions().contains(chosenAction));
    }

    /**
     * Data is mocked so ACCEPT has higher value for all states
     */

    @Test
    void testChooseActionNoExploration() {
        var stateParking= StartStateSupplier.RANDOMOCCUP_RANDOMFEE.of(parkingParameters).state();
        ActionParking chosenAction = agentParking.chooseActionNoExploration(stateParking);
        assertEquals(ActionParking.ACCEPT, chosenAction);
    }

    @Test
    void testFitMemory() {
        // Arrange
        double learningRate = 0.1;
        double deltaReward = 10.0;
        var experienceParking= ExperienceParking.builder()
                .state(StateParking.ofStart(0, FeeEnum.NotCharging))
                .action(ActionParking.ACCEPT)
                .reward(deltaReward)
                .stateNew(StateParking.ofStart(1, FeeEnum.NotCharging))
                .actionNew(ActionParking.ACCEPT)
                .rewardAverage(0)
                .isTerminal(false)
                .build();
        agentParking.fitMemory(experienceParking, learningRate);
        assertTrue(memory.read(experienceParking.state(), experienceParking.action()) > 0.0);
    }

}