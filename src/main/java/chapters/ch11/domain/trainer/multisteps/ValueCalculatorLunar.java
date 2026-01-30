package chapters.ch11.domain.trainer.multisteps;

import chapters.ch11.domain.agent.core.AgentLunar;
import chapters.ch11.domain.environment.core.StateLunar;
import chapters.ch11.domain.trainer.core.ExperienceLunar;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import static core.foundation.gadget.training.ValueCalculator.*;

/**
 * This class calculates the value and advantage of taking an action in a given state.
 * It also calculates the temporal difference error for a given experience.
 */

@AllArgsConstructor
public class ValueCalculatorLunar {
    public static final double VALUE_TERM = 0d;

    private final TrainerDependencies dependencies;

    public static ValueCalculatorLunar of(TrainerDependencies dependencies) {
        return new ValueCalculatorLunar(dependencies);
    }

    /**
     * Calculates the value of taking an action in a given state.
     *
     * @param stateFuture the future state of the environment
     * @param sumRewards the sum of rewards received so far
     * @return the value of taking an action in the given state
     */
    public  double valueOfTakingAction(StateLunar stateFuture,
                                       double sumRewards) {
        var agent=dependencies.agent();
        var parameters=dependencies.trainerParameters();
        return calculateManyStepsValue(sumRewards, parameters.gammaPowN(), agent.readCritic(stateFuture));
    }

    /**
     * Calculates the advantage of taking an action in a given state.
     *
     * @param agent the agent taking the action
     * @param state the current state of the environment
     * @param value the value of taking an action in the given state
     * @return the advantage of taking an action in the given state
     */
    public  double calcAdvantage(@NotNull AgentLunar agent, StateLunar state, double value) {
        double valueCritic = agent.readCritic(state);
        return calculateAdvantage(value, valueCritic);
    }

    /**
     * Calculates the temporal difference error for a given experience.
     *
     * @param experience the experience to calculate the error for
     * @return the temporal difference error for the given experience
     */
    public double calcTemporalDifferenceError(ExperienceLunar experience) {
        var agent = dependencies.agent();
        double valueCritic = agent.readCritic(experience.state());
        double vNext = experience.isTransitionToTerminal()
                ? VALUE_TERM
                : agent.readCritic(experience.stateNew());
        return calculateTemporalDifferenceError(
                experience.reward(),dependencies.getGamma(),vNext,valueCritic);
    }


    public double calcTemporalDifferenceErrorBestAction(ExperienceLunar experience) {
        var agent = dependencies.agent();
        var env=dependencies.environment();
        double aBest=agent.chooseActionNoExploration(experience.state());
        var sr=env.step(experience.state(),aBest);
        return calcTemporalDifferenceError(ExperienceLunar.of(experience.state(),aBest,sr));

    }
}
