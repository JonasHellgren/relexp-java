package chapters.ch9.gradient_descent;

import core.foundation.gadget.training.Weights;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OutPutCalculator {

    PhiExtractor phiExtractor;

    public static OutPutCalculator of(PhiExtractor phiExtractor) {
        return new OutPutCalculator(phiExtractor);
    }

    /**
     * Computes the output of the linear approximator for a given input.
     *
     * @param input  the input data
     * @return the output
     */
    public double outPut(Weights weights, List<Double> input) {
        double out = 0;
        int nPhis = phiExtractor.nPhis();

        for (int idxDimension = 0; idxDimension < nPhis; idxDimension++) {
            out += phiExtractor.getPhi(input, idxDimension) * weights.get(idxDimension);
        }
        return out;
    }

}
