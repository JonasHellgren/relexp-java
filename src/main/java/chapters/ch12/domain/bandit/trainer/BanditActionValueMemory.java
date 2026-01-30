package chapters.ch12.domain.bandit.trainer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BanditActionValueMemory {

    public static final int N_OUT = 2;
    public static final int N_IN = 1;

    private MultiLayerNetwork model;

    public static BanditActionValueMemory create(double learningRate) {
        return new BanditActionValueMemory(initMemory(learningRate));
    }

    public void fit(DataSet dataSet) {
        model.fit(dataSet);
    }

    public INDArray predict(INDArray input) {
        return model.output(input);
    }

    public int nInputs() { return N_IN; }

    public int nOutputs() {
        return N_OUT;
    }

    private static MultiLayerNetwork initMemory(double learningRate) {
        MultiLayerNetwork model = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                .seed(42)
                .updater(new Adam(learningRate))
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .list()
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .nIn(N_IN)   // dummy input
                        .nOut(N_OUT)  // two Q-values
                        .activation(Activation.IDENTITY)  // linear output for Q-values
                        .build())
                .build());
        model.init();
        return model;
    }

}
