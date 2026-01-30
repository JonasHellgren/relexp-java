package chapters.ch12;

import chapters.ch12.domain.inv_pendulum.agent.memory.PendulumScaler;
import chapters.ch12.domain.inv_pendulum.agent.param.AgentParameters;
import chapters.ch12.factory.AgentParametersFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestPendulumScaler {

    AgentParameters params;
    PendulumScaler scaler;
    
     @BeforeEach
      void init() {
         params = AgentParametersFactory.createForTest();
         scaler = PendulumScaler.of(params);

     }
    
    @Test
     void testNormaliseAngle() {
        double angle = 0.5;
        double expected = 0.5;
        assertEquals(expected, scaler.normaliseAngle(angle), 1e-6);
    }

    @Test
     void testNormaliseAngularSpeed() {
        double angularSpeed = 0.5;
        double expected = angularSpeed / 2.0;
        assertEquals(expected, scaler.normaliseAngularSpeed(angularSpeed), 1e-6);
    }

    
}
