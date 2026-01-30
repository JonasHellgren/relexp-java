package chapters.ch12;

import chapters.ch12.domain.inv_pendulum.environment.core.ActionPendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.EnvironmentPendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.StatePendulum;
import chapters.ch12.domain.inv_pendulum.environment.startstate_supplier.StartStateSupplierEnum;
import chapters.ch12.factory.PendulumParametersFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestEnvironmentPendulum {

     static final double TOL = 1e-6;
     public static final StatePendulum START_STATE_UPRIGHT =
             StartStateSupplierEnum.ZERO_ANGLE_ZERO_SPEED.create().getStartState();
     EnvironmentPendulum environment;

     @BeforeEach
      void init() {
         var parameters = PendulumParametersFactory.createForTest();
         environment = EnvironmentPendulum.of(parameters);
      }

    @Test
     void testStep_ZeroAngleZeroSpeedZeroTorque() {
        var result = environment.step(START_STATE_UPRIGHT, ActionPendulum.N);

        assertFalse(result.isTerminal());
        assertFalse(result.isFail());
        assertEquals(0.0, result.stateNew().angle(), TOL);
        assertEquals(0.0, result.stateNew().angularSpeed(), TOL);
        assertEquals(0.0,result.reward(), TOL);
    }

     @Test
     void testStep_ZeroAngleZeroSpeedClockwiseTorque() {
         var result = environment.step(START_STATE_UPRIGHT, ActionPendulum.CW);
         assertFalse(result.isTerminal());
         assertFalse(result.isFail());
         assertTrue(result.stateNew().angle()>0);
         assertTrue(result.stateNew().angularSpeed()>0);
         assertTrue(result.reward()<0);
     }

     @Test
     void testStep_ZeroAngleZeroSpeedCounterClockwiseTorque() {
         var result = environment.step(START_STATE_UPRIGHT, ActionPendulum.CCW);

         assertFalse(result.isTerminal());
         assertFalse(result.isFail());
         assertTrue(result.stateNew().angle()<0);
         assertTrue(result.stateNew().angularSpeed()<0);
         assertTrue(result.reward()<0);
     }

      @Test
       void givenLargeAngle_whenClockwiseTorque_thenFail() {
         var pp=environment.getParameters();
          var state = StatePendulum.ofStart(pp.angleMax()-TOL, 0.0);
          var result = environment.step(state, ActionPendulum.CW);

          System.out.println("result = " + result);

          Assertions.assertTrue(result.isFail());
          Assertions.assertTrue(result.isTerminal());
          Assertions.assertTrue(result.stateNew().angle()>pp.angleMax());
          Assertions.assertTrue(result.reward()<pp.rewardFail());

      }

 }
