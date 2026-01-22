package core.foundation.util.collections;

import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

import java.util.Arrays;

@Log
@UtilityClass
public class IndexFinderUtil {


    /**
     * Finds the index of a target value in a sorted integer array.
     *
     * @param arr the sorted integer array to search
     * @param t the target value to find
     * @return the index of the target value, or -1 if not found
     */
    public static int findIndex(int[] arr, int t)
    {
        int index = Arrays.binarySearch(arr, t);
        return (index < 0) ? -1 : index;
    }

    /**
     * Finds the bucket that a given value falls into in a sorted double array.
     *
     * @param array the sorted double array to search
     * @param value the value to find the bucket for
     * @return the index of the bucket that the value falls into, or -1 if the value is below the first bucket
     */

    public static int findBucket(double[] array, double value) {

        if (value <array[0]) {
            log.warning("value below first bucket");
            return -1;
        }

        for (int i = 0; i < array.length-1; i++) {
            if (value < array[i+1]) {
                return i;  // Value falls into bucket i
            }
        }

        log.warning("Value exceeds the last bucket");
        return array.length-1;
    }

}
