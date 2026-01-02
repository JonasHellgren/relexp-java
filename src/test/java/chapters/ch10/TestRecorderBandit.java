package chapters.ch10;

import chapters.ch10.bandit._shared.MeasuresBandit;
import chapters.ch10.bandit._shared.MeasuresBanditEnum;
import chapters.ch10.bandit.domain.trainer.RecorderBandit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestRecorderBandit {

    private RecorderBandit recorderBandit;

    @BeforeEach
     void setup() {
        recorderBandit = RecorderBandit.empty();
    }

    @Test
     void testEmptyRecorderBandit() {
        assertTrue(recorderBandit.isEmpty());
    }

    @Test
     void testAddMeasuresBandit() {
        var m1 = MeasuresBandit.builder()
                .sumRewards(10).gradLogLeft(0.1).gradLogRight(0.2)
                .build();
        recorderBandit.add(m1);
        recorderBandit.add(m1.withSumRewards(20));
        List<Double> trajG = recorderBandit.trajectory(MeasuresBanditEnum.SUMREWARDS);
        assertEquals(2, recorderBandit.size());
        assertEquals(10, trajG.get(0));
        assertEquals(20, trajG.get(1));
    }


}
