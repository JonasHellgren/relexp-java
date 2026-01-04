package chapters.ch14.plotting;


import core.foundation.gadget.cond.Counter;
import core.foundation.util.math.Accumulator;

public record MeasuresCombLP(
        Accumulator rewardAccumulator,
        Counter stepCounter
) {
    public static MeasuresCombLP empty() {
        return new MeasuresCombLP(Accumulator.empty(), Counter.empty());
    }

    public int size() {
        return stepCounter.getCount();
    }

    public void addReward(double reward) {
        rewardAccumulator.add(reward);
        stepCounter.increase();
    }


    public double sumRewards() {
        return rewardAccumulator.value();
    }

    public int count() {
        return stepCounter.getCount();
    }

    public double countAsDouble() {
        return  stepCounter.getCount();
    }

    @Override
    public String toString() {
        return "MeasuresCombLP{" +
                "rewardAccumulator=" + rewardAccumulator.value() +
                ", stepCounter=" + stepCounter.getCount() +
                '}';
    }

}
