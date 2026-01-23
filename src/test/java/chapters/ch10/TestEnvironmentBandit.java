package chapters.ch10;

import chapters.ch10.bandit.domain.environment.ActionBandit;
import chapters.ch10.bandit.domain.environment.EnvironmentBandit;
import chapters.ch10.bandit.domain.environment.StepReturnBandit;
import chapters.ch10.factory.FactoryEnvironmentParametersBandit;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestEnvironmentBandit {
    public static final int N_ITER = 100;
    public static final double GOOD_SUM = 80;

    EnvironmentBandit environmentLeftBetter, environmentRightBetter;

    @BeforeEach
    void init() {
        environmentLeftBetter = EnvironmentBandit.of(
                FactoryEnvironmentParametersBandit.veryHighLeftProbability());
        environmentRightBetter = EnvironmentBandit.of(
                FactoryEnvironmentParametersBandit.veryHighRightProbability());
    }


    @Test
    void givenLeftBetter_thenActionLeftMoreReward() {
        var srList = getStepReturns(environmentLeftBetter, ActionBandit.LEFT);
        double sumReward = srList.stream().mapToDouble(StepReturnBandit::reward).sum();
        assertTrue(sumReward > GOOD_SUM);
    }

    @Test
    void givenLeftBetter_thenActionRightLittleReward() {
        var srList = getStepReturns(environmentLeftBetter, ActionBandit.RIGHT);
        double sumReward = srList.stream().mapToDouble(StepReturnBandit::reward).sum();
        assertFalse(sumReward > GOOD_SUM);
    }


    @Test
    void givenRightBetter_thenActionLeftLittleReward() {
        var srList = getStepReturns(environmentRightBetter, ActionBandit.LEFT);
        double sumReward = srList.stream().mapToDouble(StepReturnBandit::reward).sum();
        assertFalse(sumReward > GOOD_SUM);
    }

    @Test
    void givenRightBetter_thenActionRightMuchReward() {
        var srList = getStepReturns(environmentRightBetter, ActionBandit.RIGHT);
        double sumReward = srList.stream().mapToDouble(StepReturnBandit::reward).sum();
        System.out.println("sumReward = " + sumReward);
        assertTrue(sumReward > GOOD_SUM);
    }


    @NotNull
    private List<StepReturnBandit> getStepReturns(EnvironmentBandit environment, ActionBandit action) {
        return IntStream.range(0, N_ITER)
                .mapToObj(i -> environment.step(action))
                .toList();
    }


}
