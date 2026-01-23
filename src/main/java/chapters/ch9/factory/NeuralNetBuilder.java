package chapters.ch9.factory;

import lombok.experimental.UtilityClass;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.*;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;


/**
 * Utility class for building neural networks.
 */

@UtilityClass
public class NeuralNetBuilder {

    public static final int N_NEURONS = 1;
    public static final double LEARNING_RATE = 0.01;
    public static final int N_IN = 1;
    public static final int N_OUT = 1;
    public static final IWeightInit WEIGHT_INIT_WORK = new WeightInitConstant(0.1);   //will work
    public static final IWeightInit WEIGHT_INIT_FAIL = new WeightInitConstant(-0.1);  //will fail

    public static MultiLayerNetwork buildWillWork() {
        return build(WEIGHT_INIT_WORK);
    }

    public static MultiLayerNetwork buildWillFail() {
        return build(WEIGHT_INIT_FAIL);
    }

    public static MultiLayerNetwork build(IWeightInit weightInit) {
        var config = new NeuralNetConfiguration.Builder()
                .updater(new Adam(LEARNING_RATE))
                .list()
                .layer(new DenseLayer.Builder().nIn(N_IN).nOut(N_NEURONS)
                        .weightInit(weightInit)
                        .activation(Activation.RELU).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .weightInit(weightInit)
                        .activation(Activation.IDENTITY)
                        .nIn(N_NEURONS).nOut(N_OUT).build())
                .build();
        var model = new MultiLayerNetwork(config);
        model.init();
        return model;
    }
}
