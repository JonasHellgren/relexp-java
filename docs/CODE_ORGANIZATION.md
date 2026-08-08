# Code Organization

This document explains how the Java code accompanying *Reinforcement
Learning Explained* is organized, so you can find where a given piece of
logic lives.

For step-by-step instructions on running the code, see
`docs/HOW_TO_USE.md`. This document focuses on structure, not usage.

## Overall Organization

The project is a single Maven module (`pom.xml`) with three Java source
roots, configured in `relexp-java.iml`:

* `src/main/java` — the implementation code: chapter-specific logic plus
  shared building blocks.
* `src/run/java` — small runnable examples, one (or a few) per chapter,
  each with a `public static void main` method.
* `src/test/java` — tests, mostly mirroring the chapter packages under
  `src/main/java/chapters`.

In short: `src/run/java` is the entry point you execute, and
`src/main/java` is where the actual reinforcement-learning code lives.
A runnable example wires together objects from `src/main/java` and starts
training, evaluation, or plotting.

## Source Code

Under `src/main/java` there are two top-level packages that reflect the
main split in the codebase:

* `chapters` — code that is specific to one chapter of the book, in
  packages named `ch2` through `ch14` (e.g. `chapters/ch5`,
  `chapters/ch10`, `chapters/ch14`).
* `core` — code shared across multiple chapters (see "Shared Code"
  below).

There is also a small `src/main/java/sandbox` package (`Main.java`) that
is not tied to a chapter.

## Common Package Types

Within a chapter's package under `src/main/java/chapters/chN`, the same
kinds of subpackages tend to recur, although not every chapter has all of
them and naming varies slightly between chapters.

### `domain`

Holds the core reinforcement-learning concepts for the chapter: agents,
environments, states, actions, memories, trainers, and policies. Many
chapters further split `domain` into subpackages such as `agent`,
`environment`, `trainer` (or `trainer_dep`/`trainers`), `memory`, and
`policy` (for example `chapters/ch5/domain/environment`,
`chapters/ch8/domain/agent`, `chapters/ch11/domain/trainer`,
`chapters/ch6/domain/trainers`).

### `factory`

Contains classes that build and configure the objects an example needs,
such as parameter objects, dependency bundles, and (in some chapters)
plot/chart factories (for example `chapters/ch8/factory`,
`chapters/ch11/factory`, `chapters/ch13/factory`). Class names are
frequently prefixed with `Factory` (e.g. `FactoryTrainerParameters`,
`FactoryDependencies`) or suffixed with `Factory`
(e.g. `AgentGridParametersFactoryRoad`).

### `plotting`

Contains code used to visualize training or evaluation results —
recorders that collect progress measures and plotter classes that turn
them into charts (for example `chapters/ch8/plotting`,
`chapters/ch9/plotting`, `chapters/ch14/plotting`).

### `implem`

Several chapters (`ch3`, `ch4`, `ch5`, `ch6`, `ch13`, `ch14`) also have an
`implem` package holding concrete problem implementations — for example
different environments or scenarios within the same chapter. These are
often organized into one subpackage per scenario, such as
`chapters/ch4/implem/cliff_walk`, `chapters/ch4/implem/treasure`,
`chapters/ch4/implem/blocked_road_lane`, `chapters/ch13/implem/jumper`,
and `chapters/ch13/implem/lane_change`, each typically with its own
`core`, `factory`, and `start_state_suppliers` subpackages.

Not all chapters follow this exact pattern; for instance chapter 9 groups
its algorithm code under a `gradient_descent` package instead of
`domain`/`implem`, alongside its own `factory` and `plotting` packages.

## Shared Code

Code under `src/main/java/core` is reused across several chapters rather
than belonging to one of them. Its main subpackages are:

* `core/foundation` — general-purpose building blocks: math and vector
  helpers (`foundation/gadget/math`, `foundation/gadget/vector_algebra`),
  collection and utility helpers (`foundation/util`), configuration
  reading (`foundation/config`), and small value types such as
  `foundation/gadget/pos`.
* `core/gridrl` — shared support for grid-world reinforcement-learning
  problems: grid states, actions, agents, environments, and memories used
  by several grid-based chapters (e.g. `chapters/ch4`).
* `core/nextlevelrl` — shared support for more advanced techniques, such
  as gradient calculators (`nextlevelrl/gradient`), neural-network helpers
  built on Deeplearning4j (`nextlevelrl/neural`), and radial-basis-function
  utilities (`nextlevelrl/radial_basis`).
* `core/plotting_core` — the generic charting layer (built on XChart and
  JFreeChart), including 2D and 3D chart creators
  (`plotting_core/plotting_2d`, `plotting_core/plotting_3d`) and chart
  saving (`plotting_core/chart_saving_and_plotting`). Chapter-specific
  `plotting` packages build on top of this shared layer.
* `core/plotting_rl` — shared progress-measure recording used by trainers
  across chapters (`plotting_rl/progress_plotting`).
* `core/learninggadgets` and `core/learningutils` — small shared helpers
  related to learning-rate decay and reward handling.

The reader should not assume that each chapter's implementation is fully
self-contained: chapter code regularly depends on `core` for grid
mechanics, math, neural-network helpers, and plotting, and this is a
normal, intended part of the design.

## Chapter-Specific Code

Chapter-specific code lives under `src/main/java/chapters/chN`, where `N`
is the chapter number (2 through 14). Within each chapter package, code is
generally organized using the recurring package types described above
(`domain`, `factory`, `plotting`, and sometimes `implem`), rather than
being a flat collection of classes.

Tests for this code live under `src/test/java/chapters/chN`, mirroring
the same chapter numbering.

## Runnable Examples

The code under `src/run/java` provides the entry points for exploring the
book's examples. Examples are grouped into packages named after the
chapters (e.g. `src/run/java/ch2`, `src/run/java/ch10`), and each example
is a small class — usually named `Runner...` — with a `main` method.

A runner class typically does not contain reinforcement-learning logic
itself. Instead, it uses a `factory` from `src/main/java/chapters/chN` to
build the objects it needs, runs training or evaluation using classes from
that chapter's `domain` package, and then uses a `plotting` class (from
the chapter package and/or `core/plotting_core`) to visualize the result.

Detailed instructions on how to run these examples are in
`docs/HOW_TO_USE.md`.

## Typical Code Structure

A common flow for a chapter, from entry point to shared support code,
looks like this:

```
run (RunnerXxx, src/run/java/chN)
  -> factory (chapters/chN/factory or chapters/chN/implem/.../factory)
    -> domain (chapters/chN/domain, or implem/... for a specific scenario)
      -> core (core/gridrl, core/foundation, core/nextlevelrl, ...)
    -> plotting (chapters/chN/plotting)
      -> core/plotting_core, core/plotting_rl
```

Not every chapter uses every layer, and some chapters (e.g. `ch9`) name
their domain-level package differently (`gradient_descent` instead of
`domain`), but the overall pattern — a runner using a factory to assemble
domain objects and a plotting layer to visualize results — is consistent
across the codebase.
