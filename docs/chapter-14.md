# Chapter 14

## Overview

Chapter 14 implements a planning agent that plays Pong. It combines online
rollout-based planning (a shallow, MCTS-like planner) with a fitted "long
memory" — a radial-basis-function (RBF) network that approximates the value
of states — so that shallow rollouts can be extended by a learned value
estimate instead of being rolled out to the end of the episode.

The code lives under `src/main/java/chapters/ch14` and its runnable
examples under `src/run/java/ch14`. Chapter 14 is the final chapter and
reuses several shared `core` packages built up over previous chapters
(RBF networks, math/config utilities, plotting), rather than a specific
prior chapter's environment.

## Runnable Examples

* `src/run/java/ch14/RunnerShallowPlannerFittedMemory.java` — the main
  example. It:
  1. Builds `TrainerDependencies` via `FactoryDependencies.forRunning()`.
  2. Runs the planner once with an empty (untrained) long memory and
     animates/records the result (`execution_empty_memory`).
  3. Trains the long memory with `Trainer.train()`.
  4. Plots the fitted memory as a heat map (`long_memory.png`).
  5. Runs the planner again, now backed by the fitted memory, and
     animates/records the result (`execution_fitted_memory`).
  Uses `chapters.ch14.factory.FactoryDependencies`,
  `chapters.ch14.factory.FactoryPlanner`,
  `chapters.ch14.domain.trainer.Trainer`,
  `chapters.ch14.implem.pong.ExecutorPong`, and
  `chapters.ch14.pong_animation.PongGraphicsServer`.

* `src/run/java/ch14/RunnerPongViewer.java` — a companion viewer process.
  It opens a Swing window (`PongGraphicsViewer`) that connects over a
  socket to the `PongGraphicsServer` started by
  `RunnerShallowPlannerFittedMemory`, so the game can be watched live.
  This needs to be started as a separate run so it can connect while
  the other runner is executing (see the class comment "Skip animation
  by comment line with server=..." in `RunnerShallowPlannerFittedMemory`
  for how to disable animation).

## Main Code

### Domain

`chapters/ch14/domain` holds the generic planning/training abstractions,
independent of the Pong specifics:

* `environment` — `EnvironmentI` (a step-based environment interface),
  `StepReturn`, `Experience`.
* `planner` — `Planner`, `ActionSelectorI`, `ActionSequenceRoller`,
  `BestRollout`, `RollingResult`, `PlanningStatus`. `Planner` performs
  repeated rollouts (via `ActionSequenceRoller`) until a stopping
  predicate is met (minimum rollout count, then a time-based fallback),
  and tracks the best rollout found.
* `trainer` — `Trainer` (runs training episodes, planning a step, taking
  it, fitting the long memory from a replay-buffer mini-batch, and
  recording measures), `TrainerDependencies` (the dependency bundle
  passed around), `ReplayBuffer`, `MiniBatchAdapterI`.
* `interfaces` — `LongMemory` (read/write/fit contract for the fitted
  value memory), `ExecutorI`, `RecorderI`, `StateInterpreterI`.
* `settings` — `TrainerSettings`, `PlanningSettings`, `MemorySettings`.

### Implem

`chapters/ch14/implem` holds the concrete Pong problem, split into two
subpackages (matching the `implem`-per-scenario pattern used in other
chapters, though here both subpackages describe the same Pong problem
rather than separate scenarios):

* `implem/pong` — the Pong environment and its state/action types:
  `EnvironmentPong` (physics step: paddle move, ball reflection off
  walls/paddle, reward), `StatePong`, `StateLongPong` (the reduced state
  used by the long memory), `ActionPong`, `FiniteState`, `PosXy`,
  `VelocityXy`, `PongSettings`, `ActionSelectorPong` (random rollout
  action-sequence generator), `ExecutorPong` (runs evaluation episodes,
  optionally streaming frames to the animation server).
* `implem/pong_memory` — the long-memory side: `LongMemoryRbf` (an RBF
  network wrapping `core.nextlevelrl.radial_basis.RbfNetwork`),
  `LongMemoryZero` (a no-op/zero memory, useful as a baseline or for
  tests), `StateInterpreterPong`, `StateAdapterPong`,
  `MiniBatchAdapterPong` (converts replay-buffer experiences into
  `TrainData` for fitting), `BallHitFloorCalculator` and
  `BallHitFloorResult` (compute the time until the ball reaches the
  floor, used to build the reduced `StateLongPong`).

### Factory

