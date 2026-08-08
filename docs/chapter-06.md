# Chapter 6

## Overview

Chapter 6 covers multi-step Sarsa: state-action control, state prediction,
and an "update during vs. after episode" comparison, all using an
n-step (`backupHorizon`) return instead of the one-step TD target used in
chapter 4. The chapter does **not** define its own grid-world environments.
Instead it builds a generic multi-step training framework
(`chapters.ch6.domain`) and plugs it into two environments borrowed from
earlier chapters: the splitting-path grid from chapter 3 and the Treasure
grid from chapter 4.

Runnable examples live in `src/run/java/ch6`. The corresponding
implementation lives in `src/main/java/chapters/ch6`, split into `domain`
(the multi-step trainer/agent framework, algorithm-agnostic), `implem`
(concrete agents and factories for the splitting and treasure problems),
`implem_animation` (a live animation for the splitting problem), and
`plotting`. Tests live in `src/test/java/chapters/ch6`.

For general repository layout and running instructions, see
`docs/CODE_ORGANIZATION.md` and `docs/HOW_TO_USE.md`. For the reused
environments, see `docs/chapter-03.md` (splitting path) and
`docs/chapter-04.md` (Treasure).

## Runnable Examples

* **`RunnerTrainerStateActionControlAfterEpisodeSplitting`**
  (`src/run/java/ch6/RunnerTrainerStateActionControlAfterEpisodeSplitting.java`)
  Trains a learning agent with n-step Sarsa where memory updates happen
  **after** the whole episode has been generated. Uses
  `chapters.ch6.implem.factory.TrainerDependenciesFactorySplitting`
  (`learnPolicySplittingAfterEpis`) and
  `chapters.ch6.domain.trainers.after_episode.TrainerStateActionControlAfterEpisode`,
  built on chapter 3's `EnvironmentSplittingPath`.

* **`RunnerStateActionControlDuringEpisodeSplitting`**
  (`src/run/java/ch6/RunnerStateActionControlDuringEpisodeSplitting.java`)
  Same splitting-path problem, but memory updates happen **during** the
  episode as soon as enough steps (`backupHorizon`) are available. Uses
  `TrainerDependenciesFactorySplitting.learnPolicySplittingDuringEpis` and
  `chapters.ch6.domain.trainers.during_episode.TrainerStateActionControlDuringEpisode`.
  Comparing this runner with the "after episode" one shows the difference
  between the two update strategies described in the chapter.

* **`RunnerTrainerStateActionControlAfterEpisodeTreasure`** and
  **`RunnerTrainerStateActionControlDuringEpisodeTreasure`**
  (`src/run/java/ch6/RunnerTrainerStateActionControlAfterEpisodeTreasure.java`,
  `RunnerTrainerStateActionControlDuringEpisodeTreasure.java`)
  The same after-episode / during-episode comparison, run on chapter 4's
  Treasure grid instead of the splitting path. Uses
  `chapters.ch6.implem.factory.TrainerDependenciesFactoryTreasure.treasure(...)`.

* **`RunnerTrainerStatePredictorSplitting`**
  (`src/run/java/ch6/RunnerTrainerStatePredictorSplitting.java`)
  Uses `chapters.ch6.domain.trainers.state_predictor.TrainerStatePredictor`
  (multi-step **state prediction** under a fixed random policy, not control)
  on the splitting path, and plots the learned value of state `(0,1)` versus
  backup horizon (averaged over repeated runs), showing that a larger
  horizon reduces bias/variance of the estimate.

* **`RunnerPredictorSplittingAnimation`**
  (`src/run/java/ch6/RunnerPredictorSplittingAnimation.java`)
  Runs `TrainerStatePredictor` with a live Swing animation
  (`chapters.ch6.implem_animation.AnimationSplit`) showing the agent moving
  through the splitting grid and the learned value heat map updating
  episode by episode.

* **`RunnerPlotErrorState10VersusNstepsAndLearningRateSplitting`**
  (`src/run/java/ch6/RunnerPlotErrorState10VersusNstepsAndLearningRateSplitting.java`)
  Repeatedly trains `TrainerStateActionControlDuringEpisode` on the
  splitting path for several combinations of backup horizon and learning
  rate, plotting mean/std of the average return as error bands — a
  hyperparameter sweep over `N_STEPS` and `LEARNING_RATES`.

## Main Code

### Domain

`chapters.ch6.domain` is the generic multi-step framework, independent of
any specific environment:

* `agent.AgentGridMultiStepI` — the interface implemented by every
  chapter-6 agent (`chooseAction`, `fit`, `calculateValueTarget`, `read`,
  `getMemory`). It mirrors `core.gridrl.AgentGridI` from chapter 4 but adds
  `calculateValueTarget(MultiStepResultGrid)` for the n-step return.
