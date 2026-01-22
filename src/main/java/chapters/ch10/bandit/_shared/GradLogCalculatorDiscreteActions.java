package chapters.ch10.bandit._shared;

import core.foundation.util.collections.List2ArrayConverterUtil;
import core.foundation.util.collections.ListCreatorUtil;
import core.foundation.util.collections.ArrayUtil;
import lombok.experimental.UtilityClass;

/***
 * ∇logπ(z_i )=oh(i)-softmax(z_i)
 */

@UtilityClass
public class GradLogCalculatorDiscreteActions {

    public static double[] calc(int indexAction, double[] probArray) {
        var oh= ListCreatorUtil.createListWithOneHot(probArray.length,indexAction);
        return ArrayUtil.add(
                List2ArrayConverterUtil.convertListToDoubleArr(oh),
                ArrayUtil.negate(probArray));
    }

}
