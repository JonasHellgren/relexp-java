package chapters.ch10;

import chapters.ch10.cannon.domain.envrionment.EnvironmentCannon;
import chapters.ch10.cannon.domain.envrionment.EnvironmentParametersCannon;
import chapters.ch10.cannon.domain.envrionment.StepReturnCannon;
import chapters.ch10.factory.FactoryEnvironmentParametersCannon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

 class TestEnvironmentCannon {

     public static final double TOL = 1e-5;
     EnvironmentCannon environment;
    EnvironmentParametersCannon parameters;

    @BeforeEach
    void init() {
        parameters = FactoryEnvironmentParametersCannon.createDefault();
        environment = EnvironmentCannon.of(parameters);
    }


    @Test
    void given45_thenCorrect() {
        double action = Math.PI / 4; // 45 degrees
        StepReturnCannon sr = environment.step(action);
        assertTrue(sr.reward()<0);
        assertEquals(1000d, sr.distance(),500);
        assertEquals(action, sr.angle(), TOL);

    }

    @Test
    void given0_thenCorrect() {
        double action = Math.PI / 8; // 2 degrees
        StepReturnCannon sr = environment.step(action);
        assertTrue(sr.reward()<0);
        assertEquals(1000d, sr.distance(),500);
        assertEquals(action, sr.angle(), TOL);
    }

}
