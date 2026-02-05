package chapters.ch14.implem.pong;

import chapters.ch14.domain.environment.StepReturn;
import chapters.ch14.domain.executor.ExecutorI;
import chapters.ch14.domain.planner.Planner;
import chapters.ch14.domain.planner.PlanningStatus;
import chapters.ch14.domain.trainer.TrainerDependencies;
import chapters.ch14.pong_animation.PongGraphicsDTO;
import chapters.ch14.pong_animation.PongGraphicsServer;
import chapters.ch14.implem.pong_memory.StateAdapterPong;
import chapters.ch14.plotting.MeasuresCombLP;
import chapters.ch14.plotting.Recorder;
import core.foundation.gadget.cond.Counter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import java.util.function.Supplier;

/**
 * This class implements an Executor for the Pong environment.
 * It's responsible for executing the planning process, validating the number of episodes and steps per episode.
 * It also logs in recorder and defines the server for animation.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Log
public class ExecutorPong<SI, S, A> implements ExecutorI<SI, S, A> {

    private TrainerDependencies<SI, S, A> dependencies;
    private Planner<SI, S, A> planner;
    @Getter
    private Recorder recorder;
    private PongGraphicsServer server;
    private long sleepTimeAnimationMs;
    private String name;

    public static <SI, S, A> ExecutorPong<SI, S, A> of(TrainerDependencies<SI, S, A> dependencies,
                                                       Planner<SI, S, A> planner) {
        return new ExecutorPong<>(dependencies, planner, Recorder.empty(), null, 0,"");
    }

    public static <SI, S, A> ExecutorPong<SI, S, A> of(TrainerDependencies<SI, S, A> dependencies,
                                                       Planner<SI, S, A> planner,
                                                       PongGraphicsServer server,
                                                       long sleepTimeAnimationMs,
                                                       String name) {
        return new ExecutorPong<>(dependencies, planner, Recorder.empty(), server, sleepTimeAnimationMs,name);
    }

    @SneakyThrows
    @Override
    public void validate(int nEpisodes, int maxStepsPerEpisode) {
        var episCounter = Counter.ofMaxCount(nEpisodes);
        while (episCounter.isNotExceeded()) {
            var stepCounter = Counter.ofMaxCount(maxStepsPerEpisode);
            var measures = MeasuresCombLP.empty();
            var s = dependencies.getStartState();
            boolean isTerminal = false;
            var sss = (StatePong) s;
            var stateLong = StateAdapterPong.stateLong(dependencies.timeToHitCalculator(), sss);
            log.fine("start state = " + sss + ", stateLong = " + stateLong);
            while (stepCounter.isNotExceeded() && !isTerminal) {
                S finalS = s;
                Supplier<S> ss = () -> finalS;
                var planRes = planner.plan(ss, dependencies.longMemory());
                A a = planRes.firstAction().orElseThrow();
                var sr = dependencies.step(s, a);
                isTerminal = sr.isTerminal();
                stepCounter.increase();
                measures.addReward(sr.reward());
                s = sr.stateNew();
                maybeLog(planRes, sr, s, stepCounter);
                maybeDefineServer(s, sr, planRes, stepCounter);
            }
            recorder.add(measures);
            episCounter.increase();
        }

    }

    private void maybeDefineServer(S s, StepReturn<S> sr, PlanningStatus<A> planRes, Counter stepCounter)
            throws InterruptedException {
        if (server != null) {
            server.setGfxDTO(PongGraphicsDTO.of(
                    (StatePong) s,
                    sr.isFail(),
                    planRes.isAllRolloutsFailed(),
                    stepCounter.isExceeded(),
                    name));  //define Gfx to transmit
            Thread.sleep(sleepTimeAnimationMs);
        }
    }

    private static <S, A> void maybeLog(PlanningStatus<A> planRes, StepReturn<S> sr, S s, Counter stepCounter) {
        if (planRes.isAllRolloutsFailed()) {
            log.fine("isAllRolloutsFailed = " + planRes);
        }
        if (sr.isFail()) {
            log.fine("fail state = " + s + ", after nSteps=" + stepCounter.getCount());
        }
    }
}
