package chapters.ch14.implem.pong;

import chapters.ch14.domain.environment.StepReturn;
import chapters.ch14.domain.interfaces.ExecutorI;
import chapters.ch14.domain.planner.Planner;
import chapters.ch14.domain.planner.PlanningStatus;
import chapters.ch14.domain.trainer.TrainerDependencies;
import chapters.ch14.pong_animation.PongGraphicsDTO;
import chapters.ch14.pong_animation.PongGraphicsServer;
import chapters.ch14.plotting.MeasuresCombLP;
import chapters.ch14.plotting.Recorder;
import core.foundation.gadget.cond.Counter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

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
    private String titleInAnimationWindow;

    public static <SI, S, A> ExecutorPong<SI, S, A> of(TrainerDependencies<SI, S, A> dependencies,
                                                       Planner<SI, S, A> planner) {
        return new ExecutorPong<>(dependencies, planner, Recorder.empty(), null,"");
    }

    public static <SI, S, A> ExecutorPong<SI, S, A> of(TrainerDependencies<SI, S, A> dependencies,
                                                       Planner<SI, S, A> planner,
                                                       PongGraphicsServer server,
                                                       String name) {
        return new ExecutorPong<>(dependencies, planner, Recorder.empty(), server, name);
    }

    @SneakyThrows
    @Override
    public void execute(int nTrials, int maxStepsPerEpisode) {
        var d=dependencies;
        var episCounter = Counter.ofMaxCount(nTrials);
        var stepCounter = Counter.ofMaxCount(maxStepsPerEpisode);
        while (episCounter.isNotExceeded()) {
            var measures = MeasuresCombLP.empty();
            S s = dependencies.getStartState();
            boolean isTerminal = false;
            stepCounter.reset();
            while (stepCounter.isNotExceeded() && !isTerminal) {
                S finalS = s;  //effective final in lambda expression
                var planRes = planner.plan(() -> finalS, dependencies.longMemory());
                var sr = dependencies.step(s, planRes);
                isTerminal = sr.isTerminal();
                stepCounter.increase();
                measures.addReward(sr.reward());
                s = sr.stateNew();
                maybeLog(planRes, sr, s, d.stepCounter());
                maybeSendGfxDto(s, sr, planRes, d.stepCounter());
            }
            recorder.add(measures);
            episCounter.increase();
        }
    }

    private void maybeSendGfxDto(S s, StepReturn<S> sr, PlanningStatus<A> planRes, Counter stepCounter)
            throws InterruptedException {
        if (server != null) {
            server.setGfxDTO(PongGraphicsDTO.of(
                    (StatePong) s,
                    sr.isFail(),
                    planRes.isAllRolloutsFailed(),
                    stepCounter.isExceeded(),
                    titleInAnimationWindow));  //define Gfx to transmit
            Thread.sleep(server.getSleepTimeMs());
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
