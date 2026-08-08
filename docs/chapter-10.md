# Chapter 10

## Overview

Chapter 10 introduces policy-gradient learning with two independent
one-step ("contextual bandit"-style) scenarios, each with its own
domain code:

* **Bandit** — a two-armed bandit with a discrete `LEFT`/`RIGHT` action.
  The policy is a softmax over two memory parameters, updated with the
  discrete-action policy-gradient rule.
* **Cannon** — a continuous-action problem: choosing a firing angle to
  hit a target distance. The policy is a Gaussian (mean, std) over the
  angle, updated with the continuous-action policy-gradient rule and a
  moving-average baseline.

Runnable examples live in `src/run/java/ch10`. The implementation lives
in `src/main/java/chapters/ch10`, split into `bandit` and `cannon`
subpackages (each with `domain.agent`, `domain.environment`/`envrionment`,
`domain.trainer`), a shared `factory` package, a shared `plotting`
package, and a shared `animation` package. Tests live in
`src/test/java/chapters/ch10`.

Note: the cannon environment's package is spelled `envrionment` (not
`environment`) in the actual source tree
(`chapters.ch10.cannon.domain.envrionment`) — this is a pre-existing
typo in the repository, not a documentation error.

## Runnable Examples

* **`RunnerTrainerBandit`**
  (`src/run/java/ch10/RunnerTrainerBandit.java`)
  Trains a bandit agent (`AgentBandit`) against an environment where the
  left arm pays out much more often
  (`FactoryEnvironmentParametersBandit.veryHighLeftProbability()`), using
  either a high-learning-rate/few-episodes or low-learning-rate/many-episodes
  parameter set from `FactoryTrainerParameters`. Plots the summed reward,
  the action probabilities, and the gradient-of-log values with
  `ErrorBandPlotterBandit`.

* **`RunnerTrainerBanditAnimation`**
  (`src/run/java/ch10/RunnerTrainerBanditAnimation.java`)
  Same setup as above, but passes a live `AnimationBandit` into
  `TrainerBandit.train(animation)` to show the bandit machine, its arms,
  and the coin/reward outcome step by step, plus a heat map of the memory
  parameters after each episode.

* **`RunnerTrainerCannon`**
  (`src/run/java/ch10/RunnerTrainerCannon.java`)
  Trains a cannon agent (`AgentCannon`) to hit a fixed target distance,
  comparing gradient-clipped vs. non-clipped agent parameters
  (`FactoryAgentParametersCannon.forRunning()` /
  `forRunningNoMeanClipping()`). Plots return-minus-baseline, baseline,
  angle, distance, and the learned mean/std of the policy with
  `ErrorBandPlotterCannon`.

* **`RunnerTrainerCannonAnimation`**
  (`src/run/java/ch10/RunnerTrainerCannonAnimation.java`)
  Trains a cannon agent with `FactoryAgentParametersCannon.animation()` /
  `FactoryTrainerParametersCannon.animation()` and drives a live
  `AnimationCannon` showing the cannon firing, hitting/missing the
  target, and the learned mean/std heat map. See the "stale code" note
  below — parts of this file are leftover/unused.

* **`RunnerCannonDistancePlotter`**
  (`src/run/java/ch10/RunnerCannonDistancePlotter.java`)
  Not a training example: sweeps the firing angle from 0° to 90° through
  `EnvironmentCannon.step(angle)` and plots the resulting distance vs.
  angle curve, using `core.plotting_rl.chart.ChartCreatorFactory`. Useful
  for understanding the cannon environment's reward/distance shape before
  training.

## Main Code

### Domain — Bandit (`chapters.ch10.bandit.domain`)

* `agent.AgentBandit` — holds an `agent.MemoryBandit` (two policy
  parameters `z_array`), chooses `LEFT`/`RIGHT` by sampling a softmax
  distribution (`core.foundation.gadget.math.SoftMax`), and updates the
  memory with `θ ← θ + α·G(t)·∇log π(a|s,θ)`.
* `environment.EnvironmentBandit` — on `step(action)`, samples a coin
  toss with a per-arm probability (`environment.EnvironmentParametersBandit`)
  and returns a `environment.StepReturnBandit` (reward, whether a coin was
  won).
* `trainer.TrainerBandit` — runs the episode loop: generate an experience
  via `trainer.EpisodeGeneratorBandit`, compute the return, the action
  probabilities and the gradient of the log-policy
  (`trainer.GradLogCalculatorDiscreteActions`), update the agent, and
  record measures (`trainer.RecorderBandit`). Accepts an optional
  `chapters.ch10.animation.bandit.AnimationBandit` to drive a live view.
