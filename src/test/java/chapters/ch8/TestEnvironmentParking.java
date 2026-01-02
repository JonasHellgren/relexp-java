package chapters.ch8;

import chapters.ch8.domain.environment.core.ActionParking;
import chapters.ch8.domain.environment.core.EnvironmentParking;
import chapters.ch8.domain.environment.core.FeeEnum;
import chapters.ch8.domain.environment.core.StateParking;
import chapters.ch8.domain.environment.param.ParkingParameters;
import chapters.ch8.factory.ParkingParametersFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestEnvironmentParking {

    public static final int N_STEPS = 100;
    EnvironmentParking environment;
    ParkingParameters parameters;
    List<FeeEnum> possibleFeesOfNextArriving;
     @BeforeEach
      void init() {
       parameters = ParkingParametersFactory.forTest();
       environment = EnvironmentParking.of(parameters);
       possibleFeesOfNextArriving=FeeEnum.allFees();
      }

    @Test
    public void testStep_ArrivingAndParkingAccepted() {
        var fee = FeeEnum.NotCharging;
        var state = StateParking.of(0, fee, 0);
        var action = ActionParking.ACCEPT;
        var possibleRewards=List.of(0d,fee.feeValue(parameters));
        var result = environment.step(state, action);

        assertTrue(result.isPark()==true || result.isPark()==false);
        assertTrue(result.stateNew().nOccupied()>=0);
        assertTrue(possibleFeesOfNextArriving.contains(result.stateNew().fee()));
        assertEquals(1, result.stateNew().nSteps());
        assertTrue(possibleRewards.contains(result.reward()));
    }

    @Test
    public void testStep_ArrivingAndParkingRejected() {
        var fee = FeeEnum.NotCharging;
        var state = StateParking.of(0, fee, 0);
        var action = ActionParking.REJECT;
        var possibleRewards=List.of(0d);
        var result = environment.step(state, action);

        assertFalse(result.isPark());
        assertTrue(result.stateNew().nOccupied()==0);
        assertTrue(possibleFeesOfNextArriving.contains(result.stateNew().fee()));
        assertEquals(1, result.stateNew().nSteps());
        assertTrue(possibleRewards.contains(result.reward()));
    }

    @Test
    public void givenAllOccupied_whenStepAccepted_thenMaybeFewerOccupied() {
        var fee = FeeEnum.NotCharging;
        var state = StateParking.of(parameters.nSpots(), fee, 0);
        var action = ActionParking.ACCEPT;
        var possibleRewards=List.of(0d);
        var possibleNOccupied=List.of(parameters.nSpots(),parameters.nSpots()-1);
        var result = environment.step(state, action);

        assertFalse(result.isPark());
        assertTrue(possibleNOccupied.contains(result.stateNew().nOccupied()));
        assertTrue(possibleFeesOfNextArriving.contains(result.stateNew().fee()));
        assertEquals(1, result.stateNew().nSteps());
        assertTrue(possibleRewards.contains(result.reward()));
    }


    @Test
    public void givenThreeOccupied_whenSteppingManyTimesAccepted_thenSometimesFewerOccupiedAndSomeTimesMoreOccupied() {
        int nAvailStart = 3;
        var state = StateParking.of(nAvailStart, FeeEnum.NotCharging, 0);
        Set<Integer> nOccupiedAfterStep = getSetnOccupiedAfterManySteps(state);
        assertTrue(nOccupiedAfterStep.contains(nAvailStart-1));
        assertTrue(nOccupiedAfterStep.contains(nAvailStart));
        assertTrue(nOccupiedAfterStep.contains(nAvailStart+1));
    }

    @Test
    public void givenZeroOccupied_whenSteppingManyTimesAccepted_thenSomeTimesMoreOccupied() {
        int nAvailStart = 0;
        var state = StateParking.of(nAvailStart, FeeEnum.NotCharging, 0);
        Set<Integer> nOccupiedAfterStep = getSetnOccupiedAfterManySteps(state);
        assertFalse(nOccupiedAfterStep.contains(nAvailStart-1));
        assertTrue(nOccupiedAfterStep.contains(nAvailStart));
        assertTrue(nOccupiedAfterStep.contains(nAvailStart+1));
    }

    @Test
    public void givenFiveOccupied_whenSteppingManyTimesAccepted_thenSometimesFewerOccupied() {
        int nAvailStart = 5;
        var state = StateParking.of(nAvailStart, FeeEnum.NotCharging, 0);
        Set<Integer> nOccupiedAfterStep = getSetnOccupiedAfterManySteps(state);
        assertTrue(nOccupiedAfterStep.contains(nAvailStart-1));
        assertTrue(nOccupiedAfterStep.contains(nAvailStart));
        assertFalse(nOccupiedAfterStep.contains(nAvailStart+1));
    }

    @Test
    public void givenZeroOccupied_whenSteppingManyTimesAccepted_thenSomeFull() {
        int nAvailStart = 0;
        var state = StateParking.of(nAvailStart, FeeEnum.NotCharging, 0);
        var possibleEndNofOccupied=List.of(parameters.nSpots(),parameters.nSpots()-1);
        var env = EnvironmentParking.of(parameters.withProbDeparting(0.01));
        for (int i = 0; i < N_STEPS; i++) {
            var result = env.step(state, ActionParking.ACCEPT);
            state=result.stateNew();
        }
        assertTrue(possibleEndNofOccupied.contains(state.nOccupied()));
    }



     Set<Integer> getSetnOccupiedAfterManySteps(StateParking state) {
        Set<Integer> nOccupiedAfterStep=new HashSet<>();
        for (int i = 0; i < N_STEPS; i++) {
            var result = environment.step(state, ActionParking.ACCEPT);
            nOccupiedAfterStep.add(result.stateNew().nOccupied());
        }
        return nOccupiedAfterStep;
    }

}