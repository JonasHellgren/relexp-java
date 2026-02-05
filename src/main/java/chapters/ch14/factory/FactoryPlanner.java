package chapters.ch14.factory;

import chapters.ch14.domain.action_roller.ActionSequenceRoller;
import chapters.ch14.domain.planner.Planner;
import chapters.ch14.domain.settings.PlanningSettings;
import chapters.ch14.domain.settings.TrainerSettings;
import chapters.ch14.implem.pong.*;
import chapters.ch14.implem.pong_memory.BallHitFloorCalculator;
import chapters.ch14.implem.pong_memory.StateInterpreterPong;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FactoryPlanner {

    public static Planner<StateLongPong, StatePong, ActionPong> forTest(BallHitFloorCalculator calculator,
                                                                        TrainerSettings trainerSettings) {
        return of(FactoryPlanningSettings.forTest(),calculator,trainerSettings);
    }

    public static Planner<StateLongPong, StatePong, ActionPong> forExecutor(int maxDepth,
                                                                            BallHitFloorCalculator calculator,
                                                                            TrainerSettings trainerSettings) {
        var planningSettings = FactoryPlanningSettings.forExecutor(maxDepth);
        var envSettings = FactoryPongSettings.create();
        var environment = EnvironmentPong.of(envSettings);
        var interpreter = StateInterpreterPong.create(calculator);
        var actionSequenceRoller = ActionSequenceRoller.of(environment, interpreter, trainerSettings);
        var actionSelectorPongMixed = ActionSelectorPong.of(planningSettings);
        return Planner.create(actionSelectorPongMixed, actionSequenceRoller, planningSettings);
    }


    public static Planner<StateLongPong, StatePong, ActionPong> of(PlanningSettings planningSettings,
                                                                   BallHitFloorCalculator calculator,
                                                                   TrainerSettings trainerSettings) {
        var envSettings = FactoryPongSettings.create();
        var environment = EnvironmentPong.of(envSettings);
        var interpreter = StateInterpreterPong.create(calculator);
        var actionSequenceRoller = ActionSequenceRoller.of(environment, interpreter, trainerSettings);
        var actionSelectorPongMixed = ActionSelectorPong.of(planningSettings);
        return Planner.create(actionSelectorPongMixed, actionSequenceRoller, planningSettings);
    }

}
