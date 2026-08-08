# Chapter 8

## Overview

Chapter 8 implements a continuing (non-episodic) reinforcement learning
problem: an EV/car parking lot that accepts or rejects arriving vehicles to
maximize the long-run average fee income. The agent learns state-action
values with a differential (average-reward) update rule: instead of
bootstrapping toward a discounted return, it bootstraps toward
`reward - averageReward + gamma * nextValue`, while a second, more slowly
decaying learning rate tracks the running average reward itself. This is
the parking-lot / average-reward example associated with the chapter.

Runnable examples live in `src/run/java/ch8`. The corresponding
implementation lives in `src/main/java/chapters/ch8`, split into `domain`
(agent, environment, and trainer code), `factory` (dependency and parameter
assembly), and `plotting` (result visualization). Tests live in
`src/test/java/chapters/ch8`.

For general repository layout and running instructions, see
`docs/CODE_ORGANIZATION.md` and `docs/HOW_TO_USE.md`.

## Runnable Examples

* **`RunnerTrainerParking`**
  (`src/run/java/ch8/RunnerTrainerParking.java`)
  The single runnable example for this chapter. It builds a
  `TrainerDependenciesParking` bundle (agent, environment, trainer
  parameters, start-state supplier, and decaying learning-rate schedules),
  runs `TrainerParking.train()` for one long continuing run, and then
  produces three plots: the training-progress curves (average reward and
  average occupancy), a heat map of the learned state-action values/actions
  by (fee, occupancy), and a line chart of the "accept advantage"
  (accept-value minus reject-value) versus occupancy for each fee level.
  It uses `chapters.ch8.factory.ParkingParametersFactory`,
  `chapters.ch8.factory.AgentParkingParametersFactory`,
  `chapters.ch8.factory.TrainerParametersFactory`,
  `chapters.ch8.factory.TrainerDepFactory`,
  `chapters.ch8.domain.trainer.core.TrainerParking`, and the three chapter-8
  plotters.

## Main Code

### Domain

`chapters.ch8.domain` is split into three areas:

* **`agent`**
  * `core.AgentParking` — chooses actions (epsilon-random exploration vs.
    greedy over learned action values) and fits its memory from an
    `ExperienceParking` via `fitMemory`.
  * `core.ExperienceParking` — a single-step transition record; its
    `deltaReward()` (`reward - rewardAverage`) is the key quantity used by
    the average-reward update.
  * `memory.AgentMemory` / `memory.StateActionParking` — a `HashMap`-based
    tabular state-action value store, keyed by `StateActionParking`, with a
    clipped TD-error update (`AgentParkingParameters.tdMax()` bounds the
    fitting error before applying the learning rate).
  * `param.AgentParkingParameters` — `tdMax`, `defaultValueStateAction`,
    `gamma`.
