package core.nextlevelrl.radial_basis;

import core.foundation.gadget.cond.Counter;
import core.foundation.util.cond.ConditionalsUtil;
import core.foundation.util.math.MathUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TdErrorClipper {

    private double errorBound;
    private Counter counter;
    private int errorListSize;

    public static TdErrorClipper of(double errorBound) {
        return new TdErrorClipper(errorBound,Counter.empty(),0);
    }

    public int nClips() {
        return counter.getCount();
    }

    public int errorListSize () {
        return errorListSize;
    }

    public List<Double> clip(List<Double> errorList) {
        counter.reset();
        errorListSize = errorList.size();
        errorList.forEach(e -> ConditionalsUtil.executeIfTrue(Math.abs(e) > errorBound, () -> counter.increase()));
        return errorList.stream().map(v -> MathUtil.clip(v,-errorBound,errorBound)).toList();
    }


}
