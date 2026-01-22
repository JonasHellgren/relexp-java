package core.foundation.util.rand;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandUtil<T> {

    public static String getRandomItemFromStringList(List<String> list) {
        return list.get(getRandomIntNumber(0,list.size()));
    }

    public static double doubleInInterval(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    public static double doubleInInterval(Range range) {
        return doubleInInterval((double) range.lowerEndpoint(), (double) range.upperEndpoint());
    }


    public  T  getRandomItemFromList(List<T> list) {
        int randomIntNumber = getRandomIntNumber(0, list.size());
        return list.get(randomIntNumber);
    }

    public static int getRandomIntNumber(int minInclusive, int maxExclusive) {
        return (int) ((Math.random() * (maxExclusive - minInclusive)) + minInclusive);
    }

    public static double getRandomDouble(double minValue, double maxValue) {
        return minValue+Math.random()*(maxValue-minValue);
    }

    public static double randomNumberBetweenZeroAndOne() {
        return  getRandomDouble(0, 1);
    }


    /**
     * Returns a set of unique random indices, where the number of indices is specified by nSelected.
     * The indices are chosen from the range [0, nGross).
     *
     * @param nSelected The number of unique random indices to generate.
     * @param nGross The upper bound of the range from which to choose indices.
     * @return A set of unique random indices.
     */

    public static Set<Integer> randomIndices(int nSelected, int nGross) {
        Preconditions.checkArgument(nSelected <= nGross,
                "nSelected should be less than nGross");
        Set<Integer> uniqueRandomNumbers = new HashSet<>();
        while (uniqueRandomNumbers.size() < nSelected) {
            uniqueRandomNumbers.add(RandUtil.getRandomIntNumber(0, nGross));
        }
        return uniqueRandomNumbers;
    }



}
