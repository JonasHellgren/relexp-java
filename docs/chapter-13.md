# Chapter 13

## Overview

Chapter 13 introduces Monte Carlo Tree Search (MCTS). The code implements a
generic MCTS engine (selection, expansion, simulation/rollout,
backpropagation) built around a tree of `Node`s, and applies it to two
example scenarios:

* **jumper** — a small discrete "climbing" problem (moving up/along a
  staircase while avoiding a fall), used mainly to illustrate the search
  algorithm on a tiny, printable tree.
* **lane_change** — a continuous-state car lane-change problem, used to
  illustrate MCTS on a more realistic control task, including an animated
  visualization of the search.

Runnable examples live in `src/run/java/ch13`. The corresponding
implementation lives in `src/main/java/chapters/ch13`, split into `domain`
(the generic MCTS engine), `implem` (the `jumper` and `lane_change`
scenarios), `factory` (dependency assembly per scenario), `plotting` (a
Graphviz `.dot` file generator for trees), and `animation` (live animation of
the search, specific to `lane_change`). Tests live in
`src/test/java/chapters/ch13`.

For general repository layout and running instructions, see
`docs/CODE_ORGANIZATION.md` and `docs/HOW_TO_USE.md`.

## Runnable Examples

* **`RunnerSearcherJumper`**
  (`src/run/java/ch13/RunnerSearcherJumper.java`)
  Runs MCTS on the `jumper` scenario for a configurable number of iterations
  and exploration/backup settings (see the `SETTING` and `NOFITERATIONS`
  enums), then writes the resulting tree and its optimal path as a Graphviz
  `.dot` file and prints the `dot -Tpng ...` command to render it. Uses
  `chapters.ch13.factory.jumper.FactoryDependenciesJumper`,
  `chapters.ch13.domain.searcher.core.Searcher`, and
  `chapters.ch13.plotting.DotFileGenerator`. Good entry point for the
  discrete/tiny scenario.

* **`RunnerSearcherLane`**
  (`src/run/java/ch13/RunnerSearcherLane.java`)
  Runs MCTS on the `lane_change` scenario, writes the search tree to a
  `.dot` file, and plots the steering/heading angles and lateral (`y`)
  position along the extracted optimal path using `core.plotting_core`
  charts. Uses `chapters.ch13.factory.lane_change.FactoryDependenciesLaneChange`
  and `chapters.ch13.domain.searcher.core.Searcher`.

* **`RunnerSearcherLaneAnimation`**
  (`src/run/java/ch13/RunnerSearcherLaneAnimation.java`)
  Same `lane_change` setup as `RunnerSearcherLane`, but runs the search with
  a live animation (`chapters.ch13.animation.AnimationLaneChange`) showing
  the car moving along the currently explored path and a heat map of node
  values across the tree, using `core.animation` and
  `core.foundation.config.AnimationConfig`.

* **`RunnerDotFileGenerator`**
  (`src/run/java/ch13/RunnerDotFileGenerator.java`)
  Builds a tiny, hand-constructed two-node `jumper` tree
  (`chapters.ch13.factory.jumper.FactoryTreeJumper.tinyTree()`) without
  running the searcher, and writes it to a `.dot` file. Useful for
  understanding `DotFileGenerator`'s output format in isolation.

## Main Code

### Domain

`chapters.ch13.domain` holds the generic MCTS engine, independent of any
scenario:

* `searcher.core` — the algorithm itself:
  * `Searcher` — runs the main MCTS loop (select → expand → simulate →
    backpropagate) for a configured number of iterations, optionally driving
    an `AnimationLaneChange` after each step/iteration.
  * `OuterDependencies` — a builder-style record bundling everything a
    scenario must supply: `SearcherParameters`, an `EnvironmentI`, a node
    naming function, and a rollout policy.
  * `Dependencies` / `SearchWorkers` — internal wiring that pairs
    `OuterDependencies` with the concrete worker objects (`Selector`,
    `Expander`, `Simulator`, `BackPropagator`) and a `CpuTimer`.
  * `SearcherParameters`, `Variables` — search configuration (exploration
    constant, max iterations/depth, normal vs. defensive discount and
    learning rate) and per-iteration mutable state (current node, path).
