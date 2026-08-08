# Chapter 2

## Overview

Chapter 2 code is about fitting a value with a simple tabular (bucket-based)
memory using a fixed learning rate, and using the same mechanism to
approximate a 1-D function (a sigmoid). This is the simplest possible
"learning" example in the book: no agent, no environment — just a memory
that is updated toward a target value using the delta rule
`memory += learningRate * (target - memory)`.

Runnable examples live in `src/run/java/ch2`. The corresponding
implementation lives in `src/main/java/chapters/ch2`, split into `domain`,
`factory`, `impl`, and `plotting` subpackages, plus a `deprecated`
subpackage that is no longer wired up correctly (see below). Tests live in
`src/test/java/chapters/ch2`.

For general repository layout and running instructions, see
`docs/CODE_ORGANIZATION.md` and `docs/HOW_TO_USE.md`.

## Runnable Examples

* **`RunnerFitterSingleParameter`** (`src/run/java/ch2/RunnerFitterSingleParameter.java`)
  Trains a single scalar memory value toward a fixed target (`1.0`) for
  several learning rates (`0.1, 0.2, 0.4, 0.8`) over 51 iterations, and
  plots how the fitted value converges for each learning rate. Uses
  `chapters.ch2.factory.FittingParametersFactory`,
  `chapters.ch2.factory.TrainingResultsGenerator`,
  `chapters.ch2.factory.ManyLinesChartCreatorFactory`, and
  `chapters.ch2.plotting.SingleParameterFittingPlotter`.

* **`RunnerSigmoidFunctionFitter`** (`src/run/java/ch2/RunnerSigmoidFunctionFitter.java`)
  Samples random points from a sigmoid function and fits a tabular
  (bucketed) memory to approximate the function over 1000 epochs, then
  plots the fitted curve. Uses `chapters.ch2.factory.FitterFunctionFactory`,
  `chapters.ch2.factory.FittingParametersFactory`,
  `chapters.ch2.impl.function_fitting.FitterFunctionOutput`, and
  `core.foundation.util.math.SigmoidFunctionsUtil`.

* **`RunnerPlotSigma`** (`src/run/java/ch2/RunnerPlotSigma.java`)
  A small helper example that just plots the raw sigmoid function itself
  (no fitting), useful as a reference before looking at the fitted version.
  Uses `chapters.ch2.factory.FittingParametersFactory` and the shared chart
  classes `core.plotting_rl.chart.ChartCreatorFactory` and
  `core.plotting_core.chart_saving_and_plotting.ChartSaver`.

## Main Code

### Domain

`chapters.ch2.domain.fitting` holds the core fitting concepts:

* `FittingParameters` — a record with the learning rate, number of
  iterations, default memory value, value range, bucket width (`deltaX`),
  and a plotting margin.
* `TabularMemory` — a simple `Map<Integer, Double>`-backed memory keyed by
  bucket index, with a default value for unseen buckets.
* `MemoryFitterI` / `FitterFunctionI` — interfaces for fitting a memory
  from `TrainData` and reading values back out (single value or a list of
  values).
* `MemoryFitterOutput` — the actual delta-rule fitter: for each training
  sample it looks up the bucket via a `BucketFinder`, computes the error
  against the stored value, and updates the memory by
  `learningRate * error`.

`chapters.ch2.domain.parameter_fitting.LearningRateFittingResults` is a
small record wrapping a `Map<Double, List<Double>>` that stores, for each
learning rate, the sequence of fitted values over the training iterations
(used by `RunnerFitterSingleParameter`).

`chapters.ch2.impl` contains two concrete usages built on top of
`MemoryFitterOutput`:

* `impl.parameter_fitting.FitterSingleParameter` — fits one scalar value
  (a single infinite bucket), used for the learning-rate comparison
  example.
* `impl.function_fitting.FitterFunctionOutput` and
  `FitterOutCalculator` — fit a 1-D function over a bounded, bucketed range
  and produce a list of outputs for a list of inputs (used to draw the
  fitted sigmoid curve).

### Factory

`chapters.ch2.factory` builds the objects the runners need:

* `FittingParametersFactory` — builds a default `FittingParameters`
  (learning rate `0.1`, range `10.0`, bucket width `1.0`).
* `FitterFunctionFactory` — builds a `FitterFunctionOutput` for function
  fitting, wiring a `BucketFinder` (from `core.foundation.gadget.math`)
  from the configured range and bucket width.
* `TrainingResultsGenerator` — runs the single-parameter fitting loop for
  a list of learning rates and collects a `LearningRateFittingResults`.
* `ManyLinesChartCreatorFactory` — builds a `ManyLinesChartCreator`
  (one line per learning rate) via the shared `core.plotting_rl.chart.ManyLinesFactory`.

### Plotting

`chapters.ch2.plotting.SingleParameterFittingPlotter` adds one line per
learning rate to a `ManyLinesChartCreator` and saves/shows the resulting
chart, using the shared `core.plotting_core.chart_saving_and_plotting.ChartSaver`.

### Deprecated code

`chapters.ch2.deprecated` (`FitterFunctionDifferences`,
`MemoryFitterErrors`, `RunnerFitterFunctionFromDifferences`) contains an
older variant that fits a function from local derivative/error samples
instead of direct targets. Its imports reference classes and packages
(`chapters.ch2.factory.ChartFactory`, `chapters.ch2.domain.FittingParameters`,
`core.foundation.util.math.SigmoidFunctions`, `core.plotting.chart_plotting.ChartSaverAndPlotter`)
that do not exist in the current package layout, so this code does not
compile as part of the current build. Readers should use the runners in
`src/run/java/ch2` described above instead.

## Shared Code

Chapter 2 relies on the following shared (`core`) building blocks, also
used by other chapters:

* `core.foundation.gadget.training.TrainData` — the generic
  input/output training-sample container used to feed both the
  single-parameter and function fitters.
* `core.foundation.gadget.math.BucketFinder` — maps a continuous value
  into a discrete bucket index within a configured `Range`; this is what
  turns the fitters into tabular/discretized methods.
* `core.foundation.util.math.SigmoidFunctionsUtil` — supplies the
  sigmoid function sampled and approximated in `RunnerSigmoidFunctionFitter`
  and `RunnerPlotSigma`.
* `core.foundation.config.ConfigFactory` / `PathPicsConfig` — resolve
  where generated charts are saved (`pathPicsConfig().ch2()`).
* `core.plotting_core` (`ChartFactory`, `ChartSaver`, `ChartUtility`,
  `ManyLinesChartCreator`) and `core.plotting_rl.chart`
  (`ChartCreatorFactory`, `ManyLinesFactory`) — the generic charting layer
  used to draw both single-line and multi-line (per-learning-rate) plots.

None of this shared code is specific to chapter 2; it is reused across
most chapters for training data, plotting, and configuration.

## Code Flow

A typical chapter 2 example follows:

```
run (RunnerXxx, src/run/java/ch2)
  -> factory (chapters.ch2.factory: FittingParametersFactory, FitterFunctionFactory / TrainingResultsGenerator)
    -> domain (chapters.ch2.domain.fitting: MemoryFitterOutput, TabularMemory)
      -> core.foundation.gadget.math.BucketFinder, core.foundation.gadget.training.TrainData
    -> plotting (chapters.ch2.plotting.SingleParameterFittingPlotter, or core.plotting_core directly)
      -> core.plotting_core, core.plotting_rl.chart
```

The runner builds `FittingParameters` via the factory, repeatedly calls
`fit(data)` on a fitter (either `FitterSingleParameter` or
`FitterFunctionOutput`) to update the underlying `TabularMemory`, reads
back the fitted value(s), and hands them to a chart creator that saves and
displays the plot.

## Where to Start

Start with **`RunnerFitterSingleParameter`**
(`src/run/java/ch2/RunnerFitterSingleParameter.java`). It is the simplest
end-to-end example: it fits one scalar memory value toward a fixed target
for several learning rates and plots the convergence curves. It touches
every layer used in this chapter — parameters, fitter, training-results
collection, and multi-line plotting — without the extra function-domain
bucketing logic used by `RunnerSigmoidFunctionFitter`.
