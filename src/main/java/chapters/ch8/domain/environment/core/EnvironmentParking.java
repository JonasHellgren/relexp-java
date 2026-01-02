package chapters.ch8.domain.environment.core;

import chapters.ch8.domain.environment.param.ParkingParameters;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

/**
 * Represents a parking environment, which manages the state of the parking lot and the actions taken by agents.
 */

@Log
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class EnvironmentParking {

    public record NewStateResult(StateParking state, boolean isPark, boolean isDeparting) {
        public static NewStateResult of(StateParking state, boolean isPark, boolean isDeparted) {
            return new NewStateResult(state, isPark, isDeparted);
        }
    }

    ParkingParameters parameters;

    public static EnvironmentParking of(ParkingParameters parameters) {
        return new EnvironmentParking(parameters);
    }

    /**
     * Takes a step in the environment, updating the state and calculating the reward.
     *
     * @param state  the current state
     * @param action the action taken by the agent
     * @return the result of the step, including the new state, whether a park action was accepted, and the reward
     */
    public StepReturnParking step(StateParking state, ActionParking action) {
        var res = getStateNew(state, action);
        boolean isTerminal = parameters.isMaxStepsExceeded(state);
        return StepReturnParking.of(res, isTerminal, getReward(state, res.isPark));
    }

    private NewStateResult getStateNew(StateParking state, ActionParking action) {
        int nOccupied = state.nOccupied();
        boolean isArriving = parameters.isArriving();
        boolean isPark = isArrivingAndParkingAccepted(action, nOccupied, isArriving);
        int nOccupiedNew = increaseNofOccupiedIfPark(nOccupied, isPark);
        boolean isDeparting = parameters.isDeparting();
        nOccupiedNew = decreaseNofOccupiedIfSomeIsDeparting(nOccupiedNew, isDeparting);
        var fee = isArriving ? FeeEnum.random() : state.fee();
        int nStepsIncreased = state.nSteps() + 1;
        return NewStateResult.of(
                StateParking.of(nOccupiedNew, fee, nStepsIncreased),
                isPark,
                isDeparting);
    }

    private boolean isArrivingAndParkingAccepted(ActionParking action,
                                                 int nOccupied,
                                                 boolean isArriving) {
        boolean isAvailable = nOccupied < parameters.nSpots();
        boolean isAccept = action.equals(ActionParking.ACCEPT);
        return isArriving && isAvailable && isAccept;
    }


    private int decreaseNofOccupiedIfSomeIsDeparting(int nOccupied, boolean isDeparting) {
        return Math.max(0, isDeparting ? nOccupied - 1 : nOccupied);
    }

    private int increaseNofOccupiedIfPark(int nOccupied, boolean isPark) {
        return Math.min(parameters.nSpots(), isPark ? nOccupied + 1 : nOccupied);
    }

    /**
     * The fee used here was calculated earlier, at the step a vehicle arrived.
     */
    private double getReward(StateParking state, boolean isPark) {
        return isPark ? state.fee().feeValue(parameters) : 0d;
    }


}
