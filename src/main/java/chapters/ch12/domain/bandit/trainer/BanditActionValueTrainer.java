package chapters.ch12.domain.bandit.trainer;

import chapters.ch12.plotting_bandit.BanditNeuralRecorder;
import chapters.ch12.plotting_bandit.MeasuresBanditNeural;
import core.foundation.util.cond.ConditionalsUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Log
public class BanditActionValueTrainer {

    private BanditTrainerDependencies dependencies;
    private BanditNeuralRecorder recorder;

    public static BanditActionValueTrainer create(BanditTrainerDependencies dependencies) {
        return new BanditActionValueTrainer(dependencies, BanditNeuralRecorder.empty());
    }

    public void train() {
        var memory = dependencies.memory();
        var input = Nd4j.ones(1, memory.nInputs());
        var tp = dependencies.trainerParameters();
        for (int epoch = 0; epoch < tp.nEpochs(); epoch++) {
            var dataSet = createTrainDataBatch(tp.batchSize());
            memory.fit(dataSet);
            maybeLog(epoch, input);
            var output = memory.predict(input);
            var measures = MeasuresBanditNeural.getMeasures(memory, dataSet, output);
            recorder.add(measures);
        }
    }

    public void logTime() {
        log.info("Time: " + dependencies.timer().timeInSecondsAsString());
    }

    private void maybeLog(int epoch, INDArray input) {
        ConditionalsUtil.executeIfTrue(dependencies.isTimeToLog(epoch),
                () -> log.info("Epoch "+epoch+"→ Q-values:"+dependencies.getPredicted(input)));
    }

    /**
     * Key feature: Only update the Q-value for the action taken
     */
    private DataSet createTrainDataBatch(int batchSize) {
        var inputBatch = Nd4j.ones(batchSize, dependencies.memory().nInputs());
        var qTargetBatch = getTargetBatchStartingFromPredictionsToNotAffectNonAppliedActions(
                batchSize, inputBatch);
        for (int i = 0; i < batchSize; i++) {
            int action = dependencies.getActionZeroOrOne();
            double label = dependencies.getValueOfAction(action);
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
        var memory = dependencies.memory();
        var qTargetBatch = Nd4j.zeros(batchSize, memory.nOutputs());
        qTargetBatch.assign(memory.predict(inputBatch));
        return qTargetBatch;
    }


}
