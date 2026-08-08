# Chapter 12

## Overview

Chapter 12 introduces neural-network function approximation for value-based
reinforcement learning, built on Deeplearning4j (DL4J). It has two parts:

* A minimal **neural contextual bandit** example that replaces the tabular
  action-value table of chapter 10's bandit with a small DL4J network, to
  show the basic mechanics of fitting Q-values with a neural net.
* A full **inverted-pendulum control** problem (swing-and-balance with a
  discrete torque action) trained with a DQN-style agent: a replay buffer,
  a target network, and a mini-batch fitting loop.

Runnable examples live in `src/run/java/ch12`. The corresponding
implementation lives in `src/main/java/chapters/ch12`, split into `domain`
(the bandit and inverted-pendulum domain code), `factory` (dependency and
plot assembly), `plotting_bandit` / `plotting_invpend` (chapter-specific
visualization), and `animation` (a live visualization of pendulum training).
Tests live in `src/test/java/chapters/ch12`.

For general repository layout and running instructions, see
`docs/CODE_ORGANIZATION.md` and `docs/HOW_TO_USE.md`. For the tabular bandit
this chapter's neural version is compared against, see chapter 10's bandit
code (`chapters.ch10.bandit`).

## Runnable Examples

* **`RunnerBanditValueTrainer`**
  (`src/run/java/ch12/RunnerBanditValueTrainer.java`)
  Trains a tiny DL4J network (`chapters.ch12.domain.bandit.trainer.BanditActionValueMemory`,
  a single `OutputLayer` with 1 input / 2 outputs) to learn the two Q-values
  of a two-armed bandit, using `chapters.ch12.domain.bandit.trainer.BanditActionValueTrainer`.
  The bandit environment itself is **reused from chapter 10**
  (`chapters.ch10.bandit.domain.environment.EnvironmentBandit`, wrapped by
  `chapters.ch12.domain.bandit.environment.EnvironmentBanditWrapper`), and
  its parameters come from chapter 10's
  `chapters.ch10.factory.FactoryEnvironmentParametersBandit`. Results
  (loss and the two learned Q-values) are plotted with
  `chapters.ch12.plotting_bandit.ErrorBandPlotterNeuralBandit`. This is the
  simplest example in the chapter and a good entry point into DL4J usage.

* **`RunnerPendulumSimulation`**
  (`src/run/java/ch12/RunnerPendulumSimulation.java`)
  Runs the inverted-pendulum physics (`chapters.ch12.domain.inv_pendulum.environment.core.EnvironmentPendulum`)
  under a fixed, hand-written policy (no learning) and plots the resulting
  angle/speed trajectory. Useful for understanding the environment dynamics
  in isolation, before any agent is introduced.

* **`RunnerPendulumTrainer`**
  (`src/run/java/ch12/RunnerPendulumTrainer.java`)
  The main DQN-style training example. Builds a full set of dependencies via
  `chapters.ch12.factory.TrainerDependenciesFactory.createForTrainerRunning(...)`,
  runs `chapters.ch12.domain.inv_pendulum.trainer.core.TrainerPendulum`,
  evaluates the trained agent with
  `chapters.ch12.domain.inv_pendulum.agent.evaluator.PendulumAgentEvaluator`,
  and plots training curves, evaluation trajectories, and heat maps of the
  learned value/policy with `chapters.ch12.plotting_invpend.TrainerPlotter`
  and `chapters.ch12.plotting_invpend.PendulumAgentMemoryPlotter`.

* **`RunnerPendulumAnimation`**
  (`src/run/java/ch12/RunnerPendulumAnimation.java`)
  Same training setup as `RunnerPendulumTrainer`, but drives training through
  `chapters.ch12.animation.AnimationPendulum`, which renders the swinging
  pendulum and live value/policy heat maps step by step using the shared
  `core.animation` package.

## Main Code

### Domain — bandit (`chapters.ch12.domain.bandit`)

