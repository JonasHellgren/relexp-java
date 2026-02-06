package chapters.ch14;

import chapters.ch14.domain.settings.MemorySettings;
import chapters.ch14.implem.pong.PongSettings;
import chapters.ch14.implem.pong_memory.LongMemoryRbf;
import chapters.ch14.implem.pong_memory.StateAdapterPong;
import chapters.ch14.factory.FactoryMemorySettings;
import chapters.ch14.factory.FactoryPongSettings;
import core.foundation.gadget.training.TrainData;
import core.foundation.util.rand.RandUtil;
import org.apache.commons.math3.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLongMemoryRbf {

    public static final int N_FITS = 1000;
    public static final double TOL_ONE = 0.001;
    public static final int TOL_MANY = 3;
    public static final int N_EVALS = 100;
    public static final int N_ITERATIONS_MANY = 10_000;
    PongSettings pongSetting;
    LongMemoryRbf memory;
    MemorySettings memorySettings;

    @BeforeEach
    void init() {

        pongSetting = FactoryPongSettings.create();
        memorySettings = FactoryMemorySettings.forTest();
        memory = LongMemoryRbf.of(memorySettings, pongSetting);
    }

    @Test
    void testCreateMemory() {
        var rb = memory.getMemory();
        int nk = memorySettings.nKernelsPerDimension();
        assertEquals(nk * nk, rb.getWeights().size());
    }

    @Test
    void testReadAny() {
        for (int i = 0; i < N_EVALS; i++) {
            double timeHit = getTimeHitRandom();
            double deltaX = getDeltaxRandom();
            assertEquals(0.0, memory.read(timeHit, deltaX));
        }
    }

    @Test
    void testFit_oneState() {
        double timeHit = getTimeHitRandom();
        double deltaX = getDeltaxRandom();
        int vTarget = -10;
        fitMemory(memory, Pair.create(timeHit, deltaX), vTarget, N_FITS);
        assertEquals(vTarget, memory.read(timeHit, deltaX), TOL_ONE);
    }

    @Test
    void testFit_AllStatesToFunction() {
        for (int i = 0; i < N_ITERATIONS_MANY; i++) {
            double timeHit = getTimeHitRandom();
            double deltaX = getDeltaxRandom();
            double vTarget = getMockedValue(timeHit, deltaX);
            fitMemory(memory, Pair.create(timeHit, deltaX), vTarget, 1);
        }

        evalMemory(memory);
    }

    @Test
    @Disabled("time consuming")
    void testFitUsingBuffer_AllStatesToFunction() {
        int bufferSize = 10_000;
        int mbSize = 100;
        int nFits = 1000;

        var buffer = TrainData.empty();
        for (int i = 0; i < bufferSize; i++) {
            double timeHit = getTimeHitRandom();
            double deltaX = getDeltaxRandom();
            double vTarget = getMockedValue(timeHit, deltaX);
            var in = StateAdapterPong.asInput(Pair.create(timeHit, deltaX));
            buffer.addListIn(in, vTarget);
        }

        for (int i = 0; i < nFits; i++) {
            var mb = buffer.createBatch(mbSize);
            memory.fit(mb);
        }

        evalMemory(memory);
    }

    private void evalMemory(LongMemoryRbf mem) {
        for (int i = 0; i < N_EVALS; i++) {
            double timeHit = getTimeHitRandom();
            double deltaX = getDeltaxRandom();
            while (Math.abs(deltaX - timeHit) <= 0.1) {
                deltaX = getDeltaxRandom();
            }
            double vTarget = getMockedValue(timeHit, deltaX);
            double read = mem.read(timeHit, deltaX);
            assertEquals(vTarget, read, TOL_MANY);
        }
    }

    private static double getMockedValue(double timeHit, double deltaX) {
        return timeHit < deltaX ? -10d : 0d;
    }

    private double getDeltaxRandom() {
        return RandUtil.getRandomDouble(0, pongSetting.xMax());
    }

    private double getTimeHitRandom() {
        return RandUtil.getRandomDouble(0, pongSetting.timeMaxHitBottom());
    }

    private static void fitMemory(LongMemoryRbf lm, Pair<Double, Double> pair, double vTarget, int nFits) {
        for (int i = 0; i < nFits; i++) {
            var data = TrainData.empty();
            var in = StateAdapterPong.asInput(pair);
            data.addListIn(in, vTarget);
            lm.fit(data);
        }
    }

}
