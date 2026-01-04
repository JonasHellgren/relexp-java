package chapters.ch12;

import chapters.ch12.inv_pendulum.domain.trainer.core.TrainerDependencies;
import chapters.ch12.inv_pendulum.domain.trainer.core.TrainerPendulum;
import chapters.ch12.inv_pendulum.factory.TrainerDependenciesFactory;
import chapters.ch12.inv_pendulum.plotting.MeasuresPendulumTrainingEnum;
import core.foundation.util.collections.MyListUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TestTrainer {

    TrainerPendulum trainer;
    TrainerDependencies dependencies;

    @BeforeEach
    void init() {
        dependencies = TrainerDependenciesFactory.createForTrainerTest();
        trainer = TrainerPendulum.of(dependencies);
    }

    @Test
    @Disabled
    void whenTrained_thenCanGoMultipleSteps() {
        trainer.train();
        System.out.println("trainer.getRecorder() = " + trainer.getRecorder());
        var nStepsTrajectory = trainer.getRecorder().trajectory(MeasuresPendulumTrainingEnum.N_STEPS);

        System.out.println("nStepsTrajectory.size() = " + nStepsTrajectory.size());
        double nStepsMax=dependencies.environment().getParameters().maxSteps();
        Assertions.assertTrue(MyListUtils.findMax(nStepsTrajectory).orElseThrow() > nStepsMax*0.9 );
    }


}