* `trainer.BanditActionValueMemory` — a one-layer DL4J `MultiLayerNetwork`
  (`OutputLayer`, MSE loss, `Adam` updater) that maps a constant dummy input
  to two Q-values (left/right).
* `trainer.BanditActionValueTrainer` — the training loop: builds mini-batches
  from `BanditTrainerDependencies`, fits the network, and records loss and
  Q-values via `chapters.ch12.plotting_bandit.BanditNeuralRecorder`. It only
  updates the Q-value of the action actually sampled, starting each target
  batch from the network's own predictions (comment: "Key feature: Only
  update the Q-value for the action taken").
* `environment.EnvironmentBanditWrapper` — a thin adapter around chapter 10's
  `EnvironmentBandit`/`ActionBandit`, exposing a single `step(int action)`
  method that returns a reward.

### Domain — inverted pendulum (`chapters.ch12.domain.inv_pendulum`)

* `environment.core` — `EnvironmentPendulum` (semi-implicit/symplectic Euler
  physics step, reward combining energy loss and a failure penalty),
  `StatePendulum`/`VariablesPendulum` (angle, angular speed, step count),
  `ActionPendulum` (discrete torque values, e.g. CW/N/CCW),
  `StepReturnPendulum`.
* `environment.param.PendulumParameters` — physical constants (inertia,
  length, mass, `dt`, `g`), reward shaping weights (`lambdaEnLoss`,
  `lambdaFail`), and episode limits (`angleMax`, `maxTime`/`maxSteps`).
* `environment.startstate_supplier` — `StartStateSupplierI` and
  `StartStateSupplierEnum`, which build start-state suppliers for different
  training/evaluation scenarios (zero angle, small random angle, feasible
  random angle and speed).
* `agent.core.AgentPendulum` — chooses actions (epsilon-random exploration),
  reads Q-values from its live network and a separate target network, and
  fits the live network from a mini-batch of `(state, action, target)`
  triples. Uses `core.nextlevelrl.neural.Dl4JUtil` to convert Java lists into
  ND4J arrays.
* `agent.memory.AgentMemory` — the DL4J `MultiLayerNetwork` wrapper: a dense
  input layer, optional hidden layers, and a linear (`IDENTITY`) output layer
  with one Q-value per action; supports `fit`, `predict`, and `copy` (used to
  synchronize the target network).
* `agent.memory.PendulumScaler` — normalizes angle/angular-speed inputs to
  the network's input range using `core.foundation.gadget.math.ScalerLinear`.
* `agent.memory.ActionAndItsValue` — a small `(action, value)` pair used
  throughout the agent and trainer.
* `trainer.core` — the DQN training machinery:
  * `ExperiencePendulum` — one `(state, action, stepReturn)` transition.
  * `ReplayBuffer` / `MiniBatch` — stores experiences and samples random
    mini-batches, evicting a random old entry once the buffer is full.
  * `TargetCalculator` — computes TD targets using the **target network**
    for the next state's best action value (target Q-learning update).
  * `TrainerDependencies` — bundles the agent, environment, trainer
    parameters, start-state supplier, a `CpuTimer`, and two
    `core.foundation.gadget.math.LogarithmicDecay` schedules (for the
    random-action probability and the learning rate).
  * `TrainerPendulum` — the episode loop: choose action, step environment,
    sample a mini-batch, compute targets, fit the agent, periodically copy
    parameters to the target network (`maybeCopyToTargetNetwork`), and
    record progress. Accepts an `AnimationPendulum` (or `empty()`) so the
    same loop is used for plain training and animated training.
* `agent.evaluator.PendulumAgentEvaluator` — runs the trained agent
  greedily (no exploration) from a given start state for a fixed duration
  and records whether it fails.

### Factory (`chapters.ch12.factory`)

* `HyperParametersPendulum` — a record of top-level training knobs (hidden
  layers/units, learning-rate schedule, mini-batch size, episode count,
  fail-penalty vs. reward-shaping mode, fixed vs. random start), with named
  presets such as `FAIL_PEN_START_RANDOM` used by the runners.
