package chapters.ch12.domain.inv_pendulum.trainer.core;

import chapters.ch12.domain.inv_pendulum.agent.memory.ActionAndItsValue;
import chapters.ch12.domain.inv_pendulum.environment.core.ActionPendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.StatePendulum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for calculating target values for a given mini-batch of experiences.
 * It uses the provided TrainerDependencies to access the agent and trainer parameters.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TargetCalculator {

    private final TrainerDependencies dependencies;

    public static TargetCalculator of(TrainerDependencies dependencies) {
        return new TargetCalculator(dependencies);
    }

    public List<ActionAndItsValue> calculateTargets(MiniBatch mb) {
        List<ActionAndItsValue> avList = new ArrayList<>();
        double discountFactor = dependencies.trainerParameters().discountFactor();
        for (ExperiencePendulum e : mb) {
            var sNew = e.stateNew();
            var aBest = dependencies.agent().chooseActionNoExploration(sNew);
            double valueBestAction = getValueBestAction(sNew, aBest);
            double reward = e.reward();
            double targetValue = e.isTerminal()
                    ? reward
                    : reward + discountFactor * valueBestAction;
            avList.add(ActionAndItsValue.of(e.action(), targetValue));
        }
        return avList;
    }

    private double getValueBestAction(StatePendulum sNew, ActionPendulum aBest) {
        var avListForSnew = dependencies.agent().readTargetMemory(sNew);
        var av = ActionAndItsValue.findActionValue(aBest, avListForSnew);
        return av.actionValue();
    }
}
