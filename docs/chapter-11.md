# Chapter 11

## Overview

Chapter 11 implements an actor-critic agent for a 1-dimensional Lunar Lander
problem (position and speed only, controlled by a thrust force). The agent
uses radial basis function (RBF) networks for both actor (Gaussian policy:
mean and log-std) and critic (state value), trained with an n-step
(multi-step) advantage-actor-critic update. The chapter also explores how
the step horizon `n` affects training, and includes an animated,
sound-enabled visualization of a trained agent landing.

Runnable examples live in `src/run/java/ch11`. The corresponding
implementation lives in `src/main/java/chapters/ch11`, split into `domain`
(environment, agent, and trainer), `factory` (parameter and dependency
assembly), `plotting`, and `animation`. Tests live in
`src/test/java/chapters/ch11`.

For general repository layout and running instructions, see
`docs/CODE_ORGANIZATION.md` and `docs/HOW_TO_USE.md`. For the RBF-network
and gradient machinery reused here, see chapter 9's and chapter 10's code
(`core.nextlevelrl`), described in `docs/chapter-05.md`-style detail in
their own chapter docs if available.

## Runnable Examples

* **`RunnerTrainerLunarMultiStep`**
  (`src/run/java/ch11/RunnerTrainerLunarMultiStep.java`)
  Trains the actor-critic agent with a 5-step horizon for 10,000 episodes,
  plots training progress and value/policy heat maps, evaluates the failure
  rate over 100 landings, and saves single-episode simulation plots for a
  few fixed start conditions. Uses `chapters.ch11.factory.DependencyFactory`,
  `chapters.ch11.domain.trainer.core.TrainerLunarMultiStep`,
  `chapters.ch11.plotting.LunarTrainerPlotter`, and
  `chapters.ch11.plotting.AgentEvaluator`. This is the main end-to-end
  example for the chapter.

* **`RunnerStepHorizonEvaluator`**
  (`src/run/java/ch11/RunnerStepHorizonEvaluator.java`)
  Trains the agent repeatedly for step horizons 1 to 20 (10 repeats each,
  5000 episodes each) and plots average return versus step horizon, showing
  the effect of the n-step parameter. Uses `DependencyFactory`,
  `TrainerLunarMultiStep`, and
  `chapters.ch11.plotting.ChangingHorizonPlotter`.

* **`RunnerTrainerLunarAnimation`**
  (`src/run/java/ch11/RunnerTrainerLunarAnimation.java`)
  Same training loop as `RunnerTrainerLunarMultiStep`, but runs it with a
  live, sound-enabled Swing animation (`chapters.ch11.animation.AnimationLunar`)
  showing the lander, thrust flame, and periodic value/policy heat-map
  snapshots.

* **`RunnerAccelerationVsActionPlotter`**
  (`src/run/java/ch11/RunnerAccelerationVsActionPlotter.java`)
  Plots the environment's acceleration as a function of thrust force,
  independent of training, using `chapters.ch11.domain.environment.core.EnvironmentLunar.acceleration(...)`
  directly. Useful for understanding the physics model before training.

* **`RunnerKernelsPlotter`**
  (`src/run/java/ch11/RunnerKernelsPlotter.java`)
  Plots the (position, speed) center coordinates of the critic's RBF kernel
  grid, using `chapters.ch11.factory.DependencyFactory`,
  `chapters.ch11.domain.environment.core.RadialBasisAdapter`, and the shared
  `core.nextlevelrl.radial_basis.RbfNetwork`.

## Main Code

### Domain

`chapters.ch11.domain` is split into three areas:

* **`environment`** — `EnvironmentLunar` (implements `EnvironmentI`) models a
  simplified 1-D lunar lander: state (`StateLunar`/`VariablesLunar`, holding
  position `y` and speed `spd`), physics (`acceleration`, based on thrust
  force clipped to `[forceMin, forceMax]`, lander mass, and gravity), and
  termination/reward logic (landing, crashing at excess speed, and flying too
  high). `LunarParameters` holds the physical constants and reward values.
  `RadialBasisAdapter` converts a `StateLunar` to the `List<Double>` input
  expected by the RBF networks. `startstate_suppliers` provides
  `StartStateSupplierRandomAndClipped` (random start clipped to given
  position/speed ranges, used for training and evaluation) and
  `StartStateSupplierRandomHeightZeroSpeed`.