* `PendulumParametersFactory`, `AgentParametersFactory`,
  `TrainerParametersFactory` — build the physics, network, and training
  parameter records for tests, trainer tests, and full trainer runs
  (`createForTest`, `createForTrainerTest`, `createForTrainerRunning`).
* `TrainerDependenciesFactory` — the main assembly point: wires an
  `AgentPendulum`, `EnvironmentPendulum`, `TrainerParameters`, and a start
  state supplier into a `TrainerDependencies`, with separate methods for
  tests, trainer runs, and animation runs.
* `BanditTrainerParametersFactory`, `BanditTrainerDependenciesFactory` — the
  equivalent assembly for the neural bandit example.
* `ManyLinesChartCreatorFactory` — builds pre-styled
  `core.plotting_core.plotting_2d.ManyLinesChartCreator` instances for the
  simulation, evaluation, and action-value plots.
* `MockedTrainDataFactory` — builds synthetic state/target data used only by
  the chapter's unit tests (`TestAgentPendulum`, `TestAgentMemoryFitting`,
  `TestTargetCalculator`), not by any runner.

### Plotting (`plotting_bandit`, `plotting_invpend`)

* `plotting_bandit` — `BanditNeuralRecorder` collects per-epoch measures
  (`MeasuresBanditNeural`, keyed by `MeasuresBanditNeuralEnum`: loss and the
  two Q-values); `ErrorBandPlotterNeuralBandit` turns them into filtered
  error-band charts via `core.plotting_rl.progress_plotting`.
* `plotting_invpend` — `RecorderTrainerPendulum` / `MeasuresPendulumTraining`
  record per-episode training measures (return, time, loss, action values at
  a reference state); `RecorderPendulumMeasure` / `MeasuresPendulumSimulation`
  record per-step simulation/evaluation measures (angle, speed, torque);
  `ErrorBandPlotterNeuralPendulum`, `TrainerPlotter`, and
  `PendulumAgentMemoryPlotter` turn these into training-curve charts,
  evaluation-trajectory charts, and heat maps of the learned value function
  and policy over the angle/speed state space.

### Animation (`chapters.ch12.animation`)

* `AnimationPendulum` — renders the swinging pendulum, torque direction, and
  live value/policy heat maps during training, built on the shared
  `core.animation` framework (`AnimationKit`, `GfxComponentFactory`,
  `GraphicsDto`, `LineSegment`, `HeatMapCharts`, ...). `PendulumParams`
  computes on-screen line-segment geometry from a `StatePendulum`, and
  `SoundsPendulum` plays success/failure sound effects (`fanfar.wav`,
  `metal-pipe.wav`) bundled in the same package.
  `AnimationPendulum.java` imports `chapters.ch11.domain.agent.core.AgentLunar`,
  `chapters.ch11.domain.environment.core.StateLunar`, and
  `chapters.ch11.domain.trainer.multisteps.MultiStepResult`, but none of
  these classes are actually referenced in the file — apparent leftover
  imports from copy-pasted chapter-11 animation code. Likewise,
  `RunnerBanditValueTrainer` imports
  `core.foundation.configOld.ProjectPropertiesReader` without using it. These
  are harmless (unused-import) artifacts, not functional bugs, but worth
  being aware of if you use this code as a template.

## Shared Code

Chapter 12 relies on several `core` building blocks and one other chapter's
domain code:

* `chapters.ch10.bandit.domain.environment` and
  `chapters.ch10.factory.FactoryEnvironmentParametersBandit` — the neural
  bandit example reuses chapter 10's tabular bandit **environment**
  unchanged; only the value-learning side (a neural net instead of a table)
  is new in chapter 12.
* `core.nextlevelrl.neural.Dl4JUtil` — converts Java lists to ND4J
  `INDArray`s for feeding the pendulum agent's network.
* `core.foundation.gadget.math.LogarithmicDecay` — the decaying schedule used
  for both the random-action probability and the learning rate; also used by
  chapters 3, 5, 6, 8, and 10.