* `trainer.TrainerDependenciesBandit` — a record bundling the agent,
  environment, and `trainer.TrainerParametersBandit`, with convenience
  methods used by the trainer.

### Domain — Cannon (`chapters.ch10.cannon.domain`)

* `agent.AgentCannon` — holds an `agent.MemoryCannon` storing `(zMean,
  zStd)` such that `(mean, std) = (zMean, exp(zStd))`; samples the fired
  angle from a normal distribution
  (`core.foundation.gadget.normal_distribution.NormDistributionSampler`)
  and updates memory using a clipped gradient
  (`core.nextlevelrl.gradient.GradientMeanAndLogStd`).
* `envrionment.EnvironmentCannon` — computes the projectile distance from
  the firing angle using a simple ballistic formula with a wind-resistance
  term, and a reward equal to minus the absolute distance-to-target error
  (`envrionment.EnvironmentParametersCannon`, `envrionment.StepReturnCannon`).
* `trainer.TrainerCannon` — runs the episode loop like `TrainerBandit`,
  but also maintains a running baseline (`base`) subtracted from the
  return to reduce variance, uses a decaying learning rate
  (`core.foundation.gadget.math.LogarithmicDecay`), and computes the
  policy gradient via `trainer.GradLogCalculatorContinuousAction`, which
  wraps `core.nextlevelrl.gradient.NormalDistributionGradientCalculator`.
* `trainer.TrainerDependenciesCannon` — bundles environment, agent,
  `trainer.TrainerParametersCannon`, the learning-rate decay, and the
  gradient calculator.

### Factory

`chapters.ch10.factory` builds parameter objects for both scenarios:

* `FactoryEnvironmentParametersBandit` / `FactoryAgentParametersBandit` —
  bandit environment probabilities (e.g. `veryHighLeftProbability`) and
  initial policy parameters (e.g. `equalProbability`).
* `FactoryTrainerParameters` — bandit trainer settings (learning rate,
  number of episodes) for fast/slow/animated runs.
* `FactoryEnvironmentParametersCannon` / `FactoryAgentParametersCannon` —
  the default ballistic environment, and cannon agent variants with
  different clipping strategies (`noClipping`, `clipIn0And45Degrees`,
  `clipIn0And45DegreesAndGradient`, plus `forRunning`,
  `forRunningNoMeanClipping`, `animation`).
* `FactoryTrainerParametersCannon` — cannon trainer settings (gradient
  denominator floor, episode count, learning-rate start/end for the
  decay).

### Plotting

`chapters.ch10.plotting` contains chapter-level plot helpers built on the
shared charting layer:

* `MeasuresBandit` / `MeasuresBanditEnum` and `MeasuresCannon` /
  `MeasuresCannonEnum` — define which per-episode quantities can be
  plotted (e.g. summed reward, action probabilities, gradient-of-log
  values for the bandit; return-minus-baseline, angle, distance, mean/std
  for the cannon).
* `ErrorBandPlotterBandit` / `ErrorBandPlotterCannon` — turn a recorder's
  trajectories into error-band charts (optionally windowed/filtered) via
  `core.plotting_core.plotting_2d.ErrorBandCreator` and
  `core.plotting_rl.progress_plotting.ErrorBandData` /
  `ErrorBandSaverAndPlotter`, saving output under
  `ConfigFactory.pathPicsConfig().ch10()`.

### Animation

`chapters.ch10.animation` contains one live-visualization class per
scenario, both built on the shared `core.animation` graphics kit:

* `animation.bandit.AnimationBandit` (with `BanditParams`, `SoundsBandit`)
  — draws the bandit machine, its two arms, a coin dispenser, and a heat
  map of the two memory parameters; optionally plays a coin sound.
* `animation.cannon.AnimationCannon` (with `CannonParams`, `SoundsCannon`)
  — draws the cannon firing, the target, hit/miss dots, and a heat map of
  the learned mean/std angle; plays fire/hit/splat sounds.

## Shared Code

Chapter 10 relies on the following shared (`core`) building blocks:

* `core.animation` — the generic live-plotting/graphics kit
  (`AnimationKit`, `GfxComponentFactory`, `GraphicsDto`, `LineSegment`,
  `IntervalData`, `DelayIntervalFunction`, etc.), the same kit used for
  animated examples in earlier chapters (e.g. chapter 4).