* **`agent`** — `AgentLunar` bundles an actor and a critic:
  `ActorMemoryLunar` (two `RbfNetwork` instances, one for the Gaussian
  policy's mean and one for its log-std) and `CriticMemoryLunar` (one
  `RbfNetwork` for the state value). `AgentLunar` samples actions via
  `core.foundation.gadget.normal_distribution.NormDistributionSampler` and
  computes policy gradients via
  `core.nextlevelrl.gradient.NormalDistributionGradientCalculator`.
  `AgentParameters` holds RBF kernel counts/widths and gradient/TD/advantage
  clipping limits.
* **`trainer`** — the training loop and its supporting types:
  * `core.TrainerLunarMultiStep` — the active trainer. For each episode it
    creates an episode (`EpisodeCreator`), computes n-step targets
    (`MultiStepResultsGenerator`), fits the critic and actor per step
    (`TrainingDataCreator`), and records progress measures.
  * `core.TrainerDependencies` — builder/record bundling agent, environment,
    trainer parameters, start-state supplier, and a timer.
  * `core.EpisodeCreator`, `core.EpisodeInfo`, `core.ExperienceLunar` —
    episode rollout and post-hoc extraction (rewards, positions, speeds,
    accelerations, first/last experience).
  * `multisteps` — `MultiStepResultsGenerator` computes the n-step
    discounted return and bootstrapped value target for each step of an
    episode (per the n-step formula documented in its Javadoc);
    `ValueCalculatorLunar` computes value/advantage/TD-error using the
    shared `core.foundation.gadget.training.ValueCalculator`;
    `TrainingDataCreator` turns a `MultiStepResult` into gradient-weighted
    training data (`TrainDataErr`) for critic and actor, with gradient and
    advantage clipping; `MultiStepResults`/`MultiStepResult` hold the
    per-step outputs.
  * `param.TrainerParameters` — episode count, max steps, gamma, step
    horizon (`nStepsHorizon`), learning rates, number of fit iterations.
  * **`deprecated`** — `TrainerLunarSingleStep` and
    `MemoryUpdaterSingleStepTrainer` implement an older one-step (TD(0))
    training loop. Neither class is referenced by any runnable example or
    by `TrainerLunarMultiStep`; they are superseded by the multi-step
    trainer and are effectively dead code kept for reference, similar to
    the `deprecated` package flagged in chapter 2.

### Factory

`chapters.ch11.factory` assembles parameters and dependencies:

* `DependencyFactory` — the main entry point used by every runner; builds a
  `TrainerDependencies` from a `LunarParameters`, step horizon, and episode
  count, wiring together `LunarAgentParamsFactory`, `TrainerParamsFactory`,
  a zero-weight `AgentLunar`, a new `EnvironmentLunar`, and a random/clipped
  start-state supplier.
* `LunarEnvParamsFactory` — default physical/reward constants
  (`LunarParameters`) for the environment.
* `LunarAgentParamsFactory` — default RBF kernel grid size (6x6) and kernel
  width (`gamma`, derived from a "relative sigma" and the state ranges), plus
  gradient/TD/advantage clip limits (`AgentParameters`).
* `TrainerParamsFactory` — default training hyperparameters
  (`TrainerParameters`): 5000 episodes, gamma 0.99, 5-step horizon, learning
  rates 1e-2 for actor and critic.
* `RbfMemoryFactory` — builds an `RbfNetwork` whose kernels are laid out on
  a regular (position x speed) grid, from `core.nextlevelrl.radial_basis`.
* `ProgressMeasuresFactory` — converts a training episode's experiences and
  multi-step results into a `ProgressMeasures` record (return, step count,
  TD error, gradient magnitude, actor std) for logging/plotting.

### Plotting

`chapters.ch11.plotting`:

* `LunarTrainerPlotter` — plots training-progress curves (return, actor
  std, best-action TD error, actor gradient) via the shared
  `core.plotting_rl.progress_plotting.PlotterProgressMeasures`, then delegates
  to `PlotterHeatMapsAgent` for value/policy heat maps.
* `PlotterHeatMapsAgent` — renders heat maps (over position x speed) of the
  actor's expected force, the resulting acceleration, and the critic's
  value estimate.
* `AgentEvaluator` — runs the trained agent without exploration to measure
  the fraction of failed (crashed) landings, and plots/saves a single
  simulated episode's force/acceleration/speed/position curves.
* `ChangingHorizonPlotter` — plots average return (with min/max error bars)
  against step horizon, used by `RunnerStepHorizonEvaluator`.

