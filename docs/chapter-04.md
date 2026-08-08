# Chapter 4

## Overview

Chapter 4 covers one-step temporal-difference control (Q-Learning and
Sarsa) on grid-world environments. It has three independent scenarios,
each its own small grid problem:

* **Cliff Walk** — a corridor with a row of "cliff" states along the
  bottom that end the episode with a large penalty.
* **Treasure** — a maze-like grid with walls, fail cells, and two goal
  cells giving different rewards (a "small" and a "big" treasure).
* **Blocked Road Lane** — a car driving forward along lanes, choosing to
  change lane (N/S) or continue straight (E), with a fail state
  representing a blocked lane.

Runnable examples live in `src/run/java/ch4`. The corresponding
implementation lives in `src/main/java/chapters/ch4`, split into a shared
`domain` package (agents, trainers, animation interface), one `implem`
subpackage per scenario (`cliff_walk`, `treasure`, `blocked_road_lane`),
an `implem_animation` package with per-scenario live animations, and a
`plotting` package. Tests live in `src/test/java/chapters/ch4`.

For general repository layout and running instructions, see
`docs/CODE_ORGANIZATION.md` and `docs/HOW_TO_USE.md`. Chapter 3's
`core.gridrl` foundation (`StateGrid`, `ActionGrid`, `EnvironmentGridI`,
etc., see `docs/chapter-03.md`) is used directly and extensively here.

## Runnable Examples

* **`RunnerTrainersCliff`**
  (`src/run/java/ch4/RunnerTrainersCliff.java`)
  Trains both a Q-Learning and a Sarsa agent on the Cliff Walk
  environment and plots policy, state values, and progress measures for
  each. Uses `chapters.ch4.implem.cliff_walk.factory.CliffRunnerFactory`
  and `chapters.ch4.plotting.GridPlotShowAndSave`.

* **`RunnerTrainerTreasure`**
  (`src/run/java/ch4/RunnerTrainerTreasure.java`)
  Trains two Q-Learning agents on the Treasure environment, one with low
  and one with high exploration, and plots the results for both. Uses
  `chapters.ch4.implem.treasure.factory.TreasureRunnerFactory`.

* **`RunnerTrainerRoad`**
  (`src/run/java/ch4/RunnerTrainerRoad.java`)
  Trains four Q-Learning variants on the Blocked Road Lane environment:
  high learning rate, low learning rate, high learning rate with a
  different discount factor (`0.9`), and a version with a stochastic
  (random) fail reward. Uses
  `chapters.ch4.implem.blocked_road_lane.factory.RoadRunnerFactory`.

* **`RunnerPlotterGridEnvironment`**
  (`src/run/java/ch4/RunnerPlotterGridEnvironment.java`)
  A quick way to visualize the raw layout (walls, terminal, and fail
  cells) of any of the three environments before training, by switching
  the `INDEX_ENVIRONMENT_TO_SHOW` field. Builds each environment directly
  from its `implem.*.core` and `implem.*.factory` classes and plots it
  with `chapters.ch4.plotting.GridEnvironmentPlotter`.

* **`RunnerTrainerRoadAnimation`** and **`RunnerTrainerTreasureAnimation`**
  (`src/run/java/ch4/RunnerTrainerRoadAnimation.java`,
  `RunnerTrainerTreasureAnimation.java`)
  Run a single Q-Learning training session with a live Swing animation
  showing the environment stepping and the agent's learned values
  updating episode by episode. Use
  `chapters.ch4.domain.trainer.TrainerOneStepTdQLearning` directly with
  `chapters.ch4.implem_animation.AnimationRoad` /
  `chapters.ch4.implem_animation.AnimationTreasure`.

## Main Code

### Domain

`chapters.ch4.domain` holds the algorithm code shared by all three
scenarios:

* `agent.AgentQLearningGrid` / `agent.AgentSarsaGrid` — implement
  `core.gridrl.AgentGridI`; both wrap a `core.gridrl.StateActionMemoryGrid`
  and `core.gridrl.ActionSelectorGrid`, differing only in how they compute
  the TD target (`max` over next actions for Q-Learning, the actually
  chosen next action for Sarsa).
* `trainer.TrainerOneStepTdQLearning` / `trainer.TrainerOneStepTdSarsa` —
  implement `core.gridrl.TrainerGridI`; run the episode loop (choose
  action, step environment, build an `ExperienceGrid`, fit the agent's
  memory, record progress) using a `core.gridrl.TrainerGridDependencies`
  bundle.
