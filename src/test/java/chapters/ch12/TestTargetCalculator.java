package chapters.ch12;

import chapters.ch12.inv_pendulum.domain.agent.core.AgentPendulum;
import chapters.ch12.inv_pendulum.domain.agent.memory.ActionAndItsValue;
import chapters.ch12.inv_pendulum.domain.environment.core.ActionPendulum;
import chapters.ch12.inv_pendulum.domain.environment.core.StatePendulum;
import chapters.ch12.inv_pendulum.domain.environment.core.StepReturnPendulum;
import chapters.ch12.inv_pendulum.domain.environment.param.PendulumParameters;
import chapters.ch12.inv_pendulum.domain.environment.startstate_supplier.StartStateSupplierEnum;
import chapters.ch12.inv_pendulum.domain.environment.startstate_supplier.StartStateSupplierI;
import chapters.ch12.inv_pendulum.domain.trainer.core.ExperiencePendulum;
import chapters.ch12.inv_pendulum.domain.trainer.core.MiniBatch;
import chapters.ch12.inv_pendulum.domain.trainer.core.TargetCalculator;
import chapters.ch12.inv_pendulum.domain.trainer.core.TrainerDependencies;
import chapters.ch12.inv_pendulum.factory.MockedTrainDataFactory;
import chapters.ch12.inv_pendulum.factory.TrainerDependenciesFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class TestTargetCalculator {
    public static final int N_ITER = 200;
    public static final int BATCH_SIZE = 10;
    public static final double VALUE = -50.0;
    public static final double TOL = 10;
    public static final int DUMMY_EI = 0;

    AgentPendulum agent;
    StartStateSupplierI startStateSupplier0;
    TargetCalculator targetCalculator;
    TrainerDependencies dependencies;

    @BeforeEach
    void init() {
        dependencies = TrainerDependenciesFactory.createForTest();
        var pendulumParameters = dependencies.environment().getParameters();
        agent = dependencies.agent();
        startStateSupplier0 = StartStateSupplierEnum.ZERO_ANGLE_ZERO_SPEED.create();
        fitAgentMemoryToZeroForAnyInput(pendulumParameters, dependencies);
        targetCalculator = TargetCalculator.of(dependencies);
    }

    private void fitAgentMemoryToZeroForAnyInput(PendulumParameters pendulumParameters, TrainerDependencies dependencies) {
        for (int i = 0; i < N_ITER; i++) {
            List<List<Double>> listOfLists = MockedTrainDataFactory.getListOfRandomInputs(
                    BATCH_SIZE,
                    pendulumParameters);
            var avList = MockedTrainDataFactory.getActionValueListWithValue(listOfLists, VALUE);
            List<StatePendulum> stateList = StatePendulum.getStatePendulumList(listOfLists);
            agent.fit(stateList, avList, dependencies.miniBatchDependantLearningRate(DUMMY_EI));
            if (i % dependencies.episodesBetweenTargetUpdates() == DUMMY_EI) {
                agent.copyParamsToTargetNet();
            }
        }
    }

    @Test
    void whenState00_thenCorrectActionValue() {
        var avList = agent.read(startStateSupplier0.getStartState());
        for (ActionAndItsValue av : avList) {
            Assertions.assertEquals(VALUE, av.actionValue(), TOL);
        }
    }

    public static final double REWARD = 100d;
    public static final StatePendulum STATE0 = StatePendulum.ofStart(VALUE, VALUE);
    static final StatePendulum STATE1 = StatePendulum.ofStart(1d, 1d);

    static final StepReturnPendulum STEP_RETURN_NOT_TERM = StepReturnPendulum.builder()
            .stateNew(STATE1).isFail(false).isTerminal(false).reward(REWARD).build();
    static final StepReturnPendulum STEP_RETURN_TERM = StepReturnPendulum.builder()
            .stateNew(STATE1).isFail(false).isTerminal(true).reward(REWARD).build();

    static final ExperiencePendulum EXPER_NOT_TERM =
            ExperiencePendulum.of(STATE0, ActionPendulum.N, STEP_RETURN_NOT_TERM);
    static final ExperiencePendulum EXPER_TERM =
            ExperiencePendulum.of(STATE0, ActionPendulum.N, STEP_RETURN_TERM);

    @Test
    void testTargetCalculator_notTerminal() {
        List<ExperiencePendulum> buffer = new ArrayList<>();
        buffer.add(EXPER_NOT_TERM);
        var mb = MiniBatch.of(buffer);
        var avList=targetCalculator.calculateTargets(mb);
        var df=dependencies.trainerParameters().discountFactor();
        Assertions.assertEquals(REWARD+df*VALUE, avList.get(DUMMY_EI).actionValue(),TOL);
    }

    @Test
    void testTargetCalculator_Terminal() {
        List<ExperiencePendulum> buffer = new ArrayList<>();
        buffer.add(EXPER_TERM);
        var mb = MiniBatch.of(buffer);
        var avList=targetCalculator.calculateTargets(mb);
        Assertions.assertEquals(REWARD, avList.get(DUMMY_EI).actionValue(),TOL);
    }



}
