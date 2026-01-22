package core.foundation.util.collections;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ListConverterUtil {

    public static List<Double> integer2Double(List<Integer> list) {
        return list.stream().map(i -> (double) i).toList();
    }

    public static List<Integer> double2Integer(List<Double> list) {
        return list.stream()
                .map(Double::intValue)
                .toList();
    }


}
