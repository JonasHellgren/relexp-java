package chapters.ch12;

import chapters.ch12.domain.inv_pendulum.environment.core.ActionPendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.StatePendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.StepReturnPendulum;
import chapters.ch12.domain.inv_pendulum.trainer.core.ExperiencePendulum;
import chapters.ch12.domain.inv_pendulum.trainer.core.MiniBatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

 class TestMiniBatch {

    static final StepReturnPendulum STEP_RETURN = StepReturnPendulum.builder()
            .stateNew(StatePendulum.ofStart(0.1d, 0d)).isFail(false).isTerminal(false).reward(0d).build();
     static final StatePendulum STATE0 = StatePendulum.ofStart(0d, 0d);
    static final ExperiencePendulum EXPER0 =
            ExperiencePendulum.of(STATE0, ActionPendulum.N, STEP_RETURN);
     static final StatePendulum STATE1 = StatePendulum.ofStart(1d, 1d);
    static final ExperiencePendulum EXPER1 =
            ExperiencePendulum.of(STATE1, ActionPendulum.N, STEP_RETURN);

    List<ExperiencePendulum> buffer;

     @BeforeEach
      void init() {
         buffer = new ArrayList<>();
         buffer.add(EXPER0);
         buffer.add(EXPER1);
     }

    @Test
     void testSize() {
         var miniBatch = MiniBatch.of(buffer);
        assertEquals(2, miniBatch.size());
    }

    @Test
     void testGetStateList() {

        MiniBatch miniBatch = MiniBatch.of(buffer);
        List<StatePendulum> stateList = miniBatch.getStateList();
        assertEquals(2, stateList.size());
        assertTrue(stateList.contains(STATE0));
        assertTrue(stateList.contains(STATE1));
        assertEquals(STATE0, stateList.get(0));
        assertEquals(STATE1, stateList.get(1));
    }

    @Test
     void testIterator() {

        MiniBatch miniBatch = MiniBatch.of(buffer);
        Iterator<ExperiencePendulum> iterator = miniBatch.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(EXPER0, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(EXPER1, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
     void testContains() {
        ExperiencePendulum exper2 =
                ExperiencePendulum.of(StatePendulum.ofStart(2d, 2d), ActionPendulum.N, STEP_RETURN);

        MiniBatch miniBatch = MiniBatch.of(buffer);
        assertTrue(miniBatch.contains(EXPER0));
        assertFalse(miniBatch.contains(exper2));
    }

    

}