* `searcher.workers` — the four MCTS phases as separate classes:
  `Selector` (UCT-based child selection), `Expander` (adds a new child node
  for an untried action), `Simulator` (random/rollout-policy playout to a
  terminal state or depth limit), `BackPropagator` (updates value/count
  statistics along the path, using a different learning rate/discount when
  the rollout ended in failure — "defensive backup").
* `searcher.path` — `Path`/`PathInfo` (the sequence of nodes/experiences
  visited in one iteration, with return calculation) and
  `OptimalPathExtractor` (greedily walks the tree, ignoring exploration, to
  extract the best path found so far).
* `tree` — `Node`/`NodeInfo`/`Family`/`Statistics` (a search-tree node,
  its parent/children, and its visit count/value) and `Tree`/`TreeInfo`
  (the tree as a whole, with helpers such as `numberOfNodes()`,
  `nodesAtDepth()`, and min/max value, used by the animation's heat map).
* `environment` — `EnvironmentI<S, A>` (a single `step(state, action)`
  method), `Experience`, and `StepReturnI`: the minimal interface a scenario
  environment must implement to be searchable.

### Implem (`jumper`, `lane_change`)

`chapters.ch13.implem` contains the two concrete scenarios, each
implementing `EnvironmentI` plus its own state/action/parameter types:

* **`jumper`** — `EnvironmentJumper`, `StateJumper`, `ActionJumper`,
  `JumperParameters`: a discrete "climb while moving forward" problem where
  falling (jumping down while at height) ends the episode with a penalty,
  and certain positions award coin rewards.
* **`lane_change`** — `EnvironmentLane`, `StateLane`, `ActionLane`,
  `LaneChangeParameters`: a continuous single-track (bicycle-model) car
  environment where the action is a steering angle; the state advances by
  numerical integration of heading, `x`, and `y` over a fixed time step, and
  reward penalizes steering changes and going off the desired lane, and
  rewards reaching the target `y` position.

Note: unlike some other chapters' `implem` packages (e.g. chapter 4), the
`jumper` and `lane_change` scenarios here do **not** have their own nested
`core`/`factory`/`start_state_suppliers` subpackages — factories live one
level up in the chapter-level `chapters.ch13.factory` package instead (see
below), and there is no start-state-supplier abstraction in chapter 13.

### Factory

`chapters.ch13.factory` assembles dependencies per scenario, one
subpackage each:

* **`jumper`** — `FactoryDependenciesJumper` (builds `OuterDependencies` for
  `test()`/`runner()` use), `FactorySearcherParametersJumper` (search
  hyperparameters), `JumperParametersFactory` (environment parameters),
  `FactoryNameFunctionJumper` (random node-name generator, used because
  `.dot` node IDs must be unique), `FactoryRolloutPolicyJumper` (simulation
  policy), `FactoryTreeJumper` (builds a root node, or a hand-built
  `tinyTree()` for `RunnerDotFileGenerator`), `RunnerSettings` (exploration
  constant / defensive discount / learning rate bundle used by
  `RunnerSearcherJumper`'s scenario enums), and `FactoryExperienceList`
  (a small experience-list builder used only by
  `TestBackPropagator`).
* **`lane_change`** — `FactoryDependenciesLaneChange` (builds
  `OuterDependencies` for `runner()`/`animation()`),
  `FactorySearcherSettingsLaneChange` (search hyperparameters, separate
  `test()`/`animation()` presets), `LaneChangeParametersFactory`
  (environment parameters), `FactoryNameFunctionLaneChange`,
  `FactoryRolloutPolicyLaneChange`, `FactoryTreeLaneChange` (builds the root
  node for a lane-change search).

### Plotting

`chapters.ch13.plotting.DotFileGenerator` walks a `Tree` and produces
Graphviz DOT source, labeling each node with its name/action and
visit-count/value statistics, and drawing the optimal path with thicker
edges. It is shared by all three non-animation runners (`RunnerSearcherJumper`,
`RunnerSearcherLane`, `RunnerDotFileGenerator`). Actual rendering to an
image happens outside Java, via the `dot` command-line tool (see the
comment at the top of each runner).

### Animation

`chapters.ch13.animation` (`AnimationLaneChange`, `LaneChangeParams`) is
specific to the `lane_change` scenario. It renders, per simulation step, the
car and lane mid-lines as line segments plus a small data table, and, per
search iteration, a heat map of node values across the tree by depth. It is
built on the shared `core.animation` toolkit (`AnimationKit`,
`GfxComponentFactory`, `HeatMapCharts`, `GraphicsDto`, etc.).

## Shared Code

Chapter 13 relies on the following shared (`core`) building blocks:

* `core.animation` (`AnimationKit`, `AnimationSettings`, `GfxComponentFactory`,
  `GraphicsDto`, `HeatMapCharts`, `LineSegment`, `DelayIntervalFunction`, ...)
  — the generic real-time animation toolkit. It is also used by chapters 4,
  6, 10, 11, and 12; chapter 13's `AnimationLaneChange` builds on it the same
  way chapter 12's `AnimationPendulum` does.
* `core.plotting_core` (`ChartSaver`, `ManyLinesChartCreator`, `PlotSettings`)
  — the generic XY-chart layer used by `RunnerSearcherLane` to plot steering
  angle, heading, and `y` position; used across most chapters for line
  charts.
* `core.foundation.config` (`ConfigFactory`, `PathAndFile`, `PlotConfig`,
  `AnimationConfig`) — resolves picture/output paths (`pathPicsConfig().ch13()`)
  and shared plot/animation sizing settings, used across all chapters.
* `core.foundation.util` (`RandUtil`, `ListCreatorUtil`, `MathUtil`,
  `UnitConverterUtil`, `ConditionalsUtil`, `ListUtil`) — generic helpers for
  randomness, list construction, floating-point comparison, radians↔degrees
  conversion, and conditional execution, reused across chapters.
* `core.learningutils.MyRewardListUtils` — a small reward-list helper used by
  `PathInfo` to compute discounted returns along a path; also used by
  chapters 6, 11, and 14 for similar return/result-aggregation purposes.
* `core.foundation.gadget.timer.CpuTimer` — generic run-time measurement,
  used by `SearchWorkers`/`Searcher.logTime()`.

Chapter 13's own domain code (the MCTS engine under
`chapters.ch13.domain`) and both scenario implementations are specific to
this chapter and are not reused elsewhere in the repository.

## Code Flow

```
run (RunnerSearcherJumper / RunnerSearcherLane / RunnerSearcherLaneAnimation)
  -> factory (FactoryDependenciesJumper / FactoryDependenciesLaneChange)
     -> implem (EnvironmentJumper|EnvironmentLane, rollout policy, name function)
     -> domain.searcher.core.OuterDependencies
  -> domain.searcher.core.Searcher.search(root[, animation])
     -> domain.searcher.workers: Selector -> Expander -> Simulator -> BackPropagator
        (repeated once per iteration, updating the domain.tree.Tree)
  -> domain.searcher.path.OptimalPathExtractor (extract best path)
  -> plotting.DotFileGenerator (write .dot file)  and/or  core.plotting_core (XY charts)
```

For `RunnerSearcherLaneAnimation`, `Searcher.search` additionally drives
`chapters.ch13.animation.AnimationLaneChange` after every step and every
iteration instead of (or in addition to) writing static output.

## Where to Start

Start with **`RunnerSearcherJumper`**
(`src/run/java/ch13/RunnerSearcherJumper.java`). Its `jumper` scenario is
small and discrete, so the resulting `.dot` tree stays readable, making it
easy to see how `Searcher`'s select/expand/simulate/backpropagate loop
builds up the tree. Once that flow is clear, `RunnerSearcherLane` shows the
same engine applied to a continuous-state problem with chart output, and
`RunnerSearcherLaneAnimation` shows it with a live animated view of the
search.
