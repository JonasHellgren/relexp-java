package chapters.ch7;

import chapters.ch4.implem.treasure.start_state_suppliers.StartStateSupplierPositionGiven;
import chapters.ch7.domain.trainer.TrainerOneStepTdQLearningWithSafety;
import chapters.ch7.factory.SafetyLayerFactoryTreasure;
import chapters.ch7.factory.TrainerDependencySafeFactory;
import core.foundation.util.collections.MyListUtils;
import core.gridrl.StateGrid;
import core.plotting.progress_plotting.ProgressMeasureEnum;
import core.plotting.progress_plotting.RecorderProgressMeasures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestTrainerOneStepTdQLearningWithSafetyHardCodedFails {

    TrainerOneStepTdQLearningWithSafety trainerStartAt01,trainerStartAt52;

    @BeforeEach
    void init() {
        var dependencies1 = TrainerDependencySafeFactory.treasure(10_000, 0.1, 0.9);
        var safetyLayer = SafetyLayerFactoryTreasure.produce(dependencies1);
        trainerStartAt01 = TrainerOneStepTdQLearningWithSafety.givenSafetyLayerOf(dependencies1, safetyLayer);
        trainerStartAt01.train();
        var dependencies2 =dependencies1.withStartStateSupplier(
                StartStateSupplierPositionGiven.create(StateGrid.of(5,1)));
        trainerStartAt52 = TrainerOneStepTdQLearningWithSafety.givenSafetyLayerOf(dependencies2, safetyLayer);
        trainerStartAt52.train();
    }

    /**
     * Starting most-left (0,1) shall never result in hitting some of the two first fail states (1,0) and (1,2)
     * Consequently, the number of steps shall be at least 4
     */

    @Test
    void givenStartAt01_whenTrained_thenNotFewSteps() {
        double minNSteps = getMinNSteps(trainerStartAt01.getRecorder());
        assertTrue(minNSteps > 4);
    }

    /**
     * Similar to the previous test, but starting at (5,1)
     */

    @Test
    void givenStartAt52_whenTrained_thenNotFewSteps() {
        double minNSteps = getMinNSteps(trainerStartAt52.getRecorder());
        assertTrue(minNSteps > 3);
    }

    private static double getMinNSteps(RecorderProgressMeasures recorder) {
        var nStepsList = recorder.trajectory(ProgressMeasureEnum.N_STEPS);
        return MyListUtils.findMin(nStepsList).orElseThrow();
    }

     @Test
      void whenTrained_thenGoodReturns() {
         var returns = trainerStartAt01.getRecorder().trajectory(ProgressMeasureEnum.RETURN);
         double avgReturn = MyListUtils.findAverage(returns).orElse(0.0);
         System.out.println("avgReturn = " + avgReturn);
         Assertions.assertTrue(avgReturn > 50.0);
      }

}
