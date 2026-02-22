package core.animation;

import java.util.List;

public record IntervalData(double[] cuts, double[] values) {

    public static IntervalData empty() {
        return IntervalData.of(List.of(), List.of());
    }

    public static IntervalData of(List<Double> cuts, List<Double> values) {
        return new IntervalData(cuts, values);
    }

    public IntervalData(List<Double> cuts, List<Double> values) {
        this(cuts.stream().mapToDouble(Double::doubleValue).toArray(),
                values.stream().mapToDouble(Double::doubleValue).toArray());
    }

    public IntervalData {
        if (cuts.length == 0) throw new IllegalArgumentException("cuts empty");
        if (cuts.length != values.length) throw new IllegalArgumentException("cuts/values size mismatch");
        for (int i = 1; i < cuts.length; i++) if (cuts[i] <= cuts[i - 1])
            throw new IllegalArgumentException("cuts must be strictly increasing");
        cuts = cuts.clone();
        values = values.clone();
    }

}