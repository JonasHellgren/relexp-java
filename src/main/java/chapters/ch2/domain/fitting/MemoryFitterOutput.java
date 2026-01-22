package chapters.ch2.domain.fitting;

import com.google.common.base.Preconditions;
import core.foundation.gadget.math.BucketFinder;
import core.foundation.gadget.training.TrainDataInOut;
import core.foundation.util.cond.ConditionalsUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;

@AllArgsConstructor
@Log
@Getter
public class MemoryFitterOutput implements MemoryFitterI {

    private final BucketFinder bucketFinder;
    private final TabularMemory memory;
    private final FittingParameters parameters;


    public static MemoryFitterOutput of(BucketFinder bucketFinder, FittingParameters parameters) {
        return new MemoryFitterOutput(bucketFinder, TabularMemory.of(parameters), parameters);
    }


    @Override
    public void fit(TrainDataInOut data) {

        for (int i = 0; i < data.nSamples(); i++) {
            double in = data.input(i).get(0);
            double out = data.output(i);
            ConditionalsUtil.executeOneOfTwo(bucketFinder.getBucket(in).isPresent(),
                    () -> fitMemoryFromSample(in, out),
                    () -> log.severe("Bucket not found for in: " + in));
        }
    }

    private void fitMemoryFromSample(Double in, double out) {
        int hash = bucketFinder.getBucket(in).orElseThrow();
        var error = out - memory.read(hash);
        memory.write(hash, memory.read(hash) + parameters.learningRate() * error);
    }

    public double read(double in) {
        Preconditions.checkArgument(bucketFinder.getBucket(in).isPresent(),
                "Bucket not found for in: " + in);
        return memory.read(bucketFinder.getBucket(in).orElseThrow());
    }
}