### Animation

`chapters.ch11.animation` — `AnimationLunar` drives a live Swing animation
(lander body, thrust flame particles, background stars, per-step data table)
plus periodic value/policy heat-map snapshots, built on the shared
`core.animation` graphics kit. `LunarParams` computes drawing geometry from
lander state, and `SoundsLunar` plays thrust/crash/landing sound effects
(`.wav` files bundled in the same package).

## Shared Code

Chapter 11 builds on several reusable `core` packages:

* `core.nextlevelrl.radial_basis` (`RbfNetwork`, `Kernel`, `Kernels`,
  `Weights`, `WeightUpdater`, `Activations`) — the RBF function-approximator
  used for both actor and critic memories. Also used by chapter 9 (where it
  is introduced) and chapter 14.
* `core.nextlevelrl.gradient` (`NormalDistributionGradientCalculator`,
  `GradientMeanAndLogStd`, `SafeGradientClipper`) — Gaussian-policy gradient
  computation, also used by chapter 12 (inverted pendulum) and chapter 10
  (cannon, via its own gradient calculator).
* `core.foundation.gadget.training` (`TrainDataErr`, `ValueCalculator`) —
  generic training-data containers and value/advantage/TD-error formulas,
  used across most later chapters' actor-critic code (e.g. chapter 10,
  chapter 12).
* `core.foundation.gadget.normal_distribution.NormDistributionSampler` —
  samples continuous actions from a Gaussian policy; shared with other
  continuous-action chapters.
* `core.plotting_rl.progress_plotting` (`RecorderProgressMeasures`,
  `ProgressMeasures`, `PlotterProgressMeasures`, `ProgressMeasureEnum`) —
  the generic training-progress recording/plotting framework used by nearly
  every chapter from chapter 6 onward.
* `core.animation` (`AnimationKit`, `GfxComponentFactory`, `LineSegment`,
  `GraphicsDto`, `DelayIntervalFunction`, etc.) — the generic Swing
  animation toolkit, also used by chapters 4, 6, 10, 12, and 13 for their
  respective animated environments.
* `core.foundation.config` (`ConfigFactory`, `PathAndFile`, `PlotConfig`,
  `AnimationConfig`) — chart-saving locations (`pathPicsConfig().ch11()`),
  plot sizing, and animation timing settings, shared across all chapters.

Chapter 11's own environment, agent, and trainer code (`EnvironmentLunar`,
`AgentLunar`, `TrainerLunarMultiStep`, and the `multisteps` package) is
specific to this chapter and is not reused elsewhere in the repository.

## Code Flow

```
run (RunnerTrainerLunarMultiStep, src/run/java/ch11)
  -> factory.DependencyFactory
     -> factory.LunarEnvParamsFactory / LunarAgentParamsFactory / TrainerParamsFactory
     -> domain.agent.core.AgentLunar.zeroWeights (RbfMemoryFactory -> core.nextlevelrl.radial_basis.RbfNetwork)
     -> domain.environment.core.EnvironmentLunar
  -> domain.trainer.core.TrainerLunarMultiStep.train()
     -> domain.trainer.core.EpisodeCreator (rolls out one episode)
     -> domain.trainer.multisteps.MultiStepResultsGenerator (n-step targets, advantage, TD error)
     -> domain.trainer.multisteps.TrainingDataCreator (gradient-weighted TrainDataErr)
     -> AgentLunar.fitCritic / fitActor (RbfNetwork.fitFromErrors)
     -> factory.ProgressMeasuresFactory -> core.plotting_rl RecorderProgressMeasures
  -> plotting.LunarTrainerPlotter / plotting.AgentEvaluator
     -> core.plotting_rl / core.plotting_core -> ChartSaver
```

For the animated variant (`RunnerTrainerLunarAnimation`), the same training
loop runs with an `animation.AnimationLunar` instance passed into `train(...)`,
which is invoked after each step and each episode to update the Swing
visualization and play sounds.

## Where to Start

Start with **`RunnerTrainerLunarMultiStep`**
(`src/run/java/ch11/RunnerTrainerLunarMultiStep.java`). It exercises the
full pipeline — dependency assembly, n-step actor-critic training,
progress/heat-map plotting, and evaluation — in a single, readable `main`
method, and is the natural jumping-off point into `TrainerLunarMultiStep`
and the `multisteps` package where the core algorithm lives.
