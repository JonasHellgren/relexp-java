package chapters.ch12.inv_pendulum.domain.agent.evaluator;

import chapters.ch12.inv_pendulum.domain.environment.startstate_supplier.StartStateSupplierI;
import chapters.ch12.inv_pendulum.domain.trainer.core.TrainerDependencies;
import chapters.ch12.inv_pendulum.plotting.MeasuresPendulumSimulation;
import chapters.ch12.inv_pendulum.plotting.PendulumRecorder;
import core.foundation.gadget.cond.Counter;
import core.foundation.util.cond.ConditionalsUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;


/**
 * Evaluates the performance of a pendulum agent.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Log
public class PendulumAgentEvaluator {

    public static final int N_STEPS_BETWEEN_RECORDING = 1;

    TrainerDependencies dependencies;
    PendulumRecorder recorder;

    public static PendulumAgentEvaluator of(TrainerDependencies dependencies) {
        return new PendulumAgentEvaluator(dependencies, PendulumRecorder.empty());
    }

    /**
     * Evaluates the performance of the agent for a given time period.
     *
     * @param timeMax the maximum time for evaluation
     * @param ssSup the start state supplier
     * @return true if the agent fails, false otherwise
     */
    public boolean evaluate(double timeMax, StartStateSupplierI ssSup) {
        log.info("Evaluating...");
        var env=dependencies.environment();
        var state=ssSup.getStartState();
        var agent=dependencies.agent();
        var pendPar=dependencies.environment().getParameters();
        var counter= Counter.ofMaxCount((int) (timeMax/pendPar.dt()));
        boolean isFail=false;
        while (counter.isNotExceeded() && !isFail) {
            var action = agent.chooseActionNoExploration(state);
            var sr = env.step(state, action);
            var measures = MeasuresPendulumSimulation.of(state, action, pendPar);
            state = sr.stateNew();
            addMeasure(counter, measures);
            isFail=sr.isFail();
            counter.increase();
        }
        logging(isFail, counter);
        return isFail;
    }

    private void addMeasure(Counter counter, MeasuresPendulumSimulation measures) {
        ConditionalsUtil.executeIfTrue(counter.getCount() % N_STEPS_BETWEEN_RECORDING == 0,
                () -> recorder.add(measures));
    }

    private static void logging(boolean isFail, Counter counter) {
        if (isFail) {
            log.info("Fail after " + counter.getCount() + " steps");
        } else {
            log.info("Success");
        }
    }


}
