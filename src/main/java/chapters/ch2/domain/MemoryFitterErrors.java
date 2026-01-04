package chapters.ch2.domain;

import core.foundation.gadget.math.BucketFinder;
import core.foundation.gadget.training.TrainData;
import core.foundation.util.cond.Conditionals;
import lombok.extern.java.Log;

@Log
public class MemoryFitterErrors extends MemoryFitterOutput implements MemoryFitterI {

    public MemoryFitterErrors(BucketFinder bucketFinder,
                              TabularMemory memory,
                              FittingParameters parameters) {
        super(bucketFinder, memory, parameters);
    }

    public static MemoryFitterErrors of(BucketFinder bucketFinder,
                                        FittingParameters parameters) {
        return new MemoryFitterErrors(bucketFinder, TabularMemory.of(parameters), parameters);
    }


    @Override
    public void fit(TrainData data) {
        for (int i = 0; i < data.nSamples(); i++) {
            double in = data.input(i).get(0);
            double err = data.errorForSample(i);
            Conditionals.executeOneOfTwo(getBucketFinder().getBucket(in).isPresent(),
                    () -> fitMemoryFromSample(in, err),
                    () -> log.severe("Bucket not found for in: " + in));
        }
    }

    private void fitMemoryFromSample(Double in, double err) {
        int hash = getBucketFinder().getBucket(in).orElseThrow();
        TabularMemory memory = getMemory();
        memory.write(hash, memory.read(hash) + getParameters().learningRate() * err);
    }
    
}