* **`environment`**
  * `core.EnvironmentParking` — the parking-lot transition model: an
    arriving car may be accepted/rejected (subject to available spots and
    the agent's action), occupied cars may depart, and a random fee level
    (`FeeEnum`) is assigned on arrival. `core.StateParking` /
    `core.VariablesParking` hold occupancy count, current fee, and step
    count; `core.ActionParking` is `ACCEPT`/`REJECT`; `core.FeeEnum` is
    `NotCharging`/`Charging`; `core.StepReturnParking` /
    `core.NewStateResult` carry step results.
  * `param.ParkingParameters` — number of spots, fee values, arrival/
    departure/charging-request probabilities, and `maxSteps` (used only as
    an artificial cutoff for a single training run, not a "true" episode
    boundary — the task is otherwise continuing).
  * `startstate_supplier.StartStateSupplier` (enum) /
    `StartStateSupplierI` — supplies the initial state (e.g.
    `ZEROOCCUP_RANDOMFEE`: zero occupancy, random fee).
* **`trainer`**
  * `core.TrainerParking` — the training loop: each step it computes the
    current exploration probability and learning rate (from decaying
    schedules), steps the environment, updates the agent's memory, updates
    the running average-reward statistics, and records a measurement.
  * `core.TrainerDependenciesParking` — a builder record bundling the
    agent, environment, trainer parameters, start-state supplier, a
    `CpuTimer`, and three `LogarithmicDecay` schedules (exploration
    probability, action-value learning rate, average-reward learning rate).
  * `core.TrainingStats` — accumulates reward sum, average-reward delta,
    and occupancy statistics (`Accumulator`, `DescriptiveStatistics`) over
    the run.
  * `param.TrainerParametersParking` — start/end pairs for the three
    decaying schedules above.

### Factory

`chapters.ch8.factory` assembles parameters and dependencies:

* `ParkingParametersFactory`, `AgentParkingParametersFactory`,
  `TrainerParametersFactory` — build the default `forTest()` / `forRunning()`
  parameter sets for the environment, agent, and trainer respectively.
* `TrainerDepFactory` — builds the full `TrainerDependenciesParking`
  (agent, environment, timer, and the three `LogarithmicDecay` schedules)
  from the parameter sets above.
* `AgentMemoryFactory` — fills an `AgentMemory` with mocked state-action
  values for all state-action pairs; used to build fixture memories in
  tests rather than by `RunnerTrainerParking`.

### Plotting

`chapters.ch8.plotting` turns training output into charts:

* `RecorderTrainerParking` / `MeasuresParkingTraining` /
  `MeasuresParkingTrainingEnum` — record one `MeasuresParkingTraining`
  snapshot (step, cumulative reward, average reward, average occupancy) per
  training step, and expose named trajectories for plotting.
* `TrainerPlotter` — plots the average-reward and average-occupancy curves
  over training via `ErrorBandPlotterParking`.
* `ErrorBandPlotterParking` — builds a filtered error-band chart from a
  recorded trajectory.
* `AgentParkingMemoryHeatMapPlotter` — renders the learned action (accept/
  reject) and value tables as text-annotated heat maps, one row per fee
  level, one column per occupancy count.
* `AgentParkingMemoryCurvePlotter` — plots the "accept advantage"
  (accept value minus reject value) as a function of occupancy, one line
  per fee level.

## Shared Code

Chapter 8 relies on the following shared (`core`) building blocks:

* `core.foundation.gadget.math.LogarithmicDecay` — the decaying schedule
  used for the exploration probability and both learning rates
  (`TrainerDepFactory`); also used by chapters 3, 5, 6, 10, and 12.
* `core.foundation.gadget.math.Accumulator`, `core.foundation.gadget.cond.Counter`,
  `core.foundation.gadget.timer.CpuTimer`, `core.foundation.util.math.MathUtil`
  (used for clipping the TD error) — generic utilities also used elsewhere
  in the codebase.
* `core.foundation.config.ConfigFactory`, `core.foundation.config.PathAndFile`,
  `core.foundation.config.PlotConfig` — resolve chart-saving locations
  (`pathPicsConfig().ch8()`) and plot sizing, shared across all chapters.
* `core.plotting_core` (`ChartSaver`, `ManyLinesChartCreator`,
  `HeatMapWithStringTextInCellsCreator`, `ErrorBandCreator`) and
  `core.plotting_rl.progress_plotting.ErrorBandSaverAndPlotter` /
  `core.plotting_rl.chart.StringTextChartFactory` — the generic charting
  layer reused by several chapters' error-band and text-annotated heat-map
  plots (e.g. chapters 3, 9, 10, 12 use the same heat-map creator).

Chapter 8's own domain code (`AgentParking`, `EnvironmentParking`,
`StateParking`, `AgentMemory`, and the trainer) is specific to the parking
problem and is not reused by other chapters. Unlike the grid-world chapters
(e.g. chapter 3, chapter 6), it does not build on `core.gridrl` — the
parking state (occupancy count + fee) is not modeled as a grid.

## Code Flow

```
run (RunnerTrainerParking, src/run/java/ch8)
  -> factory (ParkingParametersFactory / AgentParkingParametersFactory / TrainerParametersFactory)
    -> factory.TrainerDepFactory
      -> domain.agent.core.AgentParking.of(...) + domain.environment.core.EnvironmentParking.of(...)
      -> core.foundation.gadget.math.LogarithmicDecay (x3: exploration, action-value LR, avg-reward LR)
  -> domain.trainer.core.TrainerParking.train()
    -> steps the environment, updates AgentMemory (average-reward TD update), records TrainingStats
    -> plotting.RecorderTrainerParking accumulates MeasuresParkingTraining per step
  -> plotting (TrainerPlotter, AgentParkingMemoryHeatMapPlotter, AgentParkingMemoryCurvePlotter)
    -> core.plotting_core / core.plotting_rl -> ChartSaver
```

## Where to Start

Start with **`RunnerTrainerParking`**
(`src/run/java/ch8/RunnerTrainerParking.java`). It is the only runnable
example in the chapter and exercises the whole pipeline end to end: building
parameters and dependencies, running the average-reward training loop in
`TrainerParking`, and producing all three chapter-8 plots. From there,
`chapters.ch8.domain.trainer.core.TrainerParking.train()` is the best place
to see the average-reward update itself, and
`src/test/java/chapters/ch8/TestCorrectPolicyWhenTrained.java` shows the
expected learned policy (which occupancy/fee combinations should be
rejected) as a concrete, checkable outcome of training.
