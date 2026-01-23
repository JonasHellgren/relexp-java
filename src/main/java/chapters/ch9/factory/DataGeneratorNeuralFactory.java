package chapters.ch9.factory;


import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class DataGeneratorNeuralFactory {
    public static INDArray[] generateData(int nSamples, int lower, int upper) {
        INDArray x = Nd4j.linspace(lower, upper, nSamples).reshape(nSamples, 1);
        INDArray y = x.dup(); // Since y = x
        return new INDArray[]{x, y};
    }
}