* `animation.AnimationGridI` / `animation.AnimationDummy` — a small
  interface (`start`, `postStep`, `postEpisode`) that lets a trainer
  optionally drive a live visualization; `AnimationDummy` is the no-op
  implementation used by the non-animated runners.

### Implem (per-scenario environments)

Each scenario under `chapters.ch4.implem.<scenario>` follows the same
`core` / `factory` / `start_state_suppliers` structure:

* **`cliff_walk`** — `core.EnvironmentCliff` implements
  `core.gridrl.EnvironmentGridI`; a step off the grid is clipped back into
  bounds, and landing on a cliff cell yields a large negative terminal
  reward. `core.EnvironmentParametersCliff` and `core.InformerCliff`
  describe/answer questions about the grid (bounds, walls, terminal/fail
  states). `factory.FactoryEnvironmentParametersCliff` builds the
  11x4 grid with a row of fail states along `y=0`.
  `factory.AgentGridParametersFactoryCliff` and
  `factory.TrainerParametersFactoryCliff` provide agent/trainer
  hyperparameters. `factory.CliffRunnerFactory` wires everything together
  for the runner. Start states come from
  `start_state_suppliers.StartStateSupplierCliffXis0RandomY` (used by the
  factory); `StartStateSupplierCliffLowerLeft` and
  `StartStateSupplierCliffRandomNotTerminal` exist as alternatives but are
  not wired into the main runner.

