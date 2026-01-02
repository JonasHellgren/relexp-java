package chapters.ch10;

import chapters.ch10.bandit.domain.agent.AgentBandit;
import chapters.ch10.bandit.domain.environment.ActionBandit;
import chapters.ch10.bandit.factory.FactoryAgentParametersBandit;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAgentBandit {

    public static final int N_ITER = 100;
    public static final int MANY_ACTIONS = 80;
    public static final int FEW_ACTIONS = 20;
    AgentBandit agentLeft, agentEqual, agentRight;

    @BeforeEach
    void init() {
        agentLeft = AgentBandit.of(FactoryAgentParametersBandit.highLeftProbability());
        agentEqual = AgentBandit.of(FactoryAgentParametersBandit.equalProbability());
        agentRight = AgentBandit.of(FactoryAgentParametersBandit.highRightProbability());
    }


    @Test
    void givenLeft_thenProbsOk() {
        var probs = agentLeft.actionProbabilities();
        assertTrue(probs[0] > 0.5 && probs[1] < 0.5);
    }

    @Test
    void givenLeft_thenMostlyLeftAction() {
        var actions = getActions(agentLeft);
        assertTrue(getCountLeft(actions) > MANY_ACTIONS);
        assertTrue(getCountRight(actions) < FEW_ACTIONS);
    }

    @Test
    void givenEqual_thenSimilar() {
        var actions = getActions(agentEqual);
        assertTrue(getCountLeft(actions) < MANY_ACTIONS);
        assertTrue(getCountRight(actions) > FEW_ACTIONS);
    }

    @Test
    void givenRight_thenMostlyRightAction() {
        var actions = getActions(agentRight);
        assertTrue(getCountLeft(actions) < FEW_ACTIONS);
        assertTrue(getCountRight(actions) > MANY_ACTIONS);
    }

    @Test
    void whenUpdatingMemory_thenCorrect() {
        agentLeft.updateMemory(1, 100, new double[]{-1.0, 1.0});
        var actions = getActions(agentLeft);
        assertTrue(getCountLeft(actions) < FEW_ACTIONS);
    }

    private static long getCountLeft(List<ActionBandit> actions) {
        return actions.stream().filter(a -> a == ActionBandit.LEFT).count();
    }

    private static long getCountRight(List<ActionBandit> actions) {
        return actions.stream().filter(a -> a == ActionBandit.RIGHT).count();
    }

    @NotNull
    private List<ActionBandit> getActions(AgentBandit agent) {
        return IntStream.range(0, N_ITER)
                .mapToObj(i -> agent.chooseAction())
                .toList();
    }

}
