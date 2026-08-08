# How to Use the Code

This repository contains the Java source code that accompanies the book
*Reinforcement Learning Explained* (see `README.md` for background on the
book).

The code is organized by chapter. For each chapter of the book there is:

* a runnable example (or a small set of runnable examples) that you can
  execute directly, and
* the underlying implementation that the example uses.

This lets you read a chapter, run the matching example, and then step into
the code to see how the concepts described in the book are implemented.

The project is built with Maven (`pom.xml`) and targets Java 17/21.

## Runnable Examples

Runnable examples live under `src/run/java`. This is a separate source
folder (configured in `relexp-java.iml`), next to `src/main/java` (the
implementation) and `src/test/java` (the tests).

Under `src/run/java`, examples are grouped into packages named after the
book chapters, for example:

* `src/run/java/ch2`
* `src/run/java/ch8`
* `src/run/java/ch10`
* `src/run/java/ch14`

Each example is a small class with a `main` method, for example:

* `src/run/java/ch2/RunnerFitterSingleParameter.java`
* `src/run/java/ch8/RunnerTrainerParking.java`
* `src/run/java/ch10/RunnerTrainerBanditAnimation.java`
* `src/run/java/ch13/RunnerSearcherLane.java`

Most of these classes are named `Runner...` and can be identified by their
`public static void main(String[] args)` method. A few chapters have
several runnable examples, including variants that show an animation
(class names ending in `Animation`, e.g. `RunnerTrainerRoadAnimation.java`,
`RunnerPendulumAnimation.java`) or that plot a specific result
(e.g. `RunnerCannonDistancePlotter.java`, `RunnerKernelsPlotter.java`).

There is also a small `src/run/java/sandbox` package with a helper example
(`RunPropertiesLoader.java`) that is not tied to a specific chapter.

## Running an Example

A practical way to explore a chapter:

1. Pick a chapter from the book.
2. Find the matching package under `src/run/java`, for example
   `src/run/java/ch9` for chapter 9.
3. Open one of the `Runner...` classes in that package and run its `main`
   method (for example from your IDE, since `src/run/java` is set up as a
   source folder in `relexp-java.iml`).
4. Follow the code from the runner class into the implementation under
   `src/main/java` (see below) to see how the example is built.
5. Inspect the results. Many runners train an agent and print progress, or
   produce plots with XChart/JFreeChart. Plot output paths are configured
   per chapter in `src/main/resources/relexp.properties`
   (keys `pathpic.ch2` … `pathpic.ch14`, pointing to folders such as
   `pictures/ch2/`), so generated images can be found there after running
   an example that plots something.

## Source Code

The implementation behind the examples lives under `src/main/java`:

* `src/main/java/chapters` contains the chapter-specific implementation,
  organized in the same `chN` packages as the examples (e.g.
  `chapters/ch5`, `chapters/ch10`, `chapters/ch14`).
* `src/main/java/core` contains shared, reusable building blocks used
  across chapters (for example grid-world reinforcement learning support
  in `core/gridrl`, math and vector helpers in `core/foundation/gadget`,
  and neural-network/radial-basis helpers in `core/nextlevelrl`).

Tests for the implementation are under `src/test/java`, mirroring the same
chapter packages.

This document only gives a high-level orientation. For a more detailed
description of how the code is organized, see
`docs/CODE_ORGANIZATION.md`.

## Chapter Documentation

Separate documentation files exist for chapters 2 through 14, named
`docs/chapter-02.md` through `docs/chapter-14.md` (using two-digit chapter
numbers, e.g. `docs/chapter-09.md`).

Each chapter document helps you find, for that specific chapter:

* the runnable example(s) under `src/run/java`
* the chapter-specific source code under `src/main/java/chapters`
* shared/reusable code under `src/main/java/core` that the chapter's code
  depends on

## Recommended Workflow

A simple way to combine the book and the code:

1. Read the chapter in the book.
2. Open the corresponding chapter document under `docs/` (e.g.
   `docs/chapter-08.md`) to see which runnable examples and source files
   are relevant.
3. Run the example(s) under `src/run/java` for that chapter.
4. Step into the implementation under `src/main/java/chapters` and
   `src/main/java/core` to see how the example works.
5. Look at any generated plots (under the chapter's configured
   `pathpic.chN` folder) or console output to connect the results back to
   the concepts described in the book.
