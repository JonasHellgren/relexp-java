package core.foundation.util.collections;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ListConverter {

    public static List<Double> integer2Double(List<Integer> list) {
        return list.stream().map(i -> (double) i).toList();
    }

    public static List<Integer> double2Integer(List<Double> list) {
        return list.stream()
                .map(Double::intValue)
                .toList();
    }


}
