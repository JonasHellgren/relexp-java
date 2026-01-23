package chapters.ch10;

import chapters.ch10.cannon.domain.agent.AgentParametersCannon;
import chapters.ch10.cannon.domain.agent.MemoryCannon;
import chapters.ch10.factory.FactoryAgentParametersCannon;
import core.foundation.gadget.math.MeanAndStd;
import core.nextlevelrl.gradient.GradientMeanAndLogStd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class TestMemoryCannon {

     static final double Z_MEAN = 0.0;
     static final double MEAN = 0.0;
     static final double Z_STD = 1.0;
     static final double STD = Math.exp(Z_STD);
     public static final double TOL = 1e-9;
     private AgentParametersCannon parameters;
    private MemoryCannon memoryCannon;

    @BeforeEach
     void setup() {
        parameters= AgentParametersCannon.builder()
                .meanAndStdInit(MeanAndStd.of(MEAN, STD))
                .meanAndStdMin(MeanAndStd.of(-1.0, 0.0))
                .meanAndStdMax(MeanAndStd.of(1d, 2.0))
                .build();

        memoryCannon = MemoryCannon.of(parameters);
    }

    @Test
     void testMeanAndStdInitialization() {
        assertEquals(MEAN, memoryCannon.mean(), TOL);
        assertEquals(STD, memoryCannon.std(), TOL);
    }

    @Test
     void testAdd() {
        GradientMeanAndLogStd gradLog = GradientMeanAndLogStd.of(1.0, 0.5);
        double alphaG = 0.1;
        memoryCannon.add(gradLog, alphaG);
        assertEquals(MEAN +alphaG*gradLog.mean(), memoryCannon.mean(), TOL);
        assertEquals(Math.exp(Z_STD +alphaG*gradLog.std()), memoryCannon.std(), TOL);
    }

    @Test
     void testClip() {
        parameters= AgentParametersCannon.builder()
                .meanAndStdInit(MeanAndStd.of(2, 3))
                .meanAndStdMin(MeanAndStd.of(-1.0, 0.0))
                .meanAndStdMax(MeanAndStd.of(1d, 2.0))
                .build();
        memoryCannon = MemoryCannon.of(parameters);
        memoryCannon.clip();
        assertEquals(1.0, memoryCannon.mean(), TOL);
        assertEquals(2.0, memoryCannon.std(), TOL);
    }

    @Test
     void testClip_NoChange() {
        var parameters= FactoryAgentParametersCannon.noClipping(0.5,1.5);
        memoryCannon = MemoryCannon.of(parameters);
        memoryCannon.clip();
        assertEquals(0.5, memoryCannon.mean(), TOL);
        assertEquals(1.5, memoryCannon.std(), TOL);
    }
}


