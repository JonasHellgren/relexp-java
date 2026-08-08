# Chapter 3

## Overview

Chapter 3 introduces policy evaluation with a decaying learning rate, using
a small "splitting path" grid world: a corridor that forks at one state
into two possible routes, only one of which leads to a rewarding goal. The
code evaluates a fixed policy (random or optimal) by repeatedly sampling
transitions from random start states and fitting a tabular state-value
memory with `StateValueMemoryGrid.fit(...)`, using a learning rate that
decays logarithmically over the run.

Runnable examples live in `src/run/java/ch3`. The corresponding
implementation lives in `src/main/java/chapters/ch3`, split into
`implem/splitting_path_problem`, `policies`, `factory`, `plotting`, and a
`domain` subpackage that currently only contains a note (`info.md`)
pointing out that the actual domain interfaces
(`EnvironmentGridI`, `StartStateSupplierGridI`) live in `core`. Tests live
in `src/test/java/chapters/ch3`.

For general repository layout and running instructions, see
`docs/CODE_ORGANIZATION.md` and `docs/HOW_TO_USE.md`.

## Runnable Examples

* **`RunnerPolicyEvaluatorSplittingPath`**
  (`src/run/java/ch3/RunnerPolicyEvaluatorSplittingPath.java`)
  Evaluates a chosen policy (`OPTIMAL` by default, `RANDOM` as the
  alternative, selected via the `polChosen` field) on the splitting-path
  environment for 1000 fits, then plots the resulting state values as a
  heat map. Uses `chapters.ch3.factory.EvaluatorDependenciesFactory`,
  `chapters.ch3.implem.splitting_path_problem.PolicyEvaluatorSplittingPath`,
  the `chapters.ch3.policies` classes, and
  `chapters.ch3.factory.StateValueMemoryPlotterFactory`.

* **`RunnerLogarithmicDecay`**
  (`src/run/java/ch3/RunnerLogarithmicDecay.java`)
  A standalone helper example (no environment involved) that plots the
  shape of the logarithmic learning-rate decay used by the policy
  evaluator, for three different starting values (`90`, `50`, `10`) decaying
  to `0.01` over `10001` iterations. Uses
  `core.foundation.gadget.math.LogarithmicDecay` directly together with the
  shared `core.plotting_core.plotting_2d.ManyLinesChartCreator`. Useful to
  understand the decay behaviour before reading
  `RunnerPolicyEvaluatorSplittingPath`.

## Main Code

### Domain (`implem` and `policies`)

The splitting-path problem itself lives under
`chapters.ch3.implem.splitting_path_problem`:

* `EnvironmentParametersSplitting` — a record describing the grid bounds,
  valid actions, wall/goal/fail states, the special "split" state, and the
  reward function at the goal.
* `InformerSplitting` — implements `core.gridrl.InformerGridParamsI`;
  answers questions about a state (is it a wall, terminal, valid, at the
  split point, etc.) based on `EnvironmentParametersSplitting`.
* `EnvironmentSplittingPath` — implements `core.gridrl.EnvironmentGridI`;
  computes the next state and reward for a `(StateGrid, ActionGrid)` step,
  using `InformerSplitting` for validation and reward/terminal lookups.
* `StartStateSupplierGridRandomSplitting` /
  `StartStateSupplierGridMostLeftSplitting` — implement
  `core.gridrl.StartStateSupplierGridI`; supply either a random non-split
  state or the fixed leftmost state as the episode start (only the random
  supplier is currently wired into the runner).
* `EvaluatorDependencies` — a builder-style record bundling the
  environment, `StateValueMemoryGrid`, start-state supplier, decaying
  learning rate, discount factor, fit count, and an error list.
* `PolicyEvaluatorSplittingPath` — the evaluation loop itself: for each of
  `nFits` iterations it draws a start state, asks the policy for an action,
  steps the environment, computes a TD-style target
  (`discount * nextValue + reward`), and fits the memory toward that target
  with the current (decayed) learning rate.

`chapters.ch3.policies` holds the fixed policies being evaluated:

* `SplittingPathPolicyI` — a one-method interface (`chooseAction(StateGrid)`).
* `SplittingPathPolicyOptimal` — always moves East, except at the split
  state where it moves North (the rewarding branch).
* `SplittingPathPolicyRandom` — always moves East, except at the split
  state where it moves North or South with equal probability.

### Factory

`chapters.ch3.factory` builds the objects the runners need:

