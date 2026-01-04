package chapters.ch12;

import chapters.ch12.inv_pendulum.domain.agent.memory.ActionAndItsValue;
import chapters.ch12.inv_pendulum.domain.agent.memory.AgentMemory;
import chapters.ch12.inv_pendulum.domain.agent.param.AgentParameters;
import chapters.ch12.inv_pendulum.factory.AgentParametersFactory;
import chapters.ch12.inv_pendulum.factory.MockedTrainDataFactory;
import chapters.ch12.inv_pendulum.factory.TrainerParametersFactory;
import core.nextlevelrl.neural.Dl4JUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestAgentMemoryFitting {

    public static final int N_ITER = 200;

    static final double TOL = 2d;
    public static final double LR = 0.01;
    static AgentMemory memory,memoryTar;
    static AgentParameters agentParameters;

    @BeforeAll
    public static void init() {
        agentParameters = AgentParametersFactory.createForTest();
        var trainerParameters = TrainerParametersFactory.createForTest();
        memory = AgentMemory.of(agentParameters, trainerParameters);
        memoryTar = AgentMemory.of(agentParameters, trainerParameters);
        memoryTar.copy(memory);

        List<List<Double>> listOfLists = MockedTrainDataFactory.getListOfInputs();
        for (int i = 0; i < N_ITER; i++) {
            var inputData= Dl4JUtil.convertListOfLists(listOfLists);
            var  avList = MockedTrainDataFactory.getActionValueList(listOfLists);
            var outData= createOutData(inputData,avList);
            var dataSet=new DataSet(inputData,outData);
            dataSet.shuffle();
            memory.fit(dataSet, LR);
        }
    }

    /**
     * Key feature: Only update the Q-value for the action taken, use target network for prediction
     * Forward pass to get predicted Q-values. Start from predictions.
     */
    private static INDArray createOutData(INDArray inputs, List<ActionAndItsValue> avList) {
        var targetPredictions=memoryTar.predict(inputs);
        for (int i = 0; i < inputs.rows(); i++) {
            var av=avList.get(i);
            targetPredictions.putScalar(i, av.a().ordinal() , av.actionValue());
        }
        return  targetPredictions;
    }

    @Test
    void testCreate() {
        assertNotNull(memory);
        assertNotNull(memory.getModel());
    }

    @Test
    void testFit0First() {
        assertInGiveOut(List.of(0d, 0d), 1, 10);
    }


    @Test
    void testFitPosFirst() {
        assertInGiveOut(List.of(1.0d, 0d), 0, 10);
    }


    @Test
    void testNegPosFirst() {
        assertInGiveOut(List.of(-1.0d, 0d), 2, 10);
    }


    private static void assertInGiveOut(List<Double> in, int idxOut, int expected) {
        INDArray input = Dl4JUtil.convertListToOneRow(in);
        INDArray output = memory.predict(input);

        int[] setIdx = {0, 1, 2};
        int[] otherIdx = Arrays.stream(setIdx).filter(i -> i != idxOut).toArray();

        assertNotNull(output);
        assertEquals(agentParameters.nOutputs(), output.length());
        assertEquals(expected, output.getDouble(idxOut), TOL);
        assertEquals(0, output.getDouble(otherIdx[0]), TOL);
        assertEquals(0, output.getDouble(otherIdx[1]), TOL);
    }


}