`chapters/ch14/factory` builds and wires the objects above:
`FactoryDependencies` (assembles a full `TrainerDependencies` for
`forTest()`/`forRunning()` use), `FactoryPlanner`, `FactoryPlanningSettings`,
`FactoryTrainerSettings`, `FactoryMemorySettings`, `FactoryPongSettings`,
`FactoryStatePong`, `FactoryPlotData` (grid data for the memory heat map).

### Plotting

`chapters/ch14/plotting` contains `Recorder`/`MeasuresCombLP`/
`MeasuresCombLPEnum` (episode measures such as `N_STEPS` and
`SUM_REWARDS`) and `RecorderPlotter`, which turns recorded measures into
stair-step XY charts saved via `core.plotting_core`. This chapter records
and plots its own measure type rather than reusing `core.plotting_rl`.

### Pong animation

`chapters/ch14/pong_animation` is specific to this chapter: it provides a
small client/server pair (`PongGraphicsServer`, `PongGraphicsViewer`) that
streams `PongGraphicsDTO` snapshots over a socket to a live Swing view
(`PongPanel`, `PongPanelsContentSetter`, `PongFrameCreator`,
`PanelProperties`), so a running planner/executor can be watched in real
time from a separate process.

## Shared Code

Chapter 14 reuses several `core` packages rather than implementing this
support itself:

* `core.nextlevelrl.radial_basis` — the RBF network (`RbfNetwork`,
  `Kernel`, `Kernels`, weight updates) used by `LongMemoryRbf`. The same
  package is also used by chapter 9 (`chapters/ch9/factory/Radial3dFactory`)
  and chapter 11 (`chapters/ch11/factory/RbfMemoryFactory`, plus the
  Lunar Lander actor/critic memories), so chapter 14's fitted memory
  builds on the same RBF machinery introduced earlier in the book.
* `core.foundation.config` — `ConfigFactory`, `PathAndFile`,
  `AnimationConfig`, `PlotConfig` for reading configuration and picture
  output paths (`pathpic.ch14`).
* `core.foundation.gadget.*` — `Counter` and `CpuTimer` (episode/step
  counters and timing), `TrainData` (training-data container used when
  fitting the RBF memory), math utilities (`MathUtil`), random helpers
  (`RandUtil`).
* `core.learningutils.MyRewardListUtils` — discounted-sum-of-rewards
  helper, used by `ActionSequenceRoller` to accumulate rollout returns.
* `core.plotting_core` — `ChartSaver`, `StairDataGenerator`,
  `HeatMapChartCreator` (`plotting_3d`), used by `RecorderPlotter` and by
  the memory heat-map plot in `RunnerShallowPlannerFittedMemory`.

## Code Flow

For `RunnerShallowPlannerFittedMemory`:

```
run (RunnerShallowPlannerFittedMemory, src/run/java/ch14)
  -> factory (FactoryDependencies, FactoryPlanner, FactoryPongSettings, ...)
    -> domain (Trainer, Planner, TrainerDependencies)
      -> implem/pong (EnvironmentPong, ActionSelectorPong, ExecutorPong)
      -> implem/pong_memory (LongMemoryRbf, MiniBatchAdapterPong, BallHitFloorCalculator)
        -> core.nextlevelrl.radial_basis (RbfNetwork)
    -> pong_animation (PongGraphicsServer) -> socket -> RunnerPongViewer
    -> plotting (Recorder, RecorderPlotter, FactoryPlotData)
      -> core.plotting_core (ChartSaver, HeatMapChartCreator)
```

At each planning step, `Planner.plan(...)` repeatedly calls
`ActionSelectorPong.selectActions(...)` to generate a random action
sequence and `ActionSequenceRoller.roll(...)` to simulate it through
`EnvironmentPong`, adding the long memory's value estimate for the final
state when the rollout does not terminate. The best rollout's first action
is executed, and `Trainer` periodically fits `LongMemoryRbf` from
mini-batches sampled from the `ReplayBuffer`.

## Where to Start

Start with `src/run/java/ch14/RunnerShallowPlannerFittedMemory.java`. It
exercises the full pipeline in one place — planning with an empty memory,
training the memory, plotting it as a heat map, and re-running the planner
with the fitted memory — and its `main` method is a good map of which
factory and domain classes are involved. Run
`src/run/java/ch14/RunnerPongViewer.java` alongside it (as a separate
process) to watch the Pong game live; comment out the `server = ...` line
in `RunnerShallowPlannerFittedMemory` to skip the animation if you only
want the training/plotting results.
