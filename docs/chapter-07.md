# Chapter 7

## Overview

Chapter 7 adds a **safety layer** on top of the one-step TD Q-Learning
trainer introduced in chapter 4. The safety layer remembers state-action
pairs that have previously led to a "fail" outcome and, if the agent
proposes such an action again, replaces it with a random non-failing
alternative before it is applied to the environment.

The chapter reuses chapter 4's Treasure grid environment (see
`docs/chapter-04.md`) rather than defining its own environment. Runnable
examples live in `src/run/java/ch7`. The corresponding implementation
lives in `src/main/java/chapters/ch7`, split into a `domain` package
(safety layer, fail learners, the safety-aware trainer), a `factory`
package (dependency and safety-layer construction), and a `plotter`
package. Tests live in `src/test/java/chapters/ch7`.

For general repository layout and running instructions, see
`docs/CODE_ORGANIZATION.md` and `docs/HOW_TO_USE.md`. Chapter 3's
`core.gridrl` foundation is used directly, and chapter 4's `AgentQLearningGrid`
and Treasure environment classes are reused as-is.

## Runnable Examples

* **`RunnerHardCodedFail`**
  (`src/run/java/ch7/RunnerHardCodedFail.java`)
  Trains a Q-Learning agent on the Treasure environment with a
  **pre-populated (hard-coded) safety layer**: nine known-bad
  state-action pairs are added up front via
  `chapters.ch7.factory.SafetyLayerFactoryTreasure`. The safety layer
  itself never learns during training (`FailLearnerPassive`). Uses
  `chapters.ch7.factory.TrainerDependencySafeFactory`,
  `chapters.ch7.domain.trainer.TrainerOneStepTdQLearningWithSafety.givenSafetyLayerOf(...)`,
  and plots via `chapters.ch7.plotter.ChartPlotterSafe`.

* **`RunnerLearnedFail`**
  (`src/run/java/ch7/RunnerLearnedFail.java`)
  Trains the same environment but starts with an **empty** safety layer
  and lets it learn during training: every transition that ends in a
  fail state adds that state-action pair to the layer
  (`FailLearnerActive`). Uses
  `TrainerOneStepTdQLearningWithSafety.activeLearnerOf(...)`. Produces two
  plots: one for the full run and one restricted to the first 200
  episodes (`N_EPISODES_FEWER`) to show how quickly the safety-layer
  memory grows.

* **`RunnerHardCodedFailAvgReturnVsLearningRateAndProbRandom`**
  (`src/run/java/ch7/RunnerHardCodedFailAvgReturnVsLearningRateAndProbRandom.java`)
  A parameter sweep: repeats `RunnerHardCodedFail`-style training 100
  times per (learning rate, initial random-action probability) pair and
  plots average return with error bands, one curve per learning rate.
  Uses `core.plotting_core.plotting_2d.ErrorBandCreator` and
  `core.plotting_rl.progress_plotting.ErrorBandSaverAndPlotter`.

All three runners write their output under `ConfigFactory.pathPicsConfig().ch7()`.

## Main Code

### Domain

`chapters.ch7.domain` contains the safety mechanism and the trainer that
uses it:

* `safety_layer.SafetyLayer` — wraps a `FailStateActionMemory`. Its
  `correct(state, action)` method checks whether the proposed
  state-action pair is a known failure; if so, it returns a random
  action from the state's non-failing actions instead.
* `safety_layer.FailStateActionMemory` — the underlying `Set<StateActionGrid>`
  of known failing pairs, plus `getNonFailActions(state)` which asks the
  environment's informer for all valid actions and removes the known
  failures.
* `fail_learner.FailLearnerI` / `FailLearnerActive` / `FailLearnerPassive`
  — strategy for whether the safety layer updates itself after each
  episode. `FailLearnerActive` inspects each experience
  (`ExperienceGrid.isTransitionToFail()`) and adds new fail pairs;
  `FailLearnerPassive` does nothing, used when the layer is already
  hard-coded.
* `trainer.TrainerOneStepTdQLearningWithSafety` — implements
  `core.gridrl.TrainerGridI`. Per episode it: chooses an action from the
  agent, asks the `SafetyLayer` to correct it, steps the environment with
  the *corrected* action, records an `ExperienceGridCorrectedAction`,
  updates the agent's memory from the corrected experience, then (after
  the episode) lets the `FailLearnerI` update the safety layer and lets
  `AgentMemoryPenalizerCorrectedAction` penalize the agent for any
  correction that happened. Two static factory methods pick the
  fail-learner strategy: `givenSafetyLayerOf(...)` (passive, pre-built
  layer) and `activeLearnerOf(...)` (active, empty layer).
* `trainer.ExperienceGridCorrectedAction` — a record pairing a normal
  `core.gridrl.ExperienceGrid` with the action that was actually applied
  (`actionCorrected`); `isCorrected()` reports whether the safety layer
  intervened.
* `trainer.AgentMemoryPenalizerCorrectedAction` — after an episode,
  subtracts `trainerParameters().penaltyActionCorrection()` from the
  agent's memory value for every state-action pair that was corrected, so
  the agent also learns to avoid proposing unsafe actions in the future.