* `core.foundation.gadget.math.ScalerLinear`, `core.foundation.gadget.cond.Counter`,
  `core.foundation.gadget.timer.CpuTimer`, `core.foundation.util.rand.RandUtil` —
  generic scaling, loop-limiting, timing, and randomization helpers used
  throughout the trainer, evaluator, and agent.
* `core.foundation.config.ConfigFactory` / `PathAndFile` — resolve
  chart-saving locations (`pathPicsConfig().ch12()`) and plot settings,
  shared across all chapters.
* `core.plotting_core` (`ChartSaver`, `ManyLinesChartCreator`,
  `HeatMapChartCreator`, `ErrorBandCreator`) and
  `core.plotting_rl.progress_plotting` (`ErrorBandData`,
  `ErrorBandSaverAndPlotter`) — the generic charting layer underlying all of
  this chapter's plots.
* `core.animation` — the generic live-animation framework
  (`AnimationKit`, `GfxComponentFactory`, `GraphicsDto`, `LineSegment`,
  `HeatMapCharts`, `DelayIntervalFunction`, ...) used by
  `chapters.ch12.animation.AnimationPendulum`.
* DL4J/ND4J (`org.deeplearning4j.*`, `org.nd4j.*`) — the underlying
  neural-network library used directly by `AgentMemory` and
  `BanditActionValueMemory` for both the bandit and pendulum networks.

Chapter 12's own physics, agent, replay-buffer, and DQN-training code
(`domain.inv_pendulum`) is specific to this chapter and not reused
elsewhere in the repository.

## Code Flow

For the pendulum trainer (`RunnerPendulumTrainer` / `RunnerPendulumAnimation`):

```
run (RunnerPendulumTrainer, src/run/java/ch12)
  -> factory (TrainerDependenciesFactory, AgentParametersFactory,
              PendulumParametersFactory, TrainerParametersFactory)
    -> domain.inv_pendulum.agent.core.AgentPendulum (network via agent.memory.AgentMemory)
    -> domain.inv_pendulum.environment.core.EnvironmentPendulum
  -> domain.inv_pendulum.trainer.core.TrainerPendulum.train()
    -> per step: ReplayBuffer.sampleMiniBatch() -> TargetCalculator.calculateTargets()
       -> AgentPendulum.fit() -> periodic copyParamsToTargetNet()
  -> domain.inv_pendulum.agent.evaluator.PendulumAgentEvaluator.evaluate()
  -> plotting_invpend.TrainerPlotter / PendulumAgentMemoryPlotter
    -> core.plotting_core / core.plotting_rl -> ChartSaver
```

For the neural bandit example (`RunnerBanditValueTrainer`):

```
run (RunnerBanditValueTrainer, src/run/java/ch12)
  -> factory (BanditTrainerDependenciesFactory, BanditTrainerParametersFactory)
    -> domain.bandit.environment.EnvironmentBanditWrapper (wraps chapters.ch10 bandit env)
    -> domain.bandit.trainer.BanditActionValueMemory (DL4J network)
  -> domain.bandit.trainer.BanditActionValueTrainer.train()
  -> plotting_bandit.ErrorBandPlotterNeuralBandit
    -> core.plotting_core / core.plotting_rl -> ChartSaver
```

## Where to Start

Start with **`RunnerBanditValueTrainer`**
(`src/run/java/ch12/RunnerBanditValueTrainer.java`). It isolates the
neural-network fitting mechanics (`BanditActionValueMemory`,
`BanditActionValueTrainer`) from the more complex replay-buffer/target-network
machinery of the pendulum example, while reusing an environment already
familiar from chapter 10. Once the basic "fit a DL4J network to Q-value
targets" pattern is clear, move on to `RunnerPendulumTrainer` to see the same
idea extended into a full DQN-style trainer with a replay buffer and target
network, and finally `RunnerPendulumAnimation` to watch it train live.
