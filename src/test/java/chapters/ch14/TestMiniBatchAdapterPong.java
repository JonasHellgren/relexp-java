package chapters.ch14;

import chapters.ch14.domain.environment.Experience;
import chapters.ch14.domain.trainer.MiniBatchAdapterI;
import chapters.ch14.domain.trainer.TrainerDependencies;
import chapters.ch14.implem.pong.ActionPong;
import chapters.ch14.implem.pong.StateLongPong;
import chapters.ch14.implem.pong.StatePong;
import chapters.ch14.implem.pong_memory.BallHitFloorCalculator;
import chapters.ch14.implem.pong_memory.StateAdapterPong;
import chapters.ch14.factory.FactoryDependencies;
import chapters.ch14.factory.FactoryStatePong;
import chapters.ch14.factory.FactoryTrainerSettings;
import core.foundation.gadget.training.TrainData;
import core.foundation.gadget.training.TrainDataOld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class TestMiniBatchAdapterPong {

    public static final double TOL = 0.1;
    MiniBatchAdapterI<StateLongPong, StatePong, ActionPong> adapter;
    Experience<StatePong, ActionPong> expS,expN;
    TrainerDependencies<StateLongPong, StatePong, ActionPong> dependencies;
    BallHitFloorCalculator timeCalculator;

    @BeforeEach
    void init() {
        dependencies = FactoryDependencies.forTest(FactoryTrainerSettings.forTest());
        adapter = dependencies.miniBatchAdapter();
        var factory = FactoryStatePong.of(dependencies.envSettings());
        expS=createExperience(factory.midMidSouth());
        expN=createExperience(factory.midMidNorth());

        timeCalculator = BallHitFloorCalculator.of(dependencies.environment(), dependencies.envSettings());
    }

    private Experience<StatePong, ActionPong> createExperience(StatePong s1) {
        StatePong s = s1;
        ActionPong a = ActionPong.still;
        var sr = dependencies.step(s, a);
        return Experience.of(s, a, sr);
    }

    @Test
    void zeroMemory() {
        var state = FactoryStatePong.of(dependencies.envSettings()).random();
        var stateLong = StateAdapterPong.stateLong(timeCalculator, state);
        Assertions.assertEquals(0, dependencies.longMemory().read(stateLong), 0.01);
    }

    @Test
    void whenAdaptN_thenCorrect() {
        var miniBatch = adapter.adapt(List.of(expN));
        assertBatch(miniBatch, 1.5, 0, 0);
    }

     @Test
      void whenDomedState_thenStateLongCorrect() {
        var sLong=StateAdapterPong.stateLong(timeCalculator, FactoryStatePong.of(dependencies.envSettings()).doomed());
         Assertions.assertTrue(sLong.getDeltaX() > dependencies.envSettings().xMid());
         Assertions.assertTrue(sLong.timeHit() < sLong.deltaX());
     }


    private static void assertBatch(TrainData miniBatch, double time, int dx, int value) {
        var doubles = miniBatch.input(0);
        double output = miniBatch.output(0);
        Assertions.assertEquals(time, doubles[0], TOL);
        Assertions.assertEquals(dx, doubles[1], TOL);
        Assertions.assertEquals(value, output, TOL);
    }


}
