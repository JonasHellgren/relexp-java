package chapters.ch14.factory;

import chapters.ch14.domain.settings.TrainerSettings;
import chapters.ch14.domain.trainer.ReplayBuffer;
import chapters.ch14.domain.trainer.TrainerDependencies;
import chapters.ch14.implem.pong.ActionPong;
import chapters.ch14.implem.pong.EnvironmentPong;
import chapters.ch14.implem.pong.StateLongPong;
import chapters.ch14.implem.pong.StatePong;
import chapters.ch14.implem.pong_memory.BallHitFloorCalculator;
import chapters.ch14.implem.pong_memory.LongMemoryRbf;
import chapters.ch14.implem.pong_memory.MiniBatchAdapterPong;
import lombok.experimental.UtilityClass;

/**
 * This class provides factory methods for creating TrainerDependencies instances for the Pong environment.
 * It includes methods for creating TrainerDependencies for testing and running.
 */
@UtilityClass
public class FactoryDependencies {

    public static TrainerDependencies<StateLongPong, StatePong, ActionPong> forTest(TrainerSettings trainerSettings) {
        var envSettings=FactoryPongSettings.create();
        var stateFactory = FactoryStatePong.of(envSettings);
        var environment = EnvironmentPong.of(envSettings);
        var calculator = BallHitFloorCalculator.of(environment, envSettings);
        var planner = FactoryPlanner.of(FactoryPlanningSettings.forTest(),calculator,trainerSettings);
        var memorySettings = FactoryMemorySettings.forTest();
        var memory = LongMemoryRbf.of(memorySettings, envSettings);
        var adapter= MiniBatchAdapterPong.of(envSettings,environment, memory, trainerSettings);
        return TrainerDependencies.<StateLongPong,StatePong,ActionPong>builder()
                .trainerSettings(trainerSettings)
                .envSettings(envSettings)
                .environment(environment)
                .stateSupplier(() -> stateFactory.random())
                .planner(planner)
                .longMemory(memory)
                .replayBuffer(ReplayBuffer.of(trainerSettings))
                .miniBatchAdapter(adapter)
                .timeToHitCalculator(calculator)
                .build();
    }

    public static TrainerDependencies<StateLongPong,StatePong, ActionPong> forRunning() {
        var envSettings=FactoryPongSettings.create();
        var stateFactory = FactoryStatePong.of(envSettings);
        var environment = EnvironmentPong.of(envSettings);
        var calculator = BallHitFloorCalculator.of(environment, envSettings);
        var trainerSettings = FactoryTrainerSettings.forRunning();
        var planningSettings=FactoryPlanningSettings.forRunning();
        var planner = FactoryPlanner.of(planningSettings,calculator,trainerSettings);
        var memorySettings = FactoryMemorySettings.forRunning();
        var memory = LongMemoryRbf.of(memorySettings, envSettings);
        var adapter= MiniBatchAdapterPong.of(envSettings,environment, memory, trainerSettings);
        return TrainerDependencies.<StateLongPong,StatePong,ActionPong>builder()
                .trainerSettings(trainerSettings)
                .envSettings(envSettings)
                .environment(environment)
                .stateSupplier(() -> stateFactory.random())
                .planner(planner)
                .longMemory(memory)
                .replayBuffer(ReplayBuffer.of(trainerSettings))
                .miniBatchAdapter(adapter)
                .timeToHitCalculator(calculator)
                .build();
    }


}
