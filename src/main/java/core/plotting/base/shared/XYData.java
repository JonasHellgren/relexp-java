package core.plotting.base.shared;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public record XYData(List<Double> x, List<Double> y) {

    public static XYData of(List<Double> x, List<Double> y) {
        Preconditions.checkArgument(x.size() == y.size(),
                "x and y should have same length, xSize=%s, ySize=%s", x.size(), y.size());
        return new XYData(x, y);
    }

    public static XYData empty() {
        return new XYData(new ArrayList<>(), new ArrayList<>());
    }

    public boolean isEmpty() {
        return x.isEmpty() && y.isEmpty();
    }

    @Override
    public String toString() {
        return "XYData{" +
                "x=" + x + System.lineSeparator() +
                ", y=" + y + System.lineSeparator() +
                '}'+ System.lineSeparator();
    }

}