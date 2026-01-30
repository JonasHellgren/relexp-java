package chapters.ch12.domain.inv_pendulum.agent.core;

import chapters.ch12.domain.inv_pendulum.agent.memory.ActionAndItsValue;
import chapters.ch12.domain.inv_pendulum.agent.memory.AgentMemory;
import chapters.ch12.domain.inv_pendulum.agent.memory.PendulumScaler;
import chapters.ch12.domain.inv_pendulum.agent.param.AgentParameters;
import chapters.ch12.domain.inv_pendulum.environment.core.ActionPendulum;
import chapters.ch12.domain.inv_pendulum.environment.core.StatePendulum;
import chapters.ch12.domain.inv_pendulum.trainer.param.TrainerParameters;
import com.google.common.base.Preconditions;
import core.foundation.util.rand.RandUtil;
import core.nextlevelrl.neural.Dl4JUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Represents an agent that interacts with the inverted pendulum environment.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AgentPendulum {

    private AgentParameters agentParameters;
    private TrainerParameters trainerParameters;
    private AgentMemory memory, memoryTar;
    private PendulumScaler scaler;

    public static AgentPendulum of(AgentParameters agentParameters,
                                   TrainerParameters trainerParameters) {
        var memory = AgentMemory.of(agentParameters, trainerParameters);
        var memoryTar = AgentMemory.of(agentParameters, trainerParameters);
        memoryTar.copy(memory);
        return new AgentPendulum(
                agentParameters,
                trainerParameters,
                memory,
                memoryTar,
                PendulumScaler.of(agentParameters));
    }

    /**
     * Chooses an action for the given state, with a probability of random exploration.
     *
     * @param state      the current state
     * @param probRandom the probability of random exploration
     * @return the chosen action
     */
    public ActionPendulum chooseAction(StatePendulum state, double probRandom) {
        return (RandUtil.randomNumberBetweenZeroAndOne() < probRandom)
                ? ActionPendulum.random()
                : chooseActionNoExploration(state);
    }

    /**
     * Chooses an action for the given state, without random exploration.
     *
     * @param state the current state
     * @return the chosen action
     */
    public ActionPendulum chooseActionNoExploration(StatePendulum state) {
        var bestAV = read(state).stream()
                .max(Comparator.comparingDouble(ActionAndItsValue::actionValue))
                .orElseThrow();
        return bestAV.a();
    }

    /**
     * Updates the agent's memory with the given states and actions.
     *
     * @param states            the list of states
     * @param actionsAndTargets the list of actions and their corresponding Q-values
     * @param lr                the learning rate
     */
    public void fit(List<StatePendulum> states, List<ActionAndItsValue> actionsAndTargets, double lr) {
        int sizeList = states.size();
        Preconditions.checkArgument(sizeList == actionsAndTargets.size(), "states and actions must have the same size");
        Preconditions.checkArgument(sizeList > 0, "not enough data to fit");
        List<List<Double>> listOfNormalisedStateValues = states.stream()
                .map(s -> stateAsNormalizedList(s))
                .toList();
        var inputData = Dl4JUtil.convertListOfLists(listOfNormalisedStateValues);
        var outData = createOutData(inputData, actionsAndTargets);
        var dataSet = new DataSet(inputData, outData);
        dataSet.shuffle();
        memory.fit(dataSet,lr);
    }

    /**
     * Copies the parameters from the agent's memory to the target memory.
     */
    public void copyParamsToTargetNet() {
        memoryTar.copy(memory);
    }

    /**
     * Copies the parameters from the target memory to the agent's memory.
     */
    public void copyParamsFromTargetNet() {
        memory.copy(memoryTar);
    }

    /**
     * Reads the Q-values for the given state.
     *
     * @param state the current state
     * @return the list of Q-values for each action
     */
    public List<ActionAndItsValue> read(StatePendulum state) {
        return getActionAndItsValues(state, memory);
    }

    /**
     * Reads the Q-values for the given state.
     *
     * @param state the current state
     * @return the list of Q-values for each action
     */
    public List<ActionAndItsValue> readTargetMemory(StatePendulum state) {
        return getActionAndItsValues(state, memoryTar);
    }


    public double loss() {
        return memory.loss();
    }

    @NotNull
    private List<ActionAndItsValue> getActionAndItsValues(StatePendulum state, AgentMemory agentMemory) {
        INDArray outNet = agentMemory.predict(Dl4JUtil.convertListToOneRow(stateAsNormalizedList(state)));
        var actionList = ActionPendulum.getAllActions();
        return IntStream.range(0, actionList.size())
                .mapToObj(i -> ActionAndItsValue.of(
                       // actionList.get(i), scaler.denormalizeValue(outNet.getDouble(i))))
                        actionList.get(i),
                        outNet.getDouble(i)))
                .toList();
    }

    /**
     * Key feature: Only update the Q-value for the action taken, use target network for prediction
     * Forward pass to get predicted Q-values. Start from predictions.
     */
    private INDArray createOutData(INDArray inputs, List<ActionAndItsValue> avList) {
        var targetPredictions = memoryTar.predict(inputs);
        IntStream.range(0, inputs.rows())
                .forEach(i -> targetPredictions.putScalar(i,
                        avList.get(i).a().ordinal(),
                        //scaler.normalizeValue(avList.get(i).actionValue())));
                        avList.get(i).actionValue()));
        return targetPredictions;
    }

    private List<Double> stateAsNormalizedList(StatePendulum s) {
        return List.of(
                scaler.normaliseAngle(s.angle()), scaler.normaliseAngularSpeed(s.angularSpeed()));
    }


}
