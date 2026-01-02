package core.nextlevelrl.neural;

import lombok.experimental.UtilityClass;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

@UtilityClass
public class MultiLayerPrinter {


    public static void printWeights(MultiLayerNetwork model) {
        int layerIndex = 0;
        for (Layer layer : model.getLayers()) {
            INDArray weights = layer.getParam("W");
            INDArray biases = layer.getParam("b");
            System.out.println("Weights for layer " + layerIndex + ":");
            System.out.println(weights);
            System.out.println("Biases for layer " + layerIndex + ":");
            System.out.println(biases);
            layerIndex++;
        }
    }

}