* `core.nextlevelrl.gradient` — `GradientMeanAndLogStd`,
  `NormalDistributionGradientCalculator`, and `SafeGradientClipper`,
  used by the cannon agent/trainer to compute and clip the Gaussian
  policy gradient. This package is also used by chapter 11's
  `AgentLunar`.
* `core.foundation.gadget.math.LogarithmicDecay` — the learning-rate
  decay schedule used by `TrainerDependenciesCannon`; also used across
  several other chapters (ch3, ch4, ch5, ch6, ch8, ch11, ch12).
* `core.foundation.gadget.normal_distribution` (`NormDistributionSampler`,
  `NormalSampler`, `ProbabilityDistributionFunction`) — Gaussian sampling
  used by `AgentCannon`.
* `core.foundation.gadget.math.SoftMax` — the softmax used by
  `AgentBandit` to turn memory parameters into action probabilities.
* `core.plotting_core` and `core.plotting_rl` — the generic charting layer
  and progress-measure/error-band recording used by both
  `ErrorBandPlotterBandit`/`ErrorBandPlotterCannon` and by
  `RunnerCannonDistancePlotter` (`core.plotting_rl.chart.ChartCreatorFactory`).
* `core.foundation.config.ConfigFactory` — resolves picture-output paths
  (`pathPicsConfig().ch10()`) and animation/plot settings.

## Code Flow

```
run (RunnerTrainerBandit / RunnerTrainerCannon, src/run/java/ch10)
  -> factory (chapters.ch10.factory: FactoryEnvironmentParameters*, FactoryAgentParameters*, FactoryTrainerParameters*)
    -> domain.environment / domain.agent (EnvironmentBandit|EnvironmentCannon, AgentBandit|AgentCannon)
      -> domain.trainer (TrainerDependencies*: EpisodeGenerator*, GradLogCalculator*)
        -> domain.trainer (TrainerBandit|TrainerCannon) -- episode loop, agent memory update
  -> plotting (chapters.ch10.plotting: ErrorBandPlotterBandit|ErrorBandPlotterCannon)
    -> core.plotting_rl / core.plotting_core
```

For the animated runners, the same dependencies are built, but
`train(animation)` is called with a `chapters.ch10.animation.bandit.AnimationBandit`
or `chapters.ch10.animation.cannon.AnimationCannon` instead of the default
no-op, driving `core.animation` graphics after each step/episode.

## Stale / inconsistent code

* **`RunnerTrainerCannonAnimation`**
  (`src/run/java/ch10/RunnerTrainerCannonAnimation.java`) imports several
  chapter 11 classes (`chapters.ch11.domain.environment.param.LunarParameters`,
  `StartStateSupplierI`, `StartStateSupplierRandomAndClipped`,
  `TrainerDependencies`, `TrainerLunarMultiStep`, `AgentEvaluator`,
  `LunarTrainerPlotter`) and defines a `PATH` field and a `plotting(...)`
  helper method that reference chapter 11 types and
  `ConfigFactory.pathPicsConfig().ch11()`. None of this is actually used:
  the `plotting(...)` call in `main` is commented out, so these imports
  and the `PATH`/`plotting` method are dead, leftover code (likely
  copy-pasted from a chapter 11 runner). This does not affect the
  runner's behavior — it still trains and animates the cannon agent
  correctly — but a reader following the imports should not expect a
  real dependency from chapter 10 onto chapter 11 training/plotting code.
* **`RunnerCannonDistancePlotter`**
  (`src/run/java/ch10/RunnerCannonDistancePlotter.java`) saves its output
  chart under `ConfigFactory.pathPicsConfig().ch11()` instead of
  `.ch10()`, even though it only uses chapter 10's `EnvironmentCannon`
  and `FactoryEnvironmentParametersCannon`. This looks like a copy-paste
  inconsistency; the generated `cannon_distance` image ends up in the
  chapter 11 pictures folder rather than chapter 10's.

## Where to Start

Start with **`RunnerTrainerBandit`**
(`src/run/java/ch10/RunnerTrainerBandit.java`). It is the simplest
example in the chapter — a two-parameter policy, a one-line environment,
and a single-step episode — so it is the clearest place to see the
discrete-action policy-gradient update (`AgentBandit.updateMemory`,
`GradLogCalculatorDiscreteActions`) before moving on to the more
involved continuous-action cannon example (`RunnerTrainerCannon`), which
adds a baseline and gradient clipping on top of the same overall
structure.
