package chapters.ch13.domain.searcher.node;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the statistics of a node in the Monte Carlo Tree Search (MCTS) algorithm.
 * This class stores the count and value of the node, and provides methods for updating these values.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Statistics {

    public static final double LEARNING_RATE = 1.0;
    public static final double K = 100000.0;
    private int count;
    private double value;

    public static Statistics initial() {
        return new Statistics(0, 0.0);
    }

    public void update(double singleReturn) {
        update(LEARNING_RATE, singleReturn);
    }

    public void update(double learningRate, double singleReturn) {
        this.count++;
        this.value = this.value + learningRate / this.count * (singleReturn - value);
    }

    public double value() {
        return value;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "count=" + count +
                ", value=" + value() +
                '}';
    }

}
