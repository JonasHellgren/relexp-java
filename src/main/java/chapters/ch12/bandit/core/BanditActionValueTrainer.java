package chapters.ch12.bandit.core;

import chapters.ch10.bandit.domain.environment.EnvironmentParametersBandit;
import chapters.ch12.bandit.plotting.BanditNeuralRecorder;
import chapters.ch12.bandit.plotting.MeasuresBanditNeural;
import core.foundation.util.cond.ConditionalsUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import java.util.Random;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BanditActionValueTrainer {

    public static final int N_EPOCHS = 2_000;
    public static final double LEARNING_RATE = 0.01;
    public static final int BATCH_SIZE = 1;
    public static final int N_EPOCHS_BETWEEN_LOGGING = 500;

    private BanditActionValueMemory memory;
    private EnvironmentBanditWrapper environment;
    @Getter
    private BanditNeuralRecorder recorder;

    public static BanditActionValueTrainer create(EnvironmentParametersBandit params) {
        var environment = EnvironmentBanditWrapper.of(params);
        return new BanditActionValueTrainer(
                BanditActionValueMemory.create(LEARNING_RATE),
                environment,
                BanditNeuralRecorder.empty());
    }

    public void train() {
        INDArray input = Nd4j.ones(1, memory.nInputs());
        for (int epoch = 0; epoch < N_EPOCHS; epoch++) {
            var dataSet = createTrainDataBatch(BATCH_SIZE);
            memory.fit(dataSet);
            maybeLog(epoch, input);
            INDArray output = memory.predict(input);
            var measures = MeasuresBanditNeural.builder()
                    .error(memory.model.score(dataSet))
                    .valueLeft(output.getDouble(0))
                    .valueRight(output.getDouble(1))
                    .build();
            recorder.add(measures);
        }
    }

    private void maybeLog(int epoch, INDArray input) {
        ConditionalsUtil.executeIfTrue(epoch % N_EPOCHS_BETWEEN_LOGGING == 0, () -> {
            INDArray output = memory.predict(input);
            System.out.printf("Epoch %d → Q-values: %s%n", epoch, output);
        });
    }

    /**
     * Key feature: Only update the Q-value for the action taken
     */
    private DataSet createTrainDataBatch(int batchSize) {
        var inputBatch = Nd4j.ones(batchSize, memory.nInputs());
        var qTargetBatch = getTargetBatchStartingFromPredictionsToNotAffectNonAppliedActions(
                batchSize, inputBatch);
        Random rand= new Random();
        for (int i = 0; i < batchSize; i++) {
            int action = rand.nextBoolean() ? 0 : 1;
            double label = getValueOfAction(action);
            qTargetBatch.putScalar(i, action, label);
        }
        return new DataSet(inputBatch, qTargetBatch);
    }

    /**
      Forward pass to get predicted Q-values. Start from predictions.
     */
    @NotNull
    private INDArray getTargetBatchStartingFromPredictionsToNotAffectNonAppliedActions(
            int batchSize, INDArray inputBatch) {
        var qTargetBatch = Nd4j.zeros(batchSize, memory.nOutputs());
        qTargetBatch.assign(memory.predict(inputBatch));
        return qTargetBatch;
    }

    private double getValueOfAction(int action) {
        return environment.step(action);
    }

}
