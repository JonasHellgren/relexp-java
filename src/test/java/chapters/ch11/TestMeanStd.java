package chapters.ch11;

import chapters.ch11.domain.agent.param.AgentParameters;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.factory.LunarAgentParamsFactory;
import chapters.ch11.factory.LunarEnvParamsFactory;
import core.foundation.gadget.math.MeanAndStd;
import core.nextlevelrl.gradient.GradientMeanAndLogStd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestMeanStd {

    public static final int GRAD_MEAN_MAX = 1;
    public static final int GRAD_STD_MAX = 1;
    LunarParameters lunarParameters;
    AgentParameters agentParameters;

    @BeforeEach
    void init() {
        lunarParameters = LunarEnvParamsFactory.produceDefault();
        agentParameters = LunarAgentParamsFactory.newDefault(lunarParameters)
                .withGradMeanMax(GRAD_MEAN_MAX).withGradStdMax(GRAD_STD_MAX);
    }

    @Test
    void testOfMethod() {
        double mean = 10.0;
        double std = 5.0;
        MeanAndStd meanStd = MeanAndStd.of(mean, std);
        assertEquals(mean, meanStd.mean());
        assertEquals(std, meanStd.std());
    }

    @Test
    void testEquality() {
        double mean = 10.0;
        double std = 5.0;
        MeanAndStd meanStd1 = new MeanAndStd(mean, std);
        MeanAndStd meanStd2 = new MeanAndStd(mean, std);
        assertEquals(meanStd1, meanStd2);
    }

    @Test
    void testCreateClipped() {
        GradientMeanAndLogStd gradMeanAndStd = GradientMeanAndLogStd.of(10.0, 20.0);
        GradientMeanAndLogStd clipped = gradMeanAndStd.clip(
                agentParameters.gradMeanMax(), agentParameters.gradStdMax());
        assertEquals(GRAD_MEAN_MAX, clipped.mean(), 0.0);
        assertEquals(GRAD_STD_MAX, clipped.std(), 0.0);
    }


}
