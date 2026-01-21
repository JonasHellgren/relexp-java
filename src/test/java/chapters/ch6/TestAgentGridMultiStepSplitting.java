package chapters.ch6;

import chapters.ch3.factory.EnvironmentParametersSplittingFactory;
import chapters.ch3.implem.splitting_path_problem.EnvironmentSplittingPath;
import chapters.ch6.domain.agent.AgentGridMultiStepI;
import chapters.ch6.domain.trainer.result_generator.MultiStepResultGrid;
import chapters.ch6.implem.factory.AgentGridMultiStepFactory;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

public class TestAgentGridMultiStepSplitting {

    public static final StateGrid STATE00 = StateGrid.of(0, 0);
    public static final StateGrid STATE10 = StateGrid.of(1, 0);
    public static final double LEARNING_RATE = 0.1;
    public static final int N_FITS = 1000;
    public static final double TOL = 0.1;
    public static final double PROB_RANDOM_DUMMY = 1.0;
    AgentGridMultiStepI agent;
    EnvironmentSplittingPath environment;

    @BeforeEach
    void init() {
        agent = AgentGridMultiStepFactory.createSplittingBestAction();
        var gridParameters = EnvironmentParametersSplittingFactory.produce();
        environment = EnvironmentSplittingPath.of(gridParameters);
    }

    @Test
    void givenState01_whenChooseAction_thenCorrect() {
        var action=agent.chooseAction(StateGrid.of(0,1), PROB_RANDOM_DUMMY);
        Assertions.assertEquals(ActionGrid.E, action);
    }

    @Test
    void givenState21_whenChooseAction_thenCorrect() {
        var action=agent.chooseAction(StateGrid.of(2,1), PROB_RANDOM_DUMMY);
        Assertions.assertEquals(ActionGrid.N, action);
    }


    @Test
    void givenNotFitted_whenRead_thenCorrect() {
        double value = agent.read(STATE00);
        Assertions.assertEquals(0.0, value);
    }

    @Test
    void givenFittedNoStateFuture_whenRead_thenCorrect() {
        double expected = 1.0;
        var msr = MultiStepResultGrid.builder()
                .state(STATE00)
                .action(ActionGrid.E)
                .sumRewards(expected)
                .stateFuture(Optional.empty())
                .build();
        fitManyTimes(msr);
        double value = agent.read(STATE00);
        Assertions.assertEquals(expected, value, TOL);
    }

    @Test
    void givenFittedStateFuture_whenRead_thenCorrect() {
        var msr10 = MultiStepResultGrid.builder()
                .state(STATE10)
                .action(ActionGrid.E)
                .sumRewards(1.0)
                .stateFuture(Optional.empty())
                .build();
        fitManyTimes(msr10);
        var msr00 = MultiStepResultGrid.builder()
                .state(STATE00)
                .action(ActionGrid.E)
                .sumRewards(1.0)
                .stateFuture(Optional.of(STATE10))
                .build();
        fitManyTimes(msr00);
        double value = agent.read(STATE00);
        Assertions.assertEquals(1+1, value, TOL);
    }


    private void fitManyTimes(MultiStepResultGrid msr) {
        for (int i = 0; i < N_FITS; i++) {
            agent.fit(msr, LEARNING_RATE);
        }
    }


}
