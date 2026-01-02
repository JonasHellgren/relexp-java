package chapters.ch6;

import chapters.ch6.domain.agent.core.AgentGridMultiStepI;
import chapters.ch6.domain.trainer.core.TrainerStateActionControlDuringEpisode;
import chapters.ch6.implem.factory.TrainerDependenciesFactory;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestTrainerStateActionControlDuringEpisodeSplitting {

    public static final int N_EPISODES = 1000;
    public static final double LEARNING_RATE_START = 0.1;
    public static final double TOL = 0.1;
    TrainerStateActionControlDuringEpisode trainerHor1, trainerHor3;

    @BeforeEach
      void init() {
         trainerHor1=defineTrainer(1);
         trainerHor3=defineTrainer(3);
         trainerHor1.train();
         trainerHor3.train();
      }

    private TrainerStateActionControlDuringEpisode defineTrainer(int nStepsHorizon) {
        var dependencies= TrainerDependenciesFactory.learnPolicySplittingDuringEpis(nStepsHorizon, N_EPISODES, LEARNING_RATE_START);
        return TrainerStateActionControlDuringEpisode.of(dependencies);
    }

     @Test
      void givenTrainer1_thenCorrectValues() {
        var agent= trainerHor1.getDependencies().agent();
         assertValues(agent);
     }

    @Test
    void givenTrainer1_thenCorrectStateActionValues() {
        var agent= trainerHor1.getDependencies().agent();
        assertStateActionValues(agent);
    }

    @Test
    void givenTrainer3_thenCorrectValues() {
        var agent= trainerHor3.getDependencies().agent();
        assertValues(agent);
    }

    @Test
    void givenTrainer3_thenCorrectStateActionValues() {
        var agent= trainerHor3.getDependencies().agent();
        assertStateActionValues(agent);
    }

    @Test
    void trainer3BetterValues() {
        var agent1= trainerHor1.getDependencies().agent();
        var agent3= trainerHor3.getDependencies().agent();
        double values01Agent1= agent1.read(StateGrid.of(0,1));
        double values01Agent3= agent3.read(StateGrid.of(0,1));
        double e3=Math.abs(1-values01Agent3);
        double e1=Math.abs(1-values01Agent1);
        Assertions.assertTrue(e3<e1);
    }

    private static void assertStateActionValues(AgentGridMultiStepI agent) {
        var s01=StateGrid.of(0,1);
        var s32=StateGrid.of(3,2);
        Assertions.assertTrue(agent.read(s01, ActionGrid.E)> agent.read(s01,ActionGrid.N));
        Assertions.assertTrue(agent.read(s32,ActionGrid.E)> agent.read(s32,ActionGrid.N));
    }

    private static void assertValues(AgentGridMultiStepI agent) {
        double value01= agent.read(StateGrid.of(0,1));
        double value21= agent.read(StateGrid.of(2,1));
        double value41= agent.read(StateGrid.of(2,1));
        double value40= agent.read(StateGrid.of(4,0));
        double value30= agent.read(StateGrid.of(3,0));
        assertAll("Values do not match expected results",
                 () -> Assertions.assertEquals(1.0, value01, TOL),
                 () -> Assertions.assertEquals(1.0, value21, TOL),
                 () -> Assertions.assertEquals(1.0, value41, TOL),
                 () -> Assertions.assertEquals(0.0, value40, TOL),
                 () -> Assertions.assertEquals(0.0, value30, TOL)
         );
    }


}
