package core.foundation.util.list;

import java.util.List;

public class List2ArrayConverter {

    private List2ArrayConverter() {
    }

    public static double[] convertListToDoubleArr(List<Double> inList) {
        double[] outArray = new double[inList.size()];
        for (int i = 0; i < inList.size(); i++) {
            outArray[i] = inList.get(i); // Auto-unboxing converts Double to double
        }
        return outArray;
    }

    public static double[] convertIntegerListToDoubleArr(List<Integer> inList) {
        double[] outArray = new double[inList.size()];
        for (int i = 0; i < inList.size(); i++) {
            outArray[i] = inList.get(i); // Auto-unboxing converts Integer to double
        }
        return outArray;
    }


        public static double[][] convertListWithListToDoubleMat(List<List<Double>> inList) {
        if (inList.isEmpty()) {
            return new double[0][0]; // Return an empty 2D array if the input list is empty
        }
        double[][] outArray = new double[inList.size()][];
        for (int i = 0; i < inList.size(); i++) {
            List<Double> innerList = inList.get(i);
            outArray[i] = new double[innerList.size()];
            for (int j = 0; j < innerList.size(); j++) {
                outArray[i][j] = innerList.get(j); // Auto-unboxing converts Double to double
            }
        }
        return outArray;
    }

    /**
     * Converts a list of Double objects to an array of String objects.
     *
     * This method uses Java 8's Stream API to map each Double object to its String representation,
     * and then collects the results into an array.
     *
     * @param doubleList The list of Double objects to be converted.
     * @param decimalPlaces The number of decimal places to format the strings to.
     * @return An array of String objects representing the input Double objects.
     */
    public static String[] convertListToStringArr(List<Double> doubleList, int decimalPlaces) {
        return doubleList.stream()
                .map(d -> String.format("%." + decimalPlaces + "f", d))
                .toArray(String[]::new);
    }
}
