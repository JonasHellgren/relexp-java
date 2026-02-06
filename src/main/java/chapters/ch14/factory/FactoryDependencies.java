package chapters.ch14.factory;

import chapters.ch14.domain.settings.MemorySettings;
import chapters.ch14.domain.settings.PlanningSettings;
import chapters.ch14.domain.settings.TrainerSettings;
import chapters.ch14.domain.trainer.ReplayBuffer;
import chapters.ch14.domain.trainer.TrainerDependencies;
import chapters.ch14.implem.pong.*;
import chapters.ch14.implem.pong_memory.BallHitFloorCalculator;
import chapters.ch14.implem.pong_memory.LongMemoryRbf;
import chapters.ch14.implem.pong_memory.MiniBatchAdapterPong;
import core.foundation.gadget.cond.Counter;
import core.foundation.gadget.timer.CpuTimer;
import lombok.Builder;
import lombok.experimental.UtilityClass;

/**
 * This class provides factory methods for creating TrainerDependencies instances for the Pong environment.
 * It includes methods for creating TrainerDependencies for testing and running.
 */
@UtilityClass
public class FactoryDependencies {

    @Builder
    record Settings(TrainerSettings trainerSettings,
                    PongSettings envSettings,
                    PlanningSettings planningSettings,
                    MemorySettings memorySettings
    ) {
    }

    public static TrainerDependencies<StateLongPong, StatePong, ActionPong> forTest(TrainerSettings trainerSettings) {
        var settings = Settings.builder()
                .trainerSettings(trainerSettings)
                .envSettings(FactoryPongSettings.create())
                .planningSettings(FactoryPlanningSettings.forTest())
                .memorySettings(FactoryMemorySettings.forTest())
                .build();
        return produce(settings);
    }

    public static TrainerDependencies<StateLongPong, StatePong, ActionPong> forRunning() {
        var settings = Settings.builder()
                .trainerSettings(FactoryTrainerSettings.forRunning())
                .envSettings(FactoryPongSettings.create())
                .planningSettings(FactoryPlanningSettings.forRunning())
                .memorySettings(FactoryMemorySettings.forRunning())
                .build();
        return produce(settings);
    }


    private static TrainerDependencies<StateLongPong, StatePong, ActionPong> produce(Settings settings) {
        var stateFactory = FactoryStatePong.of(settings.envSettings);
        var environment = EnvironmentPong.of(settings.envSettings);
        var calculator = BallHitFloorCalculator.of(environment, settings.envSettings);
        var planner = FactoryPlanner.of(settings.planningSettings, calculator, settings.trainerSettings);
       // var memorySettings = FactoryMemorySettings.forTest();
        var memory = LongMemoryRbf.of(settings.memorySettings, settings.envSettings);
        var adapter = MiniBatchAdapterPong.of(settings.envSettings, environment, memory, settings.trainerSettings);

        System.out.println("settings.trainerSettings.maxEpisodes() = " + settings.trainerSettings.maxEpisodes());

        return TrainerDependencies.<StateLongPong, StatePong, ActionPong>builder()
                .trainerSettings(settings.trainerSettings)
                .envSettings(settings.envSettings)
                .environment(environment)
                .stateSupplier(() -> stateFactory.random())
                .planner(planner)
                .longMemory(memory)
                .replayBuffer(ReplayBuffer.of(settings.trainerSettings))
                .miniBatchAdapter(adapter)
                .timeToHitCalculator(calculator)
                .stepCounter(Counter.ofMaxCount(settings.trainerSettings.maxStepsPerEpisode()))
                .episCounter(Counter.ofMaxCount(settings.trainerSettings.maxEpisodes()))
                .timer(CpuTimer.empty())
                .build();
    }

}
