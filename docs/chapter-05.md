# Chapter 5

## Overview

Chapter 5 introduces Monte Carlo policy evaluation: instead of bootstrapping
from an estimated next-state value (as in chapter 3's TD-style fitting), the
code generates whole episodes and updates state (or state-action) values
from the observed return, using first-visit updates and a decaying learning
rate. The chapter defines a small, reusable Monte Carlo framework
(`EnvironmentMcI`, `PolicyMcI`, episode generation, first-visit policy
evaluation) and applies it to three example problems: a random walk, a dice
game, and the splitting-path grid world reused from chapter 3.

Runnable examples live in `src/run/java/ch5`. The corresponding
implementation lives in `src/main/java/chapters/ch5`, split into `domain`
(the Monte Carlo framework interfaces and evaluators), `implem` (the three
concrete problems: `dice`, `walk`, `splitting`, plus a small `converter`
package), and `factory` (dependency assembly and plotting helpers). Tests
live in `src/test/java/chapters/ch5`.

For general repository layout and running instructions, see
`docs/CODE_ORGANIZATION.md` and `docs/HOW_TO_USE.md`. For background on the
splitting-path environment reused here, see `docs/chapter-03.md`.

## Runnable Examples

* **`RunnerPolicyEvaluationWalk`**
  (`src/run/java/ch5/RunnerPolicyEvaluationWalk.java`)
  Runs Monte Carlo state-value evaluation on a random-walk problem
  (`chapters.ch5.implem.walk`) and plots the resulting state values as a
  text-annotated heat map. Uses `chapters.ch5.factory.WalkDependenciesFactory`,
  `chapters.ch5.domain.policy_evaluator.StatePolicyEvaluationMc`, and
  `chapters.ch5.factory.HeatMapWithTextFactoryWalk`. A good minimal entry
  point into the chapter's Monte Carlo machinery.

* **`RunnerPolicyEvaluationDice`**
  (`src/run/java/ch5/RunnerPolicyEvaluationDice.java`)
  Runs Monte Carlo **state-action** value evaluation on a simple dice game
  (throw vs. stop) using `chapters.ch5.implem.dice`, then plots both the
  learned state-action values (as two heat maps, one per action) and the
  moving-average TD-style fitting error over iterations. Uses
  `chapters.ch5.factory.DiceDependenciesFactory`,
  `chapters.ch5.domain.policy_evaluator.StateActionPolicyEvaluationMc`, and
  `chapters.ch5.factory.HeatMapWithTextFactoryDice`.

* **`RunnerPlotErrorVersusIterationForSplittingPath`**
  (`src/run/java/ch5/RunnerPlotErrorVersusIterationForSplittingPath.java`)
  Compares Monte Carlo evaluation against chapter 3's TD-style evaluation on
  the same splitting-path environment, plotting both fitting-error curves on
  one chart. Uses `chapters.ch5.factory.SplittingDependenciesFactory` (Monte
  Carlo side, via `chapters.ch5.implem.splitting`) together with chapter 3's
  `chapters.ch3.implem.splitting_path_problem.EnvironmentSplittingPath` and
  `chapters.ch3.policies.SplittingPathPolicyOptimal` (TD side). This is the
  example that most directly demonstrates the reuse relationship with
  chapter 3.

## Main Code

### Domain

`chapters.ch5.domain` defines the generic Monte Carlo abstractions used by
all three implementations:

* `environment` — `EnvironmentMcI`, `StateMcI`, `ActionMcI`, `ParametersMcI`,
  `StepReturnMc`, `ExperienceMc`, `StartStateSupplierI`: the interfaces and
  value objects describing a Monte Carlo environment and a single
  state/action/reward experience.
* `memory` — `StateMemoryMcI`, `StateActionMemoryMcI`, `StateActionMcI`:
  interfaces for reading/writing learned values, keyed either by state or by
  state-action pair.
* `policy` — `PolicyMcI`: a one-method interface (`chooseAction(state)`).
* `episode_generator` — `EpisodeGeneratorI` / `EpisodeGenerator`: runs a
  policy against an environment from a given start state until termination,
  collecting the resulting list of `ExperienceMc`.
* `policy_evaluator` — the evaluation loop itself:
  * `EvaluatorDependencies` — a builder-style record bundling the start-state
    supplier, episode generator, state and/or state-action memory, decaying
    learning rate, `EvaluatorSettings`, and an error list.
  * `EvaluatorSettings` — start/end learning rate, discount factor
    (`gamma`), iteration count, and an optional probability of taking a
    random action (used by the dice example's exploring policy).
  * `StatePolicyEvaluationMc` and `StateActionPolicyEvaluationMc` — run
    `nIterations` episodes; for each episode they walk backwards through the
    experiences, accumulate the discounted return, and apply a first-visit
    update (`ExperiencesInfo.isFirstVisit`) toward the observed return using
    the current decayed learning rate, recording the absolute fitting error.

### Implem (`walk`, `dice`, `splitting`, `converter`)

Each subpackage of `chapters.ch5.implem` implements the `domain` interfaces
for one concrete problem:

* **`walk`** — `EnvironmentWalk`, `StateWalk`, `ActionWalk`,
  `RandomWalkParameters`, `MemoryWalk`, `PolicyMcWalk`,
  `StartStateSupplierWalk`: a 1-D random walk with fail and terminal
  end-states, evaluated under a fixed policy.
* **`dice`** — `EnvironmentDice`, `StateDice`, `ActionDice`,
  `DiceParameters`, `StateActionMemoryDice`, `PolicyDice`,
  `StartStateSupplierDice`: a push-your-luck dice game (throw to add a
  random score or stop), evaluated with an epsilon-random exploring policy
  (`EvaluatorSettings.probRandomAction`) so state-action values can be
  learned for both actions.
* **`splitting`** — `EnvironmentSplittingMc`, `StateSplittingMc`,
  `ActionSplittingMc`, `PolicyMcSplittingOptimal`, `PolicyMcSplittingRandom`,
  `StartStateSupplierMostLeftSplitting`, `StartStateSupplierRandomSplitting`,
  and `SplittingPathAdapter`. This package does **not** reimplement the
  splitting-path grid; `SplittingPathAdapter` wraps chapter 3's
  `chapters.ch3.implem.splitting_path_problem.EnvironmentSplittingPath`,
  translating between chapter 5's Monte Carlo state/action types
  (`StateSplittingMc`, `ActionSplittingMc`) and chapter 3's grid types
  (`core.gridrl.StateGrid`, `core.gridrl.ActionGrid`). `EnvironmentSplittingMc`
  implements `EnvironmentMcI` purely by delegating each `step` call through
  this adapter.
* **`converter`** — `StateTypeConverter`, a small helper for converting
  between state representations.

### Factory

`chapters.ch5.factory` assembles the dependencies and plotting objects each
runner needs, one factory per problem:

* `WalkDependenciesFactory`, `DiceDependenciesFactory`,
  `SplittingDependenciesFactory` — each builds an `EvaluatorDependencies`
  instance from an environment, policy, memory, start-state supplier, and
  `EvaluatorSettings` (learning-rate schedule, discount factor, iteration
  count). `SplittingDependenciesFactory` additionally exposes an
  `optimalPolicy(settings)` method used to build the Monte Carlo side of the
  splitting-path comparison.
* `GridParametersWalkFactory` — builds the `RandomWalkParameters` for the
  walk example.
* `GridProperties` — a small helper describing a rectangular sub-grid (start
  row/col, row/col counts) used when laying out heat-map cells.
* `HeatMapWithTextFactoryWalk`, `HeatMapWithTextFactoryDice` — build
  text-annotated `HeatMapChart` objects from the learned `StateMemoryMcI` /
  `StateActionMemoryMcI`, using the shared
  `core.plotting_core.plotting_3d.HeatMapWithStringTextInCellsCreator`.

## Shared Code

Chapter 5 relies on the following shared (`core`) building blocks:

* `core.foundation.gadget.math.LogarithmicDecay` — the decaying
  learning-rate schedule used by `EvaluatorSettings.getDecayingLearningRate()`;
  also used by chapters 3, 6, 8, 10, and 12.
* `core.foundation.gadget.math.MovingAverage`, `core.foundation.gadget.cond.Counter`,
  `core.foundation.gadget.timer.CpuTimer` — generic utilities used to filter
  error curves for plotting, drive the iteration loop, and time the runs.
* `core.foundation.config.ConfigFactory`, `core.foundation.config.PathAndFile` —
  resolve chart-saving locations (`pathPicsConfig().ch5()`) and plot sizing
  settings, shared across all chapters.
* `core.plotting_core` (`ChartSaver`, `ManyLinesChartCreator`,
  `HeatMapWithStringTextInCellsCreator`) and `core.plotting_rl.chart.ManyLinesFactory`
  — the generic charting layer used for both the heat-map plots and the
  error-versus-iteration line charts.
* `core.gridrl` (`StateGrid`, `ActionGrid`, `StateValueMemoryGrid`) — used
  indirectly through `SplittingPathAdapter` and, in
  `RunnerPlotErrorVersusIterationForSplittingPath`, directly for the TD-style
  comparison side.

Conversely, chapter 5's own environment/policy code (`walk`, `dice`) is
specific to this chapter and is not reused elsewhere. The **splitting**
package is the exception: it reuses chapter 3's environment rather than
reimplementing it. See `docs/chapter-03.md` for the chapter-3 side of this
relationship, including reuse by chapter 6 as well.

## Code Flow

```
run (RunnerPolicyEvaluationWalk / RunnerPolicyEvaluationDice, src/run/java/ch5)
  -> factory (WalkDependenciesFactory / DiceDependenciesFactory)
    -> implem (EnvironmentWalk|EnvironmentDice, PolicyMcWalk|PolicyDice, memory)
    -> domain.episode_generator.EpisodeGenerator
  -> domain.policy_evaluator (StatePolicyEvaluationMc / StateActionPolicyEvaluationMc)
    -> generates episodes, applies first-visit MC updates with decaying learning rate
  -> factory (HeatMapWithTextFactoryWalk / HeatMapWithTextFactoryDice)
    -> core.plotting_core / core.plotting_rl -> ChartSaver
```

For the splitting-path comparison
(`RunnerPlotErrorVersusIterationForSplittingPath`), the same flow runs twice
in parallel: once through `chapters.ch5.factory.SplittingDependenciesFactory`
and `StatePolicyEvaluationMc` (Monte Carlo, via `SplittingPathAdapter`), and
once through chapter 3's `EnvironmentSplittingPath` and
`PolicyEvaluatorSplittingPath` (TD-style), with both error curves plotted on
the same chart.

## Where to Start

Start with **`RunnerPolicyEvaluationWalk`**
(`src/run/java/ch5/RunnerPolicyEvaluationWalk.java`). It is the simplest
end-to-end example: a small environment, a fixed policy, one factory call to
assemble dependencies, one call to `evaluate()`, and one heat-map plot. Once
the Monte Carlo framework (`domain.episode_generator`,
`domain.policy_evaluator`) is clear from this example, the dice example adds
state-action values and an exploring policy, and
`RunnerPlotErrorVersusIterationForSplittingPath` shows how the same
framework connects back to chapter 3's environment.
