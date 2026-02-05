package chapters.ch14;

import chapters.ch14.domain.trainer.Trainer;
import chapters.ch14.domain.trainer.TrainerDependencies;
import chapters.ch14.implem.pong.ActionPong;
import chapters.ch14.implem.pong.StateLongPong;
import chapters.ch14.implem.pong.StatePong;
import chapters.ch14.implem.pong_memory.BallHitFloorCalculator;
import chapters.ch14.implem.pong_memory.StateAdapterPong;
import chapters.ch14.factory.FactoryDependencies;
import chapters.ch14.factory.FactoryStatePong;
import chapters.ch14.factory.FactoryTrainerSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TestTrainer {

    TrainerDependencies<StateLongPong, StatePong, ActionPong> dependencies;
    Trainer<StateLongPong, StatePong, ActionPong> trainer;

    @BeforeEach
    void init() {
        dependencies = FactoryDependencies.forTest(FactoryTrainerSettings.forTestLongMemFit());
        trainer = Trainer.of(dependencies);
    }


    @Test
    @Disabled("Time consuming")
    void givenTrainer_whenTrain_thenCorrectMemValueForDoomedState() {
        trainer.train();
        var factory = FactoryStatePong.of(dependencies.envSettings());
        var memory = dependencies.longMemory();
        var timeCalculator = BallHitFloorCalculator.of(dependencies.environment(), dependencies.envSettings());

        for (int i = 0; i < 10; i++) {
            var state = factory.stateBallXMaxPaddleMid_HeadSouth(i / 10.0);
            timeCalculator = BallHitFloorCalculator.of(dependencies.environment(), dependencies.envSettings());
            var stateLong = StateAdapterPong.stateLong(timeCalculator, state);
            double val = memory.read(stateLong);
            System.out.println("stateLong = " + stateLong + "val = " + val);
        }

        var stateDoomed = factory.doomed();
        var stateLong = StateAdapterPong.stateLong(timeCalculator, stateDoomed);
        double val = memory.read(stateLong);
        System.out.println("stateLong = " + stateLong);
        System.out.println("val = " + val);

        Assertions.assertTrue(val < -10);

    }


}
