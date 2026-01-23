package chapters.ch11.factory;

import chapters.ch11.domain.agent.core.AgentLunar;
import chapters.ch11.domain.environment.core.EnvironmentLunar;
import chapters.ch11.domain.environment.param.LunarParameters;
import chapters.ch11.domain.environment.startstate_suppliers.StartStateSupplierRandomAndClipped;
import chapters.ch11.domain.trainer.core.TrainerDependencies;
import core.foundation.gadget.timer.CpuTimer;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.Pair;

@UtilityClass
public class DependencyFactory {

    public static final double MAX_START_SPD = -2d;


    public static TrainerDependencies produce(LunarParameters ep, int stepHorizon, int nEpisodes) {
        var p = LunarAgentParamsFactory.newDefault(ep);
        var tp = TrainerParamsFactory.of(stepHorizon,nEpisodes);
        return TrainerDependencies.builder()
                .agent(AgentLunar.zeroWeights(p, ep))
                .environment(EnvironmentLunar.of(ep))
                .trainerParameters(tp)
                .startStateSupplier(StartStateSupplierRandomAndClipped.create(
                        ep, Pair.create(ep.yMax(), ep.yMax()), Pair.create(-ep.spdMax(), MAX_START_SPD)))
                .timer(CpuTimer.empty())
                .build();
    }

}
