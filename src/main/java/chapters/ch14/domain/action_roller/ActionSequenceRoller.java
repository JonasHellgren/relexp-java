package chapters.ch14.domain.action_roller;

import chapters.ch14.domain.environment.EnvironmentI;
import chapters.ch14.domain.environment.StepReturn;
import chapters.ch14.domain.long_memory.LongMemory;
import chapters.ch14.domain.settings.TrainerSettings;
import chapters.ch14.domain.state_intepreter.StateInterpreterI;
import com.google.common.base.Preconditions;
import core.foundation.util.cond.ConditionalsUtil;
import core.learningutils.MyRewardListUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is responsible for rolling out a sequence of actions and calculating the rewards and returns.
 * It takes an environment, a state interpreter, and trainer settings as input.
 * TrainerSettings are used by this class because it contains gamma (discount factor)
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log
public class ActionSequenceRoller<SI, S, A> {

    private final EnvironmentI<S, A> environment;
    private final StateInterpreterI<SI, S> interpreter;
    private final TrainerSettings trainerSettings;

    public static <SI, S, A> ActionSequenceRoller<SI, S, A> of(EnvironmentI<S, A> environment,
                                                               StateInterpreterI<SI, S> interpreter,
                                                               TrainerSettings trainerSettings) {
        return new ActionSequenceRoller<>(environment, interpreter, trainerSettings);
    }

    /**
     * Rolls out a sequence of actions and calculates the rewards and returns.
     *
     * @param s the initial state
     * @param actions the sequence of actions to roll out
     * @param memory the long memory that stores the state interpreter's state
     * @return a RollingResult object that contains the accumulated sum of rewards, whether
     * the rollout is terminal or failed, and the number of steps
     */
    public RollingResult roll(S s, List<A> actions, LongMemory<SI> memory) {
        Preconditions.checkArgument(!actions.isEmpty(), "no actions provided");
        List<Double> rewards = new ArrayList<>();
        StepReturn<S> sr = null;
        for (A a : actions) {
            sr = environment.step(s, a);
            rewards.add(sr.reward());
            if (sr.isTerminal()) {
                break;
            }
            s = sr.stateNew();
        }

        maybeAddLongMemoryValue(memory, sr, rewards);
        double accRewards = MyRewardListUtils.discountedSum(rewards, trainerSettings.gamma());
        return RollingResult.builder()
                .accRewards(accRewards)
                .isEndTerminal(sr.isTerminal())
                .isEndFail(sr.isFail())
                .nSteps(rewards.size())
                .build();
    }

    private void maybeAddLongMemoryValue(LongMemory<SI> memory, StepReturn<S> sr, List<Double> rewards) {
        ConditionalsUtil.executeIfTrue(!sr.isTerminal(), () ->
            rewards.add(memory.read(interpreter.interpret(sr.stateNew()))));
    }
}
