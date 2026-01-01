package core.foundation.gadget.training;
import lombok.experimental.UtilityClass;

/**
 * This class provides methods for calculating various values related to reinforcement learning.
 */

@UtilityClass
public final class ValueCalculator {

    /**
     * Calculates the value of a single step in a Markov decision process.
     *
     * This method combines the immediate reward with the discounted value of the next state.
     *
     * @param reward the immediate reward received for taking an action in a state
     * @param discountFactor the discount factor, representing the relative importance of future rewards
     * @param valueStateNext the value of the state after taking the action
     * @return the value of the single step
     */
    public static double calculateSingleStepValue(double reward,
                                                  double discountFactor,
                                                  double valueStateNext) {
        return reward + discountFactor * valueStateNext;
    }
    
    /**
     * Calculates the value of taking an action in a given state for multiple steps.
     *
     * @param sumRewards the sum of rewards received so far
     * @param discountFactorPowN the power of the discount factor discountFactor raised to the number of steps
     * @param valueStateFuture the value of the state after the specified number of steps
     * @return the value of taking an action in the given state for multiple steps
     */
    public static double calculateManyStepsValue(double sumRewards,
                                                 double discountFactorPowN,
                                                 double valueStateFuture) {
        return sumRewards + discountFactorPowN * valueStateFuture;
    }

    /**
     * Calculates the advantage of taking an action in a given state.
     *
     * @param valueStateFuture the value of the state after taking the action
     * @param valueStatePresent the value of the state before taking the action
     * @return the advantage of taking an action in the given state
     */
    public static double calculateAdvantage(double valueStateFuture,
                                            double valueStatePresent) {
        return valueStateFuture - valueStatePresent;
    }

    /**
     * Calculates the temporal difference error for a given experience.
     *
     * @param reward the reward received for taking an action in a state
     * @param discountFactor the discount factor
     * @param valueStateNext the value of the state after taking the action
     * @param valueStatePresent the value of the state before taking the action
     * @return the temporal difference error for the given experience
     */
    public static double calculateTemporalDifferenceError(double reward,
                                                          double discountFactor,
                                                          double valueStateNext,
                                                          double valueStatePresent) {
        return calculateSingleStepValue(reward, discountFactor, valueStateNext) - valueStatePresent;
    }


    /**
     * Calculates the target value for a given experience.
     *If the current state is a terminal state, the target value is the reward
     *Otherwise, calculate the target value using the Q-learning update rule
     *
     * @param isTransitionToTerminal whether the current state is a terminal state
     * @param reward the reward received for taking an action in the current state
     * @param qNextMax the maximum Q-value of the next state
     * @param discountFactor the discount factor for future rewards
     * @return the target value for the given experience
     */
    public static double calculateTargetValue(boolean isTransitionToTerminal,
                                              double reward,
                                              double qNextMax,
                                              double discountFactor) {

        return (isTransitionToTerminal)
                ? reward
                : ValueCalculator.calculateSingleStepValue(reward, discountFactor, qNextMax);
    }


}
