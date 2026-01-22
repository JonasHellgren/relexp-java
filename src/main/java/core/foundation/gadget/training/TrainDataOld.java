package core.foundation.gadget.training;

import core.foundation.util.rand.RandUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of training data.
 * Each example in the training data consists of an input, an error OR an output.
 * Error and output can't both be present at the same time.
 */
public record TrainDataOld(
        List<List<Double>> inputs,
        List<Double> errors,
        List<Double> outputs
) {


    public static TrainDataOld emptyFromOutputs() {
        return new TrainDataOld(new ArrayList<>(), null, new ArrayList<>());
    }

    public static TrainDataOld emptyFromErrors() {
        return new TrainDataOld(new ArrayList<>(), new ArrayList<>(), null);
    }

    public static TrainDataOld ofErrors(List<List<Double>> inputs, List<Double> errors) {
        return new TrainDataOld(inputs, errors, null);
    }

    public static TrainDataOld ofOutputs(List<List<Double>> inputs, List<Double> outputs) {
        return new TrainDataOld(inputs, null, outputs);
    }

    public void addIAndOut(List<Double> input, double output) {
        inputs.add(input);
        outputs.add(output);
    }

    public void addInAndError(List<Double> input, double error) {
        inputs.add(input);
        errors.add(error);
    }

    public void clear() {
        inputs.clear();
        if (isErrors()) {
            errors.clear();
        } else {
            outputs.clear();
        }
    }

    public TrainDataOld createBatch(int len) {
        var randomIndices = RandUtil.randomIndices(len, nSamples());
        var newInputs = new ArrayList<List<Double>>();
        if (isErrors()) {
            var newErrors = new ArrayList<Double>();
            for (int i : randomIndices) {
                newInputs.add(inputs.get(i));
                newErrors.add(errors.get(i));
            }
            return TrainDataOld.ofErrors(newInputs, newErrors);
        } else {
            var newOutputs = new ArrayList<Double>();
            for (int i : randomIndices) {
                newInputs.add(inputs.get(i));
                newOutputs.add(outputs.get(i));
            }
            return TrainDataOld.ofOutputs(newInputs, newOutputs);
        }
    }

    public int nSamples() {
        return inputs.size();
    }

    public boolean isErrors() {
        return errors != null;
    }

    public boolean isOutput() {
        return !isErrors();
    }

    public List<Double> input(int i) {
        return inputs.get(i);
    }

    public double errorForSample(int i) {
        return errors.get(i);
    }


    public double output(int i) {
        return outputs.get(i);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        for (int i = 0; i < nSamples(); i++) {
            if (isErrors()) {
                sb.append(input(i)).append(" ").append(errors.get(i)).append("\n");
            } else {
                sb.append(input(i)).append(" ").append(output(i)).append("\n");
            }
        }
        return sb.toString();


    }

}
