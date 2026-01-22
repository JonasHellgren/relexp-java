package core.foundation.gadget.set;

import core.foundation.util.rand.RandUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetUtils {

    /**
     * Checks if all elements in a list are the same.
     *
     * @param list the list to check
     * @param <T> the type of elements in the list
     * @return true if all elements are the same, false otherwise
     */
    public  static <T> boolean allSame(List<T> list) {
        T firstItem = list.get(0);
        return list.stream().allMatch(i -> i.equals(firstItem));
    }

    /**
     * Returns the union of two lists, i.e., a list containing all unique elements from both lists.
     *
     * @param list1 the first list
     * @param list2 the second list
     * @param <T> the type of elements in the lists
     * @return the union of the two lists
     */
    public  static <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> union=new HashSet<>(list1);
        union.addAll(list2);
        return new ArrayList<>(union);
    }

    /**
     * Returns the difference between two lists, i.e., a list containing all elements that are in the first list but not in the second list.
     *
     * @param list1 the first list
     * @param list2 the second list
     * @param <T> the type of elements in the lists
     * @return the difference between the two lists
     */
    public  static <T> List<T> difference(List<T> list1, List<T> list2) {
        List<T> union = union(list1, list2);
        Set<T> difference = new HashSet<>(union);
        intersection(list1, list2).forEach(difference::remove);
        return new ArrayList<>(difference);
    }

    /**
     * Returns the intersection of two lists, i.e., a list containing all elements that are common to both lists.
     *
     * @param list1 the first list
     * @param list2 the second list
     * @param <T> the type of elements in the lists
     * @return the intersection of the two lists
     */
    public  static <T> List<T> intersection(List<T> list1, List<T> list2) {
        Set<T> same=new HashSet<>(list1);
        same.retainAll(list2);
        return new ArrayList<>(same);
    }

    /**
     * Retrieves a random element from a given set.
     *
     * @param set the set to retrieve an element from
     * @param <T> the type of elements in the set
     * @return a random element from the set
     * @throws IllegalArgumentException if the set is empty
     */
    public static <T> T getAnyFromSet(Set<T> set) {
        int randomIntNumber = RandUtil.getRandomIntNumber(0, set.size());
        return new ArrayList<>(set).get(randomIntNumber);
    }


    /**
     * Checks if all elements in a set are different.
     *
     * This method uses the property of sets that all elements are unique.
     * It compares the size of the set with the number of distinct elements in the set.
     * If they are equal, it means that all elements in the set are different.
     *
     * @param set the set to check
     * @param <T> the type of elements in the set
     * @return true if all elements are different, false otherwise
     */
    public static <T> boolean allDifferent(Set<T> set) {
        return set.size() == set.stream().distinct().count();
    }
}
