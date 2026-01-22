package core.foundation.util.collections;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class SetCreatorUtil {


    /**
     * Creates a set of doubles starting from the specified start value, incrementing by the specified step,
     * and ending at or below the specified end value.
     *
     * @param start the starting value of the set
     * @param end   the ending value of the set (inclusive)
     * @param step  the increment between each element
     * @return a set of doubles starting from the specified start value, incrementing by the specified step,
     * and ending at or below the specified end value
     */
    public static Set<Double> createFromStartToEndWithStep(double start, double end, double step) {
        return new HashSet<>(ListCreatorUtil.createFromStartToEndWithStep(start, end, step));
    }

    /**
     * Creates a set of doubles starting from the specified start value, incrementing by the specified step,
     * and containing the specified number of elements.
     *
     * @param start    the starting value of the set
     * @param step     the increment between each element
     * @param nItems   the number of elements to generate
     * @return a set of doubles starting from the specified start value, incrementing by the specified step,
     * and containing the specified number of elements
     */
    public static Set<Double> createFromStartWithStepWithNofItems(double start, double step, int nItems) {
        return new HashSet<>(ListCreatorUtil.createFromStartWithStepWithNofItems(start, step, nItems));
    }

    /**
     * Creates a set of integers starting from the specified start value, incrementing by the specified step,
     * and containing the specified number of elements.
     *
     * @param start    the starting value of the set
     * @param step     the increment between each element
     * @param nItems   the number of elements to generate
     * @return a set of integers starting from the specified start value, incrementing by the specified step,
     * and containing the specified number of elements
     */
    public static Set<Integer> createFromStartWithStepWithNofItems(int start, int step, int nItems) {
        // Use ListCreator to generate the list of integers and then convert it to a set
        return new HashSet<>(ListCreatorUtil.createFromStartWithStepWithNofItems(start, step, nItems));
    }

    /**
     * Creates a set of doubles starting from the specified start value, ending at the specified end value,
     * and containing the specified number of elements.
     *
     * @param start    the starting value of the set
     * @param end      the ending value of the set (inclusive)
     * @param nItems   the number of elements to generate
     * @return a set of doubles starting from the specified start value, ending at the specified end value,
     * and containing the specified number of elements
     */
    public static Set<Double> createFromStartToEndWithNofTimes(double start, double end, int nItems) {
        // Use ListCreator to generate the list of doubles and then convert it to a set
        return new HashSet<>(ListCreatorUtil.createFromStartToEndWithNofItems(start, end, nItems));
    }

}
