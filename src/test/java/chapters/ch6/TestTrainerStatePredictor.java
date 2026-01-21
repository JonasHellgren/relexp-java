package chapters.ch6;

import chapters.ch6.domain.trainers.state_predictor.TrainerStatePredictor;
import chapters.ch6.implem.factory.TrainerDependenciesFactorySplitting;
import core.gridrl.StateGrid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTrainerStatePredictor {

    public static final int N_EPISODES = 1000;
    public static final double TOL = 0.3;
    public static final double LEARNING_RATE_START = 0.1;
    TrainerStatePredictor trainerHor1, trainerHor3;

     @BeforeEach
      void init() {
         trainerHor1=defineTrainer(1);
         trainerHor3=defineTrainer(3);
     }

    private TrainerStatePredictor defineTrainer(int nStepsHorizon) {
        var dependencies= TrainerDependenciesFactorySplitting.givenOptimalPolicySplitting(nStepsHorizon, N_EPISODES, LEARNING_RATE_START);
        return TrainerStatePredictor.of(dependencies);
    }

    @Test
       void whenPredicted_thenCorrect() {
         trainerHor1.train();
         var agent= trainerHor1.getDependencies().agent();
          double value01= agent.read(StateGrid.of(0,1));
          double value22= agent.read(StateGrid.of(2,2));
          Assertions.assertEquals(1.0, value01, TOL);
          Assertions.assertEquals(1.0, value22, TOL);
      }

    @Test
    void whenHor3_thenBetterPred() {
        trainerHor1.train();
        trainerHor3.train();
        var agent1= trainerHor1.getDependencies().agent();
        var agent3= trainerHor3.getDependencies().agent();
        double values01Agent1= agent1.read(StateGrid.of(0,1));
        double values01Agent3= agent3.read(StateGrid.of(0,1));
        double e3=Math.abs(1-values01Agent3);
        double e1=Math.abs(1-values01Agent1);

        System.out.println("e3 = " + e3);
        System.out.println("e1 = " + e1);

        Assertions.assertTrue(e3<e1);
    }


}