* `trainer.EpisodeInfoSafe` / `trainer.ProgressMeasureExtractorSafe` —
  turn a list of `ExperienceGridCorrectedAction` plus the safety layer's
  current memory size into a `core.plotting_rl.progress_plotting.ProgressMeasures`
  record (return, steps, and memory size) for recording/plotting.

### Factory

`chapters.ch7.factory`:

* `TrainerDependencySafeFactory` — builds a
  `core.gridrl.TrainerGridDependencies` bundle for the Treasure
  environment. It reuses chapter 4's
  `chapters.ch4.implem.treasure.core.EnvironmentTreasure`,
  `EnvironmentParametersTreasureFactor`, `InformerTreasure`, and
  `StartStateSupplierTreasureMostLeft` unchanged, and wraps chapter 4's
  `chapters.ch4.domain.agent.AgentQLearningGrid` as the agent. It only
  supplies chapter-7-specific agent/trainer parameters (e.g.
  `penaltyActionCorrection(-1)`).
* `SafetyLayerFactoryTreasure` — builds an empty `SafetyLayer` and adds
  nine hard-coded `StateActionGrid` failure entries specific to the
  Treasure grid layout (used by `RunnerHardCodedFail` and the sweep
  runner, not by `RunnerLearnedFail`, which starts from an empty layer).

### Plotter

`chapters.ch7.plotter.ChartPlotterSafe` — the chapter's post-training
plotting helper. Besides the usual state-value/policy heat maps and
return/steps progress curves (via `core.plotting_rl.chart.GridAgentPlotter`
and `core.plotting_rl.progress_plotting.PlotterProgressMeasures`, as in
chapter 4), it also plots `ProgressMeasureEnum.SIZE_MEMORY` — the growth
of the safety layer's fail memory over episodes — which is specific to
this chapter.

## Shared Code

Chapter 7 reuses, rather than reimplements, most of its environment and
agent code:

* `chapters.ch4.implem.treasure.*` — the Treasure environment, its
  parameters/informer, and its "most left" start-state supplier are used
  directly by `TrainerDependencySafeFactory`. No chapter-7-specific
  environment exists; the safety layer is added purely as a wrapper
  around action selection.
* `chapters.ch4.domain.agent.AgentQLearningGrid` — the same Q-Learning
  agent from chapter 4 is reused; the safety layer changes only which
  action is actually *applied* to the environment, not how the agent
  itself learns.
* `chapters.ch4.domain.animation.AnimationDummy` — used by all three
  runners as the no-op animation passed to `train(...)`.
* `core.gridrl` — `StateGrid`, `ActionGrid`, `StateActionGrid`,
  `StepReturnGrid`, `ExperienceGrid`, `TrainerGridDependencies`,
  `TrainerGridI`, `TrainerGridParameters` (introduced in chapter 3, used
  throughout chapter 4) form the foundation `SafetyLayer` and the
  chapter-7 trainer are built on.
* `core.plotting_rl.progress_plotting` — `RecorderProgressMeasures`,
  `ProgressMeasures`, `ProgressMeasureEnum`, `PlotterProgressMeasures`,
  and (for the sweep runner) `ErrorBandSaverAndPlotter` — the same
  progress-recording/plotting infrastructure used by chapter 4, extended
  here with the `SIZE_MEMORY` measure.
* `core.plotting_core.plotting_2d.ErrorBandCreator` — generic error-band
  plotting used by `RunnerHardCodedFailAvgReturnVsLearningRateAndProbRandom`.
* `core.foundation.config.ConfigFactory` — resolves output paths
  (`pathPicsConfig().ch7()`) and plot settings, same pattern as other
  chapters.

The test `TestTrainerOneStepTdQLearningWithSafetyHardCodedFails` also
pulls in `chapters.ch4.implem.treasure.start_state_suppliers.StartStateSupplierPositionGiven`
and `chapters.ch4.implem_animation.AnimationTreasure`, confirming these
chapter-4 classes are shared beyond the main runners as well.

No deprecated or stale code was found under `chapters.ch7`.

## Code Flow

```
run (RunnerHardCodedFail / RunnerLearnedFail, src/run/java/ch7)
  -> factory (TrainerDependencySafeFactory)              -- builds TrainerGridDependencies, reusing ch4 Treasure env + AgentQLearningGrid
  -> factory (SafetyLayerFactoryTreasure)                -- (hard-coded variant only) pre-populates a SafetyLayer
  -> domain.trainer.TrainerOneStepTdQLearningWithSafety   -- episode loop: choose action -> safetyLayer.correct(...) -> step environment
    -> domain.fail_learner.FailLearnerActive/Passive      -- (learned variant) grows the safety layer from fail transitions
    -> domain.trainer.AgentMemoryPenalizerCorrectedAction -- penalizes the agent for corrected actions
    -> domain.trainer.ProgressMeasureExtractorSafe        -- records return/steps/memory size per episode
  -> plotter.ChartPlotterSafe                             -- policy/value heat maps + progress curves incl. safety-memory growth
```

## Where to Start

Start with **`RunnerHardCodedFail`**
(`src/run/java/ch7/RunnerHardCodedFail.java`). It is the simplest of the
three runners: the safety layer is fixed and inspectable
(`SafetyLayerFactoryTreasure`), so you can see exactly what
`SafetyLayer.correct(...)` does before moving on to
`RunnerLearnedFail`, where the layer is built up automatically during
training via `FailLearnerActive`.
