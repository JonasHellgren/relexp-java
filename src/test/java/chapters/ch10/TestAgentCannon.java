package chapters.ch10;

import chapters.ch10.cannon.domain.agent.AgentCannon;
import chapters.ch10.cannon.factory.FactoryAgentParametersCannon;
import core.foundation.util.math.MathUtil;
import core.nextlevelrl.gradient.GradientMeanAndLogStd;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.stream.IntStream;

public class TestAgentCannon {


    public static final int STD = 1;
    public static final int MEAN = 0;
    public static final double TOL = 1e-5;
    AgentCannon agent;

    @BeforeEach
    void init() {
        var parameters = FactoryAgentParametersCannon.noClipping(MEAN, STD);
        agent = AgentCannon.of(parameters);
    }

    @Test
    void givenAgent_thenCorrectInitMeanAndStd() {

        Assertions.assertEquals(MEAN, agent.meanAndStd().mean());
        Assertions.assertEquals(STD, agent.meanAndStd().std());
    }

    /**
     * The sigma rule (also known as the 68-95-99.7 rule) states that:
     * About 68% of the data points fall within 1 standard deviation (σ) of the mean.
     * About 95% of the data points fall within 2 standard deviations (2σ) of the mean.
     * About 99.7% of the data points fall within 3 standard deviations (3σ) of the mean.
     */

    @Test
    void correctAction() {

        IntStream.range(MEAN, 10).forEach(i -> {
            double a = agent.chooseAction();
            Assertions.assertTrue(MathUtil.isInLimits(a, -3 * STD, 3 * STD));
        });
    }


    @Test
    void whenUpdating_thenCorrect() {
        var gradLog = GradientMeanAndLogStd.of(1.0, 1.0);
        double learningRate = 0.1;
        double returnAtT = 1.0;
        agent.updateMemory(learningRate, returnAtT, gradLog);
        var mAndS = agent.meanAndStd();
        Assertions.assertEquals(MEAN + learningRate * gradLog.mean(), mAndS.mean());
        double zStdNew = Math.log(STD) + learningRate * gradLog.std();
        Assertions.assertEquals(zStdNew, Math.log(mAndS.std()), TOL);
        Assertions.assertTrue(mAndS.std() > STD);
    }

}
