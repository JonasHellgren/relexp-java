package chapters.ch12.domain.bandit.trainer;

import chapters.ch12.domain.bandit.environment.EnvironmentBanditWrapper;
import core.foundation.gadget.timer.CpuTimer;
import lombok.Builder;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.Random;

@Builder
public record BanditTrainerDependencies(
        BanditActionValueMemory memory,
        EnvironmentBanditWrapper environment,
        BanditTrainerParameters trainerParameters,
        CpuTimer timer,
        Random rand
) {

    public boolean isTimeToLog(int epoch) {
        return epoch % trainerParameters().nEpochsBetweenLogging() == 0;
    }


    public INDArray getPredicted(INDArray input) {
        return memory().predict(input);
    }


    public double getValueOfAction(int action) {
        return environment().step(action);
    }


    public int getActionZeroOrOne() {
        return rand.nextBoolean() ? 0 : 1;
    }

}
