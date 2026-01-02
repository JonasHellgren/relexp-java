package chapters.ch10.bandit.domain.agent;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Represents a bandit agent's memory, which stores the policy parameters.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemoryBandit {

    private double[] z_array;

    public static MemoryBandit of(AgentParametersBandit parameters) {
       return new MemoryBandit(parameters.z_array_init());
    }

    public double[] getMemoryParameters() {
        return z_array;
    }

    /**
     * Updates the policy parameters in the memory by adding the given increments.
     *
     * @param dz the increments to add to the policy parameters
     */
    public void add(double[] dz) {
        Preconditions.checkArgument(dz.length == z_array.length,"dz.length != z_array.length");
        IntStream.range(0, z_array.length).forEach(i -> z_array[i] += dz[i]);
    }

    @Override
    public String toString() {
        return "MemoryBandit{" +
                "z_array=" + Arrays.toString(z_array) +
                '}';
    }

}
