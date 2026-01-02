package chapters.ch9.gradient_descent;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;

/**
 * This class represents a PhiExtractor, which is used to extract features from input data.
 * It holds a list of functions that can be applied to the input data to extract the features.
 */
@AllArgsConstructor
public class PhiExtractor {

    public List<ToDoubleFunction<List<Double>>> functionList;


    public static PhiExtractor empty() {
        return new PhiExtractor(new ArrayList<>());
    }

    public static PhiExtractor of(List<ToDoubleFunction<List<Double>>> functionList) {
        return new PhiExtractor(functionList);
    }

    public int nPhis() {
        return functionList.size();
    }

    public double getPhi(List<Double> input, int idxDimension) {
        Preconditions.checkArgument(idxDimension >= 0 && idxDimension < functionList.size(),
                "Non valid dimension index");
        var function = functionList.get(idxDimension);
        return function.applyAsDouble(input);
    }
}
