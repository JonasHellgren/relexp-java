package chapters.ch5.domain.policy_evaluator;

import chapters.ch5.domain.environment.StartStateSupplierI;
import chapters.ch5.domain.episode_generator.EpisodeGeneratorI;
import chapters.ch5.domain.memory.StateActionMemoryMcI;
import chapters.ch5.domain.memory.StateMemoryMcI;
import core.foundation.util.math.LogarithmicDecay;
import lombok.Builder;
import lombok.With;

import java.util.List;

@Builder
@With
public record EvaluatorDependencies(
        StartStateSupplierI startStateSupplier,
        EpisodeGeneratorI episodeGenerator,
        StateMemoryMcI stateMemory,
        StateActionMemoryMcI stateActionMemory,
        LogarithmicDecay learningRate,
        EvaluatorSettings settings,
        List<Double> errorList
) {
}
