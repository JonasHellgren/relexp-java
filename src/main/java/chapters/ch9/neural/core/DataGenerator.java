package chapters.ch9.neural.core;


import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class DataGenerator {
    public static INDArray[] generateData(int nSamples) {
        INDArray x = Nd4j.linspace(0, 10, nSamples).reshape(nSamples, 1);
        INDArray y = x.dup(); // Since y = x
        return new INDArray[]{x, y};
    }
}
