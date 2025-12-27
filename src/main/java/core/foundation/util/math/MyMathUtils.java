package core.foundation.util.math;

import lombok.extern.java.Log;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Log
public final class MyMathUtils {

    /**
     * Clips a given variable to a specified range.
     *
     * This method ensures that the variable is within the range defined by minValue and maxValue.
     * If the variable is less than minValue, it is set to minValue. If it is greater than maxValue, it is set to maxValue.
     *
     * @param variable the value to be clipped
     * @param minValue the minimum allowed value
     * @param maxValue the maximum allowed value
     * @return the clipped value
     */
    public static double clip(double variable, double minValue, double maxValue) {
        double lowerThanMax= Math.min(variable, maxValue);
        return Math.max(lowerThanMax, minValue);
    }

    public static int clip(int variable, int minValue, int maxValue) {
        int lowerThanMax= Math.min(variable, maxValue);
        return Math.max(lowerThanMax, minValue);
    }

    public static boolean isZero(double value) {
        return (Math.abs(value-0)<2*Double.MIN_VALUE);
    }

    public static boolean isZero(int value) {
        return (value==0);
    }

    public static boolean isNonZero(double value) {
        return !isZero(value);
    }

    public static boolean isNeg(double value) {
        return value<-Double.MIN_VALUE;
    }

    public static boolean isPos(double value) {
        return value>Double.MIN_VALUE;
    }

    public static boolean isInRange(double value, double min, double max) {
        return (value>=min && value<=max);
    }

    public static boolean isInRange(int value, int min, int max) {
        return (value>=min && value<=max);
    }

    public static boolean isEqualDoubles(double s1, double s2, double delta)
    {
        return (Math.abs(s1-s2)<delta);
    }

    public static boolean isEven(int value) {
        return (value%2==0);
    }

    public static double setAsSmallIfZero(double value) {
        return (isZero(value)?Double.MIN_VALUE:value);
    }

    public static String getRoundedNumberAsString(Double value, int nofDigits)  {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);  //ENGLISH => point
        nf.setMaximumFractionDigits(nofDigits);
        return nf.format(value);
    }

    public static double normalize(double x,double xMin,double xMax) {
        return (x-xMin)/(xMax-xMin);
    }

    public static boolean compareIntScalars(int s1,int s2, int delta)
    {
        return (Math.abs(s1-s2)<delta);
    }

    public static boolean compareDoubleScalars(double s1,double s2, double delta)
    {
        return (Math.abs(s1-s2)<delta);
    }

    public static boolean compareDoubleArrays(double arr1[],double arr2[], double delta)
    {
        if (arr1.length != arr2.length)
            return false;

        for (int i = 0; i < arr1.length; i++) {

            if (Math.abs(arr1[i] - arr2[i])>delta)
                return false;
        }
        return true;
    }

    public static List<Double> accumulatedSum(List<Double> list) {
        // Use AtomicReference to hold the running total
        AtomicReference<Double> runningSum = new AtomicReference<>(0.0);
        return list.stream().map(numberInList -> runningSum.updateAndGet(sum -> sum + numberInList))
                .collect(Collectors.toList());
    }


    public static double scale(double xNorm,double xMin,double xMax) {
        return xMin+xNorm*(xMax-xMin);
    }

    public static boolean isInLimits(double value, int lower, int upper) {
        return value >= lower && value <= upper;
    }

    public static boolean isInLimits(double value, double lower, double upper) {
        return value >= lower && value <= upper;
    }




}
