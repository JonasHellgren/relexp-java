package core.foundation.util.list_array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/***
 * Example [[1 2 3],[1 2 3]] -> [1 1], [2 2] and [3 3].
 */

public class ListTransposer {

    public static List<List<Double>> transpose(List<List<Double>> originalList) {
        if (originalList.isEmpty()) {
            return Collections.emptyList();
        }

        List<List<Double>> result = getInitLists(originalList);
        modifyResult(originalList, result);
        return result;
    }

    private static void modifyResult(List<List<Double>> originalList, List<List<Double>> result) {
        for (List<Double> innerList : originalList) {
            for (int i = 0; i < innerList.size(); i++) {
                result.get(i).add(innerList.get(i));
            }
        }
    }

    static List<List<Double>> getInitLists(List<List<Double>> originalList) {
        List<List<Double>> result = new ArrayList<>();
        int size = originalList.get(0).size();
        for (int i = 0; i < size; i++) {
            result.add(new ArrayList<>());
        }
        return result;
    }

}
