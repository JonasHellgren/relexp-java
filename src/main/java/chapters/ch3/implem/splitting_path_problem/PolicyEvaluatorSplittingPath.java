package chapters.ch3.implem.splitting_path_problem;

import chapters.ch3.policies.SplittingPathPolicyI;
import core.foundation.util.math.LogarithmicDecay;
import core.gridrl.StartStateSupplierGridI;
import core.gridrl.StateValueMemoryGrid;
import lombok.Builder;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Builder
public class PolicyEvaluatorSplittingPath {

    private EnvironmentSplittingPath environment;
    private StateValueMemoryGrid memory;
    private int nFits;
    private LogarithmicDecay learningRate;
    private double discountFactor;
    private StartStateSupplierGridI startStateSupplier;
    @Builder.Default
    @Getter
    private List<Double> errorList = new ArrayList<>();


    public void evaluate(SplittingPathPolicyI policy) {
        errorList.clear();
        for (int i = 0; i < nFits; i++) {
            var state=startStateSupplier.getStartState();
            var action = policy.chooseAction(state);
            var sr = environment.step(state, action);
            var stateNext = sr.sNext();
            double reward = sr.reward();
            double valueNext = memory.read(stateNext);
            double valueTar = discountFactor * valueNext + reward;
            double lr=learningRate.calcOut(i);
            memory.fit(state, valueTar, lr);
            errorList.add(Math.abs(valueTar - memory.read(state)));
        }
    }

}
