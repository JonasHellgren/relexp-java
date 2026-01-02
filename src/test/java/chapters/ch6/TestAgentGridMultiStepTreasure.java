package chapters.ch6;

import chapters.ch4.implem.treasure.core.EnvironmentTreasure;
import chapters.ch4.implem.treasure.factory.EnvironmentParametersTreasureFactor;
import chapters.ch6.domain.agent.core.AgentGridMultiStepI;
import chapters.ch6.domain.trainer.multisteps_after_episode.MultiStepResultGrid;
import chapters.ch6.implem.factory.AgentGridMultiStepFactory;
import core.gridrl.ActionGrid;
import core.gridrl.StateGrid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TODO remove this test - not using multi step for treasure
 *
 */

public class TestAgentGridMultiStepTreasure {

    public static final StateGrid STATE00 = StateGrid.of(0, 0);
    public static final StateGrid STATE10 = StateGrid.of(1, 0);
    public static final double LEARNING_RATE = 0.1;
    public static final int N_FITS = 1000;
    public static final double TOL = 0.1;
    public static final double ONE = 1.0;

    AgentGridMultiStepI agent;
    EnvironmentTreasure environment;

    @BeforeEach
    void init() {
        agent = AgentGridMultiStepFactory.createTreasure();
        var gridParameters = EnvironmentParametersTreasureFactor.produce();
        environment = EnvironmentTreasure.of(gridParameters);
    }

    @Test
    void whenChooseAction_thenCorrect() {
        var actions = getActions();
        Assertions.assertTrue(actions.containsAll(
                List.of(ActionGrid.N, ActionGrid.E, ActionGrid.S, ActionGrid.W)));
    }

    @Test
    void givenNotFitted_whenRead_thenCorrect() {
        double value = agent.read(STATE00);
        Assertions.assertEquals(0.0, value);
    }

    @Test
    void givenFittedNoStateFuture_whenRead_thenCorrect() {
        double expected = 1.0;
        var action = ActionGrid.E;
        var msr = MultiStepResultGrid.builder()
                .state(STATE00)
                .action(action)
                .sumRewards(expected)
                .stateFuture(Optional.empty())
                .build();
        fitManyTimes(msr);
        double value = agent.read(STATE00);
        var bestAction=agent.chooseActionNoExploration(STATE00);
        Assertions.assertEquals(expected, value, TOL);
        Assertions.assertEquals(bestAction, action);
    }

    @Test
    @Disabled
    void givenFittedStateFuture_whenRead_thenCorrect() {
        var action = ActionGrid.E;
        var msr10 = MultiStepResultGrid.builder()
                .state(STATE10)
                .action(action)
                .sumRewards(1.0)
                .stateFuture(Optional.empty())
                .build();
        fitManyTimes(msr10);
        var msr00 = MultiStepResultGrid.builder()
                .state(STATE00)
                .action(action)
                .sumRewards(1.0)
                .stateFuture(Optional.of(STATE10))
                .build();
        fitManyTimes(msr00);
        double value = agent.read(STATE00);
        var bestAction=agent.chooseActionNoExploration(STATE00);
        Assertions.assertEquals(1+1, value, TOL);
        Assertions.assertEquals(bestAction, action);

    }


    List<ActionGrid> getActions() {
        List<ActionGrid> actions = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            actions.add(agent.chooseAction(STATE00, ONE));
        }
        return actions;
    }


    private void fitManyTimes(MultiStepResultGrid msr) {
        for (int i = 0; i < N_FITS; i++) {
            agent.fit(msr, LEARNING_RATE);
        }
    }

}
