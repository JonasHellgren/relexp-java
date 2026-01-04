package chapters.ch14;

import chapters.ch14.plotting.MeasuresCombLP;
import chapters.ch14.plotting.MeasuresCombLPEnum;
import chapters.ch14.plotting.Recorder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class TestRecorder {
    private Recorder recorder;

    @BeforeEach
    public void setup() {
        recorder = Recorder.empty();
    }

    @Test
    public void testAdd() {
        var measures =  MeasuresCombLP.empty();
        measures.addReward(1.0);
        measures.addReward(2.0);
        recorder.add(measures);
        Assertions.assertEquals(2, measures.size());
        Assertions.assertEquals(3.0, recorder.trajectory(MeasuresCombLPEnum.SUM_REWARDS).get(0), 0.0001);
    }

    @Test
    public void testTrajectory() {
        var measures1 =  MeasuresCombLP.empty();
        measures1.addReward(1.0);
        recorder.add(measures1);

        var measures2 =  MeasuresCombLP.empty();
        measures2.addReward(2.0);
        recorder.add(measures2);

        List<Double> trajectory = recorder.trajectory(MeasuresCombLPEnum.SUM_REWARDS);
        Assertions.assertEquals(2, trajectory.size());
        Assertions.assertEquals(1.0, trajectory.get(0), 0.0001);
        Assertions.assertEquals(2.0, trajectory.get(1), 0.0001);
    }



}
