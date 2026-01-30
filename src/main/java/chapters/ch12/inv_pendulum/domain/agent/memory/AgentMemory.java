package chapters.ch12.inv_pendulum.domain.agent.memory;

import chapters.ch12.inv_pendulum.domain.agent.param.AgentParameters;
import chapters.ch12.inv_pendulum.domain.trainer.param.TrainerParameters;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

/**
 * Represents the memory of an agent in a reinforcement learning environment.
 * This class is responsible for creating and managing the neural network model
 * used by the agent to make predictions and learn from experience.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AgentMemory {

    private static final int SEED = 42;
    private MultiLayerNetwork model;
    private AgentParameters agentParameters;
    private TrainerParameters trainerParameters;

    public static AgentMemory of(AgentParameters ap, TrainerParameters tp) {
        return new AgentMemory(
                initModel(ap, tp),
                ap,
                tp);
    }

    /**
     * Updates the model's parameters based on the given dataset and learning rate.
     *
     * @param dataSet the dataset to train on
     * @param lr      the learning rate
     */
    public void fit(DataSet dataSet, double lr) {
        model.setLearningRate(lr);
        model.fit(dataSet);
    }

    public double loss() {
        return model.score();
    }

    public void copy(AgentMemory other) {
        model.setParameters(other.getModel().params());
    }

    /**
     * Makes a prediction using the given input.
     *
     * @param input the input to make a prediction on
     * @return the predicted output
     */
    public INDArray predict(INDArray input) {
        return model.output(input);
    }

    private static MultiLayerNetwork initModel(AgentParameters ap, TrainerParameters tp) {
        NeuralNetConfiguration.ListBuilder confBuilder = getConfBuilder(ap, tp);
        addInputLayer(ap, tp, confBuilder);
        addHiddenLayers(ap, tp, confBuilder);
        addOutPutLayer(ap, tp, confBuilder);
        return buildNetwork(confBuilder);
    }

    private static NeuralNetConfiguration.ListBuilder getConfBuilder(AgentParameters ap, TrainerParameters tp) {
        return new NeuralNetConfiguration.Builder()
                .seed(SEED)
                .updater(new Adam(tp.learningRateStartEnd().getFirst()))
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .list();
    }

    private static MultiLayerNetwork buildNetwork(NeuralNetConfiguration.ListBuilder confBuilder) {
        MultiLayerConfiguration conf = confBuilder.build();
        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();
        return net;
    }

    private static void addInputLayer(AgentParameters ap, TrainerParameters tp, NeuralNetConfiguration.ListBuilder confBuilder) {
        confBuilder.layer(new DenseLayer.Builder()
                .nIn(ap.nInputs()).nOut(ap.nHiddenUnits())
                .activation(Activation.RELU)
                .build());
    }

    private static void addHiddenLayers(AgentParameters ap, TrainerParameters tp, NeuralNetConfiguration.ListBuilder confBuilder) {
        double nHiddenLayers = 0d; // ap.nHiddenLayers();
        for (int i = 1; i <= nHiddenLayers; i++) {
            confBuilder.layer(new DenseLayer.Builder()
                    .nIn(ap.nHiddenUnits()).nOut(ap.nHiddenUnits())
                    .activation(Activation.RELU)
                    .build());
        }
    }

    private static void addOutPutLayer(AgentParameters ap, TrainerParameters tp, NeuralNetConfiguration.ListBuilder confBuilder) {
        confBuilder.layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                .activation(Activation.IDENTITY)
                .nIn(ap.nHiddenUnits()).nOut(ap.nOutputs()).build());
    }

}



