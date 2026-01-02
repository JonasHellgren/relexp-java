package chapters.ch10;

import chapters.ch10.bandit.domain.agent.AgentParametersBandit;
import chapters.ch10.bandit.domain.agent.MemoryBandit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

 class TestMemoryBandit {

    MemoryBandit memoryBandit;

     @BeforeEach
      void init() {
         AgentParametersBandit parameters = new AgentParametersBandit(new double[]{1.0, 2.0});
         memoryBandit = MemoryBandit.of(parameters);
      }

    @Test
     void testConstructor() {

        assertNotNull(memoryBandit);
        assertArrayEquals(new double[]{1.0, 2.0}, memoryBandit.getMemoryParameters());
    }

    @Test
     void testAdd() {
        double[] dz = new double[]{0.1, 0.2};
        memoryBandit.add(dz);
        assertArrayEquals(new double[]{1.1, 2.2}, memoryBandit.getMemoryParameters());
    }

    @Test
     void testAddInvalidLength() {
        double[] dz = new double[]{0.1, 0.2,3};
        assertThrows(IllegalArgumentException.class, () -> memoryBandit.add(dz));
    }

    @Test
     void testGetMemoryParameters() {
        double[] memoryParameters = memoryBandit.getMemoryParameters();
        assertArrayEquals(new double[]{1.0, 2.0}, memoryParameters);
    }
}
