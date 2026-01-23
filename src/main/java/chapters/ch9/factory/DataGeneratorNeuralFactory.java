package chapters.ch9.factory;


import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * This class provides a static method to generate data for a neural network.
 * The generated data consists of two arrays: x and y, where y has same shape as x.
 * This is commonly used for regression problems where the input and output are the same.
 *
 * @author Your Name
 */
public class DataGeneratorNeuralFactory {
    public static INDArray[] generateData(int nSamples, int lower, int upper) {
        INDArray x = Nd4j.linspace(lower, upper, nSamples).reshape(nSamples, 1);
        INDArray y = x.dup(); // Since y = x
        return new INDArray[]{x, y};
    }
}