* **`treasure`** — `core.EnvironmentTreasure` additionally blocks moves
  into wall cells (the agent stays in place). `core.EnvironmentParametersTreasure`
  / `core.InformerTreasure` describe the grid, including two differently
  rewarded goal cells. `factory.EnvironmentParametersTreasureFactor`
  builds the 10x4 maze layout (walls, fail cells, small/big treasure).
  `factory.FactoryAgentGridParametersTreasure` and
  `factory.FactoryTrainerParametersTreasure` provide the low/high
  exploration parameter sets used by the runner.
  `factory.TreasureRunnerFactory` wires dependencies together. Start
  states come from `start_state_suppliers.StartStateSupplierTreasureMostLeft`;
  `StartStateSupplierRandom` and `StartStateSupplierPositionGiven` are
  alternative suppliers (the latter is used by a chapter 7 test, not by
  this chapter's own runners).

* **`blocked_road_lane`** — `core.EnvironmentRoad` always moves the car
  one step forward in `x` while `y` (lane) changes according to the
  chosen action; `core.EnvironmentParametersRoad` / `core.InformerRoadParams`
  describe the grid and reward parameters, including a stochastic fail
  reward (`core.foundation.gadget.math.MeanAndStd`).
  `factory.FactoryEnvironmentParametersRoad` provides both a fixed-reward
  and a random-reward variant of the environment.
  `factory.AgentGridParametersFactoryRoad` and
  `factory.TrainerParametersFactoryRoad` provide the parameter variants
  used by the runner (high/low learning rate, discount `0.9`).
  `factory.RoadRunnerFactory` wires everything, including a dedicated
  `animation(...)` method used by `RunnerTrainerRoadAnimation`. Start
  states come from
  `start_state_suppliers.StartStateSupplierRoadMostLeftAnyLane`;
  `StartStateSupplierRoadXPos1AnyLane` is an alternative, tested directly
  but not wired into the main runner.

### Animation

`chapters.ch4.implem_animation` contains one `AnimationGridI`
implementation per animated scenario: `AnimationRoad` (draws a car moving
along lanes plus a heat map of learned action/state values) and
`AnimationTreasure` (draws the seeker's position, walls, and fail cells,
plus the same kind of value heat maps). Both are built on the shared
`core.animation` graphics kit.

### Plotting

`chapters.ch4.plotting` contains chapter-level plot helpers built on
shared charting code:

* `GridEnvironmentPlotter` — renders the static layout of a grid
  environment (walls `X`, terminal `T`, fail `F`) as a heat map, using
  `core.plotting_rl.chart.GridPlotHelper`.
* `GridPlotShowAndSave` — the common post-training plotting step used by
  all three "non-animated" runners: saves/shows the learned policy and
  state-value heat maps via `core.plotting_rl.chart.GridAgentPlotter`,
  and the return/TD-error progress curves via
  `core.plotting_rl.progress_plotting.PlotterProgressMeasures`.

## Shared Code

Chapter 4 relies on the following shared (`core`) building blocks:

* `core.gridrl` — the grid-world foundation introduced with chapter 3:
  `StateGrid`, `ActionGrid`, `StepReturnGrid`, `ExperienceGrid`,
  `EnvironmentGridI`, `InformerGridParamsI`, `StartStateSupplierGridI`,
  `AgentGridI`, `TrainerGridI`, `TrainerGridDependencies`,
  `StateActionMemoryGrid`, and `ActionSelectorGrid`. All three of chapter
  4's environments and both its agents/trainers are built directly on
  these interfaces.
* `core.animation` — the generic live-plotting/graphics kit
  (`AnimationKit`, `GfxComponentFactory`, `GraphicsDto`, `LineSegment`,
  etc.) used by both `AnimationRoad` and `AnimationTreasure`.
* `core.plotting_rl` — `chart.GridAgentPlotter` and `chart.GridPlotHelper`
  (grid/heat-map charting) and `progress_plotting.RecorderProgressMeasures`
  / `PlotterProgressMeasures` (recording and plotting return/TD-error
  curves over episodes).
* `core.foundation.config.ConfigFactory` — resolves where generated charts
  are saved (`pathPicsConfig().ch4()`) and reads chart/animation settings.
* `core.foundation.gadget.training.ValueCalculator` — computes the TD
  target value shared by both `AgentQLearningGrid` and `AgentSarsaGrid`.

Conversely, chapter 4's own Treasure environment is reused by later
chapters rather than being self-contained to this chapter:
`chapters.ch4.implem.treasure.core.EnvironmentTreasure`,
`EnvironmentParametersTreasureFactor`, and
`StartStateSupplierTreasureMostLeft` are used directly by chapter 6's
multi-step agent example
(`chapters.ch6.implem.factory.TrainerDependenciesFactoryTreasure`,
`chapters.ch6.implem.factory.AgentGridMultiStepFactory`) and by chapter
7's safety-focused trainer
(`chapters.ch7.factory.TrainerDependencySafeFactory`). Readers who want to
see the same Treasure grid used with different algorithms can follow
these references forward into chapters 6 and 7.

## Code Flow

```
run (RunnerTrainersCliff / RunnerTrainerTreasure / RunnerTrainerRoad, src/run/java/ch4)
  -> factory (chapters.ch4.implem.<scenario>.factory: *RunnerFactory, FactoryEnvironmentParameters*, AgentGridParametersFactory*, TrainerParametersFactory*)
    -> implem/core (chapters.ch4.implem.<scenario>.core: Environment*, Informer*)
      -> core.gridrl (StateGrid, ActionGrid, EnvironmentGridI, TrainerGridDependencies)
    -> domain (chapters.ch4.domain.agent: AgentQLearningGrid / AgentSarsaGrid)
      -> domain.trainer (TrainerOneStepTdQLearning / TrainerOneStepTdSarsa) -- the episode training loop
  -> plotting (chapters.ch4.plotting.GridPlotShowAndSave)
    -> core.plotting_rl (GridAgentPlotter, PlotterProgressMeasures)
```

Each `*RunnerFactory` (`CliffRunnerFactory`, `TreasureRunnerFactory`,
`RoadRunnerFactory`) builds one or more `TrainerGridDependencies` bundles
(environment, agent, start-state supplier, trainer parameters), wraps them
in trainers, and the runner calls `train()` on all of them before handing
the resulting recorders to `GridPlotShowAndSave` for plotting. The two
animated runners skip the factory's `Trainers` bundle and instead call
`TrainerOneStepTdQLearning.of(...)` directly so they can pass a live
`AnimationGridI` (`AnimationRoad`/`AnimationTreasure`) into `train(...)`.

## Where to Start

Start with **`RunnerPlotterGridEnvironment`**
(`src/run/java/ch4/RunnerPlotterGridEnvironment.java`) to see the raw
layout of all three environments (cliff, treasure, road) side by side
before diving into training. Then move to **`RunnerTrainersCliff`**
(`src/run/java/ch4/RunnerTrainersCliff.java`), the simplest of the three
training runners, to see Q-Learning and Sarsa trained and compared on the
same grid — a good entry point into `TrainerOneStepTdQLearning`,
`TrainerOneStepTdSarsa`, and the shared `core.gridrl` types they depend
on.
