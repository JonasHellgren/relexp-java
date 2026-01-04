package core.foundation.util.collections;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MyListUtils {

    private MyListUtils() {
    }

    public static OptionalDouble findMin(List<Double> list)  {
        return  list.stream().mapToDouble(Double::doubleValue)
                .min();
    }

    public static OptionalDouble findMax(List<Double> list)  {
        return  list.stream().mapToDouble(Double::doubleValue)
                .max();
    }

    public static OptionalDouble findAverage(List<Double> list) {
        return list.stream()
                .mapToDouble(a -> a)
                .average();
    }

    public static OptionalDouble findAverageOfAbsolute(List<Double> list) {
        return list.stream()
                .mapToDouble(Math::abs)
                .average();
    }

    public static double sumList(List<Double> list) {
        return list.stream()
                .mapToDouble(a -> a)
                .sum();
    }

    public static <T> boolean isNullOrEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> Optional<T> findFirst(List<T> list) {
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }


    public static <T> Optional<T> findEnd(List<T> list) {
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(list.size() - 1));
    }

    public static List<Double> sumListElements(List<Double> listA, List<Double> listB) {
        return IntStream.range(0, listA.size())
                .mapToObj(i -> listA.get(i) + listB.get(i))
                .toList();
    }

    public static Integer sumIntegerList(List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).sum();
    }


    public static Double sumDoubleList(List<Double> list) {
        return list.stream().mapToDouble(Double::doubleValue).sum();
    }

    public static List<Double> addScalarToListElements(List<Double> listA, Double scalar) {
        return listA.stream().map(num -> num + scalar)
                .collect(Collectors.toList());
    }

    public static List<Double> multiplyListElements(List<Double> list, double scalar) {
        return list.stream()
                .map(num -> num * scalar)
                .collect(Collectors.toList());
    }

    public static double dotProduct(List<Double> listA,List<Double> listB)  {
        return IntStream.range(0, Math.min(listA.size(), listB.size()))
                .mapToDouble(i -> listA.get(i) * listB.get(i))
                .sum();
    }

    public static List<Double> elementProduct(List<Double> listA,List<Double> listB)  {
        return IntStream.range(0, Math.min(listA.size(), listB.size()))
                .mapToObj(i -> listA.get(i) * listB.get(i))
                .collect(Collectors.toList());
    }

    public static List<Double> elementSubtraction(List<Double> listA, List<Double> listB) {
        return IntStream.range(0, listA.size())
                .mapToDouble(i -> listB.get(i) - listA.get(i))
                .boxed()
                .toList();
    }


    public static List<Double> everyItemAbsolute(List<Double> errData) {
        return errData.stream().map(Math::abs).toList();
    }

    public static double[] toArray(List<Double> list) {
        return list.stream().mapToDouble(Number::doubleValue).toArray();
    }

    public static boolean areDoubleArraysEqual(double[] x, double[] y, double tol)
    {
        if (x.length!=y.length) {
            return false;
        }
        for (int i = 0; i < x.length; i += 1)
        {
            if (Math.abs((y[i] - x[i])) > tol)
            {
                return false;
            }
        }
        return true;
    }

    public static<T> List<T> merge(List<T> list1, List<T> list2)
    {
        List<T> list = new ArrayList<>(list1);
        list.addAll(list2);
        return list;
    }

    public static OptionalInt findMinInt(List<Integer> list)  {
        return  list.stream().mapToInt(Integer::intValue)
                .min();
    }

    public static OptionalInt findMaxInt(List<Integer> list)  {
        return  list.stream().mapToInt(Integer::intValue)
                .max();
    }

    public static OptionalDouble findAverageInt(List<Integer> list) {
        return list.stream().mapToDouble(a -> a).average();
    }

    public static double[] doubleListToArray(List<Double> doubleList) {
        return  doubleList.stream().mapToDouble(d -> d).toArray();
    }

    public static List<Double> absOfDoubles(List<Double> list) {
        return list.stream().map(Math::abs).collect(Collectors.toList());
    }

    public static List<Double> diff(List<Double> list) {
        return  IntStream.range(1, list.size())
                .mapToObj(i -> list.get(i) - list.get(i - 1))
                .toList();
    }

    public static double sqrtOfSumOfSquares(List<Double> list) {
        return Math.sqrt(MyListUtils.sumList(MyListUtils.elementProduct(list, list)));
    }


/*
    public static List<Double> arrayPrimitiveDoublesToList(double[] arr) {
        return DoubleStream.of(arr).boxed().collect(Collectors.toList());
    }
*/


/*
    public static List<Double> createEqualElementValues(int len, double value) {
        return new ArrayList<>(Collections.nCopies(len,value));
    }
*/


}