* `EnvironmentParametersSplittingFactory` — builds the concrete
  `EnvironmentParametersSplitting` for this chapter's grid (a 6x3 grid with
  walls forming a corridor, a split point, and two goal states with
  different rewards).
* `EvaluatorDependenciesFactory` — assembles a full `EvaluatorDependencies`
  (environment, zero-initialized `StateValueMemoryGrid`, random start-state
  supplier, `LogarithmicDecay` learning rate, discount factor `1.0`).
* `StateValueMemoryPlotterFactory` — builds a `StateValueMemoryPlotter`
  from the environment parameters and the fitted memory.

### Plotting

`chapters.ch3.plotting.StateValueMemoryPlotter` turns the fitted
`StateValueMemoryGrid` into a text-annotated heat map (walls and terminal
states shown as `.`, other cells showing the rounded state value), using
the shared `core.plotting_rl.chart.StringTextChartFactory` to build the
chart and `core.plotting_core.chart_saving_and_plotting.ChartSaver` to save
and display it.

## Shared Code

Chapter 3 relies on the following shared (`core`) building blocks:

* `core.gridrl` — `StateGrid`, `ActionGrid`, `StepReturnGrid`,
  `EnvironmentGridI`, `InformerGridParamsI`, `StartStateSupplierGridI`, and
  `StateValueMemoryGrid` (the tabular state-value memory with its own
  `fit`/`read`/`write` methods). This package is the shared grid-world
  foundation also used extensively by chapters 4, 5, 6, and 7.
* `core.foundation.gadget.math.LogarithmicDecay` — the decaying
  learning-rate schedule used by the evaluator; also reused by chapters 5,
  6, 8, 10, and 12.
* `core.foundation.config.ConfigFactory` — resolves where generated charts
  are saved (`pathPicsConfig().ch3()`) and reads chart size settings
  (`plotConfig()`).
* `core.plotting_core` (`ChartSaver`, `ManyLinesChartCreator`) and
  `core.plotting_rl.chart.StringTextChartFactory` — the generic charting
  layer used for both the learning-rate-decay line chart and the
  state-value heat map.

None of this shared code is specific to chapter 3; it is reused across
several chapters for grid mechanics, learning-rate decay, and plotting.

Conversely, chapter 3's own splitting-path environment is itself reused by
later chapters rather than being self-contained to this chapter:
`chapters.ch3.implem.splitting_path_problem.EnvironmentSplittingPath` (and
related classes such as `EnvironmentParametersSplittingFactory`) is used
directly by chapter 5's Monte Carlo example
(`chapters.ch5.implem.splitting.SplittingPathAdapter`,
`chapters.ch5.factory.SplittingDependenciesFactory`) and by chapter 6's
multi-step agents (`chapters.ch6.implem.splitting`,
`chapters.ch6.implem_animation.AnimationSplit`). Readers who want to see
the same environment used with different algorithms can follow these
references forward into chapters 5 and 6.

## Code Flow

```
run (RunnerPolicyEvaluatorSplittingPath, src/run/java/ch3)
  -> factory (chapters.ch3.factory: EnvironmentParametersSplittingFactory, EvaluatorDependenciesFactory)
    -> domain/implem (chapters.ch3.implem.splitting_path_problem: EnvironmentSplittingPath, PolicyEvaluatorSplittingPath)
      -> chapters.ch3.policies (SplittingPathPolicyOptimal / SplittingPathPolicyRandom)
      -> core.gridrl (StateGrid, ActionGrid, StateValueMemoryGrid)
      -> core.foundation.gadget.math.LogarithmicDecay
    -> plotting (chapters.ch3.plotting.StateValueMemoryPlotter)
      -> core.plotting_rl.chart.StringTextChartFactory, core.plotting_core.chart_saving_and_plotting.ChartSaver
```

The runner builds an `EvaluatorDependencies` bundle via the factory, picks a
policy from `policesMap`, runs `PolicyEvaluatorSplittingPath.evaluate(...)`
to fit the `StateValueMemoryGrid` over `nFits` sampled transitions, and then
plots the resulting state values as a heat-map image.

## Where to Start

Start with **`RunnerPolicyEvaluatorSplittingPath`**
(`src/run/java/ch3/RunnerPolicyEvaluatorSplittingPath.java`). It is the
main end-to-end example of the chapter: it builds the splitting-path
environment, runs the policy-evaluation loop with a decaying learning rate,
and produces a heat-map plot of the learned state values. Switching the
`polChosen` field between `OPTIMAL` and `RANDOM` is a quick way to see how
the evaluated policy changes the resulting value map.
