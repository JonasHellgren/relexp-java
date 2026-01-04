package chapters.ch10.bandit._shared;

import core.foundation.util.collections.List2ArrayConverter;
import core.foundation.util.collections.ListCreator;
import core.foundation.util.collections.MyArrayUtil;
import lombok.experimental.UtilityClass;

/***
 * ∇logπ(z_i )=oh(i)-softmax(z_i)
 */

@UtilityClass
public class GradLogCalculatorDiscreteActions {

    public static double[] calc(int indexAction, double[] probArray) {
        var oh= ListCreator.createListWithOneHot(probArray.length,indexAction);
        return MyArrayUtil.add(
                List2ArrayConverter.convertListToDoubleArr(oh),
                MyArrayUtil.negate(probArray));
    }

}