* `trainer_dep.core` — `TrainerDependenciesMultiStep` (the dependency
  bundle: agent, environment, `TrainerParametersMultiStepGrid`, start-state
  supplier, plus derived decaying learning-rate/exploration schedules and a
  step counter), `TrainerI` (marker interface with `train()`), and
  `ReturnCalculator` (computes the n-step discounted return `G` at a given
  time index, including the bootstrapped value of the state/action `n`
  steps ahead if the episode hasn't ended).
* `trainer_dep.episode_generator` — `EpisodeGeneratorGrid` (runs a full
  Sarsa episode against the environment/agent and returns the list of
  `core.gridrl.ExperienceGrid`) and `EpisodeInfo` (chapter-6-specific
  episode helper: rewards in a window, future state/action lookup for a
  given index, first-visit check — richer than the shared
  `core.gridrl.EpisodeGridInfo` used elsewhere in the chapter).
* `trainer_dep.result_generator` — `MultiStepResultGrid` /
  `MultiStepResultsGrid` (the per-step n-step target: state, action, summed
  reward, and optional future state/action) and
  `MultiStepResultsGeneratorGrid` (turns a raw experience list into a list
  of these per-step results, used by the "after episode" and state-predictor
  trainers).
* `trainers.after_episode.TrainerStateActionControlAfterEpisode` — n-step
  Sarsa control that generates a whole episode first, then applies
  first-visit updates for every step.
* `trainers.during_episode` — `TrainerStateActionControlDuringEpisode` (the
  during-episode variant of the same algorithm), plus its two helpers
  `ExperienceListCreator` (steps the environment while updating memory
  online, tau steps behind the current step) and `MultiStepMemoryUpdater`
  (applies one memory update at a given `tau` using `ReturnCalculator`).
* `trainers.state_predictor.TrainerStatePredictor` — multi-step **state
  prediction** (not control): fits state-action values toward the n-step
  return under a fixed exploring policy, optionally driving an
  `animation.AnimationGridMultiStepI` (`AnimationGridMsEmpty` is the no-op
  default).

### Implem (`splitting`, `treasure`) and Factory

`chapters.ch6.implem` provides one agent implementation per reused
environment plus the factories that wire dependencies together. There is
no `chapters.ch6.domain.environment` package with its own environment
classes — a placeholder file at
`src/main/java/chapters/ch6/domain/environment/read_me` states this
explicitly ("Used environments for multi-step problems are from other
packages").

* **`implem.splitting.agent`** — three `AgentGridMultiStepI` implementations
  for the splitting path, all backed by `core.gridrl.StateActionMemoryGrid`
  and chapter 3's `InformerSplitting`:
  `AgentGridMultiStepBestActionSplitting` (a fixed rule-based policy: go
  east, or north at the split cell `(2,1)`), its subclass
  `AgentGridMultiStepRandomActionSplitting` (randomizes north/south at the
  split), and `AgentGridMultiStepLearnerPolicySplitting` (a real learning
  agent using `core.gridrl.ActionSelectorGrid` for epsilon-greedy action
  choice). `StartStateGridSupplierMostLeftSplitting` supplies the fixed
  start state `(0,1)`.
* **`implem.treasure.agent.AgentGridMultiStepTreasure`** — the learning
  agent used for the Treasure examples, structurally identical to
  `AgentGridMultiStepLearnerPolicySplitting` but built from chapter 4's
  `EnvironmentParametersTreasure` / `InformerTreasure`.
* **`implem.factory.AgentGridMultiStepFactory`** — builds each of the four
  agents above with their `core.gridrl.AgentGridParameters` (discount
  factor, `tdMax`).
* **`implem.factory.TrainerDependenciesFactorySplitting`** — builds
  `TrainerDependenciesMultiStep` for the splitting examples
  (`givenOptimalPolicySplitting`, `givenRandomPolicySplitting`,
  `learnPolicySplittingAfterEpis`, `learnPolicySplittingDuringEpis`),
  constructing chapter 3's `EnvironmentSplittingPath` directly from
  `chapters.ch3.factory.EnvironmentParametersSplittingFactory.produce()`.
* **`implem.factory.TrainerDependenciesFactoryTreasure`** — builds
  `TrainerDependenciesMultiStep` for the Treasure example (`treasure(...)`),
  constructing chapter 4's `EnvironmentTreasure` directly from
  `chapters.ch4.implem.treasure.factory.EnvironmentParametersTreasureFactor.produce()`
  and reusing `chapters.ch4.implem.treasure.start_state_suppliers.StartStateSupplierTreasureMostLeft`.

### Animation

`chapters.ch6.implem_animation.AnimationSplit` implements
`domain.animation.AnimationGridMultiStepI` for the splitting problem: it
draws the agent's position and a value heat map using the shared
`core.animation` graphics kit. `AnimationSplit.create(...)` casts its
`EnvironmentGridI` argument to chapter 3's `EnvironmentSplittingPath`, so it
is tied specifically to that environment (its `empty()` factory method
falls back to chapter 4's `InformerTreasure.empty()` purely as an unrelated
placeholder informer, not a real dependency on the Treasure environment).

### Plotting

`chapters.ch6.plotting` contains chapter-level helpers:

* `GridAgentPlotterMultiStep` — extends the shared
  `core.plotting_rl.chart.GridAgentPlotter` (from chapter 4) so the same
  grid/heat-map plotting code can read values from a multi-step agent.
* `ProgressMeasureExtractorMultiStep` / `ProgressMeasuresExtractorDuring` —
  turn an episode's experiences (and, for the after-episode trainer, its
  `MultiStepResultsGrid`) into `core.plotting_rl.progress_plotting.ProgressMeasures`
  (return, step count, average TD error) for the after-episode and
  during-episode trainers respectively.
* `PlottingFactoryMultiStep` — builds `PlotSettings` for line/error-band
  plots (e.g. the value-vs-horizon and return-vs-horizon sweeps).

## Shared Code

Chapter 6 relies heavily on shared code rather than reimplementing it:

* `chapters.ch3.implem.splitting_path_problem.EnvironmentSplittingPath` and
  `chapters.ch3.factory.EnvironmentParametersSplittingFactory` — the
  splitting-path grid itself; chapter 6 reuses it unchanged for its
  splitting examples (see `docs/chapter-03.md`).
* `chapters.ch4.implem.treasure.core.EnvironmentTreasure`,
  `chapters.ch4.implem.treasure.factory.EnvironmentParametersTreasureFactor`,
  and `chapters.ch4.implem.treasure.start_state_suppliers.StartStateSupplierTreasureMostLeft`
  — the Treasure grid, reused unchanged for chapter 6's treasure examples
  (see `docs/chapter-04.md`).
* `core.gridrl` — `StateGrid`, `ActionGrid`, `StateActionGrid`,
  `ExperienceGrid`, `EnvironmentGridI`, `StartStateGridSupplierI`,
  `StateActionMemoryGrid`, `ActionSelectorGrid`, `AgentGridParameters`,
  `EpisodeGridInfo` — the same grid-world foundation used by chapters 3 and
  4. Chapter 6 does not add new grid primitives; it only adds the
  multi-step training logic on top.
* `core.plotting_rl.chart.GridAgentPlotter` and
  `core.plotting_rl.progress_plotting` (`RecorderProgressMeasures`,
  `PlotterProgressMeasures`, `ProgressMeasureEnum`, `ProgressMeasures`) —
  the same progress-recording and heat-map/line-chart plotting code used by
  chapters 3–5.
* `core.foundation.gadget.math.LogarithmicDecay` — the decaying
  learning-rate/exploration schedule, used the same way as in chapters 3
  and 5.
* `core.plotting_core.plotting_2d.ErrorBandCreator` and
  `core.plotting_rl.progress_plotting.ErrorBandSaverAndPlotter` — used by
  the hyperparameter-sweep runner to plot mean/std error bands.

Conversely, chapter 6's own agents and multi-step trainer framework are
used forward by chapter 7: see `docs/chapter-04.md` for the note on
`chapters.ch7.factory.TrainerDependencySafeFactory` also reusing the
Treasure environment, and inspect `src/main/java/chapters/ch7` if
cross-checking further reuse of chapter 6 code specifically.

## Code Flow

```
run (RunnerTrainerStateActionControlAfterEpisodeSplitting / ...Treasure, src/run/java/ch6)
  -> factory (TrainerDependenciesFactorySplitting / TrainerDependenciesFactoryTreasure)
    -> reused environment (ch3.EnvironmentSplittingPath / ch4.EnvironmentTreasure)
    -> implem agent (AgentGridMultiStep*Splitting / AgentGridMultiStepTreasure)
  -> domain.trainer_dep.core.TrainerDependenciesMultiStep  (bundles agent, env, params, decays)
  -> domain.trainers.after_episode / during_episode / state_predictor  (the training loop)
    -> domain.trainer_dep.episode_generator.EpisodeGeneratorGrid  (or inline stepping)
    -> domain.trainer_dep.core.ReturnCalculator  (n-step return)
    -> agent.fit(...)  (memory update)
  -> plotting (GridAgentPlotterMultiStep, PlotterProgressMeasures)
```

Each runner picks one of the three trainers (`TrainerStateActionControlAfterEpisode`,
`TrainerStateActionControlDuringEpisode`, or `TrainerStatePredictor`),
builds it from a `TrainerDependenciesMultiStep` produced by one of the two
factories, calls `train()`, and then plots state values/policy and the
recorded progress measures.

## Where to Start

Start with **`RunnerTrainerStateActionControlAfterEpisodeSplitting`**
(`src/run/java/ch6/RunnerTrainerStateActionControlAfterEpisodeSplitting.java`).
It uses the smaller, easier-to-follow "after episode" trainer
(`TrainerStateActionControlAfterEpisode`) on the already-familiar
splitting-path environment from chapter 3, and its factory method
(`TrainerDependenciesFactorySplitting.learnPolicySplittingAfterEpis`) shows
clearly how chapter 6 assembles a `TrainerDependenciesMultiStep` around a
reused environment. From there,
`RunnerStateActionControlDuringEpisodeSplitting` shows the during-episode
variant on the same problem, and the Treasure runners show the same
framework applied to chapter 4's environment.
