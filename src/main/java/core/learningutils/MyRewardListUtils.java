package core.learningutils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static core.foundation.util.collections.MyListUtils.dotProduct;
import static core.foundation.util.collections.MyListUtils.elementProduct;


public final class MyRewardListUtils {

    private MyRewardListUtils() {
    }

    /**
     * list = [10 10 10], df=0.5 => listDf=[1*df^0 1*df^1 1*df^2] => dotProduct(list,listDf)=10+5+2.5
     */
    public static double discountedSum(List<Double> list, double discountFactor) {
        var listDf = getDiscountList(list.size(), discountFactor);
        return dotProduct(list, listDf);
    }


    /**
     * 1d,10d,10d , df=0.5->  1d,5d,2.5d
     */

    public static List<Double> discountedElements(List<Double> list, double discountFactor) {
        var listDf = getDiscountList(list.size(), discountFactor);
        return elementProduct(list, listDf);
    }

    /**
     * 1d,10d,10d , df=0.5->  0.25d,5d,10d
     */

    public static List<Double> discountedElementsReverse(List<Double> list, double discountFactor) {
        var listDf = getDiscountList(list.size(), discountFactor);
        Collections.reverse(listDf);
        return elementProduct(list, listDf);
    }

    /**
     * rewards=[0,1,1] => returns=[2,2,1]
     */

    public static List<Double> getReturns(List<Double> rewards) {
        double singleReturn = 0;
        List<Double> returns = new ArrayList<>();
        for (int i = rewards.size() - 1; i >= 0; i--) {
            singleReturn = singleReturn + rewards.get(i);
            returns.add(singleReturn);
        }
        Collections.reverse(returns);
        return returns;
    }

    /**
     * Returns a list of discount factors to be used for calculating the discounted sum or
     * discounted elements of a list.
     *
     * @param len            the length of the list to create discount factors for
     * @param discountFactor the discount factor, e.g. 0.5
     * @return a list of discount factors
     */

    public static List<Double> getDiscountList(int len, double discountFactor) {
        List<Double> listDf = new ArrayList<>();
        double df = 1;
        for (int i = 0; i < len; i++) {
            listDf.add(df);
            df = df * discountFactor;
        }
        return listDf;
    }

    /**
     * Calculates the cumulative sum of a list of values.
     *
     * @param values the list of values to calculate the cumulative sum for
     * @return a list of cumulative sums
     */

    public static List<Double> cumulativeSum(List<Double> values) {
        double sum = 0; // running sum
        List<Double> accumValues = new ArrayList<>();
        for (Double value : values) {
            sum += value;  // Update the running sum
            accumValues.add(sum);  // Add the running sum to the accumValues list
        }
        return accumValues;
    }


}
