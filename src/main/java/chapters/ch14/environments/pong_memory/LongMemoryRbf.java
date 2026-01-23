package chapters.ch14.environments.pong_memory;

import chapters.ch14.domain.long_memory.LongMemory;
import chapters.ch14.domain.settings.MemorySettings;
import chapters.ch14.environments.pong.PongSettings;
import chapters.ch14.environments.pong.StateLongPong;
import chapters.ch9.radial_basis_old.Kernel;
import chapters.ch9.radial_basis_old.Kernels;
import chapters.ch9.radial_basis_old.RbfNetwork;
import core.foundation.gadget.training.TrainDataOld;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.math3.util.Pair;

/**
 * This class implements the LongMemory interface using a Radial Basis Function (RBF) network.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LongMemoryRbf implements LongMemory<StateLongPong> {

    private final RbfNetwork memory;
    private final MemorySettings memorySettings;
    private final PongSettings envSettings;

    public static LongMemoryRbf of(MemorySettings memorySettings, PongSettings envSettings) {
        var mem = createMemoryManyCenters(memorySettings, envSettings);
        return new LongMemoryRbf(mem, memorySettings, envSettings);
    }

    @Override
    public double read(StateLongPong state) {
        return memory.outPut(StateAdapterPong.asInput(state));
    }

    @Override
    public void write(StateLongPong state, double value) {
        throw new UnsupportedOperationException();
    }


    /**
     * Fits the  memory to the given training data.
     *
     * @param data the training data to fit the memory to
     */
    @Override
    public void fit(TrainDataOld data) {
        int batchSize = Math.min(data.nSamples(), memorySettings.batchSize());
        memory.fit(data, memorySettings.nEpochs(), batchSize);
    }

    /**
     * Not clean to expose but simplifies testing
     *
     * @param timeHit
     * @param deltaX
     * @return
     */
    public double read(double timeHit, double deltaX) {
        var in = StateAdapterPong.asInput(Pair.create(timeHit, deltaX));
        return memory.outPut(in);
    }

    private static RbfNetwork createMemoryManyCenters(MemorySettings memorySettings,
                                                      PongSettings envSettings) {
        var kernels = Kernels.empty();
        int nk = memorySettings.nKernelsPerDimension();
        double sigmaTimeHit = memorySettings.getSigmaTimeHit(envSettings);
        double sigmaDeltaX = memorySettings.getSigmaDeltaX(envSettings);
        var gammas = new double[]{gamma(sigmaTimeHit), gamma(sigmaDeltaX)};
        for (double tHit : envSettings.timeMaxHitSpace(nk)) {
            for (double dx : envSettings.deltaXSpace(nk)) {
                var kernel = Kernel.ofGammas(new double[]{tHit, dx}, gammas);
                kernels.addKernel(kernel);
            }
        }
        return RbfNetwork.of(kernels, memorySettings.learningRate());
    }

    /**
     * Sigma is like the radius of a circle, controlling the size of the kernel.
     * Gamma is like the "stickiness" of the kernel, controlling how quickly it
     * decays as you move away from the center.
     */

    static double gamma(double sigma) {
        return 1 / (2 * sigma * sigma);
    }



}
