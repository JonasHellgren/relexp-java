# Chapter 9

## Overview

Chapter 9 covers function approximation with gradient descent: fitting a
linear model by hand-rolled gradient descent, radial-basis-function (RBF)
networks (1D and 2D), and a small neural network built on Deeplearning4j.

Runnable examples live in `src/run/java/ch9`. The corresponding
implementation lives in `src/main/java/chapters/ch9`. Unlike most other
chapters, chapter 9 does not use a `domain`/`implem` split; its algorithm
code sits in a `gradient_descent` package, alongside its own `factory` and
`plotting` packages (confirmed in the actual source tree). Tests live in
`src/test/java/chapters/ch9`.

For general repository layout and running instructions, see
`docs/CODE_ORGANIZATION.md` and `docs/HOW_TO_USE.md`.

## Runnable Examples

* **`RunnerLinearFitter`**
  (`src/run/java/ch9/RunnerLinearFitter.java`)
  Fits a simple linear model (`y = w0 + w1*x`) to a small hand-written
  data set using gradient descent, with a configurable batch size, and
  plots the fitted line against the data plus the error-per-epoch curve.
  Uses `chapters.ch9.factory.TrainDataLinearFitterFactory` and
  `chapters.ch9.gradient_descent.LinearFitter`/`PhiExtractor`.

* **`RunnerPlotSingleRbf`**
  (`src/run/java/ch9/RunnerPlotSingleRbf.java`)
  Plots the activation curve of a single RBF kernel for three different
  sigma (width) values, to illustrate how sigma shapes the kernel. Uses
  `core.nextlevelrl.radial_basis.Kernels` and `RbfNetwork` directly (no
  chapter-9 factory).

* **`RunnerRadialBasis1dLineOnlyMiniBatch`**
  (`src/run/java/ch9/RunnerRadialBasis1dLineOnlyMiniBatch.java`)
  Trains an `RbfNetwork` with 6 kernels on a 1D linear target using
  mini-batch fitting, then plots the fitted correlation curve and the
  learned per-kernel weights against their centers.

* **`RunnerRadialBasis3dFunction`**
  (`src/run/java/ch9/RunnerRadialBasis3dFunction.java`)
  Fits an `RbfNetwork` (2D input, grid of kernels) to a 2D sinusoidal
  function and renders reference vs. fitted heat maps. Uses
  `chapters.ch9.factory.Radial3dFactory` for kernel/train-data/grid setup
  and `core.plotting_core.plotting_3d.HeatMapChartCreator`.

* **`RunnerTrainerNeuralOneDimensional`**
  (`src/run/java/ch9/RunnerTrainerNeuralOneDimensional.java`)
  Trains a small Deeplearning4j `MultiLayerNetwork` (one dense layer, one
  output layer) to approximate `y = x` over `[0, 10]`, logs
  predictions/loss during training, and plots the fitted correlation curve
  and an error band of loss/prediction measures over iterations. Uses
  `chapters.ch9.factory.DataGeneratorNeuralFactory`, `NeuralNetBuilder`,
  and the `chapters.ch9.plotting` recorder/plotter classes. A code comment
  in the file notes that swapping in `NeuralNetBuilder.buildWillFail()`
  intentionally demonstrates a training failure caused by negative weight
  initialization.

## Main Code

### Gradient Descent (algorithm code, in place of `domain`)

`chapters.ch9.gradient_descent` holds the hand-rolled linear-regression
gradient-descent implementation used by `RunnerLinearFitter`:

* `LinearFitter` — orchestrates fitting: holds the `TrainData`, a
  `WeightUpdaterLinear`, the `Weights` being learned, an
  `OutPutCalculator`, and a `CpuTimer`; exposes `fit(...)` and
  `fitAndReturnErrorPerEpoch(...)` (batch gradient descent over epochs)
  plus `calcOutputs(...)`/`calcOut(...)` for evaluating the fitted model.
* `PhiExtractor` — holds a list of feature functions
  (`ToDoubleFunction<List<Double>>`) applied to an input to produce the
  feature vector (`phi`) used by the linear model; `RunnerLinearFitter`
  configures it with a bias term (`x -> 1`) and the identity feature
  (`x -> x.get(0)`).
* `WeightUpdaterLinear` — computes the per-sample errors and the average
  gradient over a batch, then updates each weight by
  `learningRate * gradient[i]`.
* `OutPutCalculator` — computes the linear model's output as the dot
  product of `Weights` and the `PhiExtractor`'s feature values.

### Factory

`chapters.ch9.factory` builds the inputs used by the runners:

* `TrainDataLinearFitterFactory` — a fixed, hand-written 20-point data set
  (linearly spaced `x`, noisy `y`) used by `RunnerLinearFitter`.
* `Radial3dFactory` — builds a 20x20 grid of 2D `Kernel`s, the
  corresponding `TrainData` for a given `DoubleBinaryOperator` target
  function, and the `x`/`y` grid coordinates used both for training and
  for heat-map plotting in `RunnerRadialBasis3dFunction`.
* `DataGeneratorNeuralFactory` — generates `y = x` training data as ND4J
  `INDArray`s for the neural-network example.
* `NeuralNetBuilder` — builds a two-layer `MultiLayerNetwork` (dense +
  output, Adam optimizer, MSE loss); provides both a working weight
  initialization (`buildWillWork`) and a deliberately broken one
  (`buildWillFail`, negative constant weights) to illustrate a training
  failure mode.

### Plotting

`chapters.ch9.plotting` supports the neural-network example only (the RBF
and linear-fitter runners build their charts inline using
`core.plotting_core`):

* `MeasuresOneDimRegressionNeural` — a record capturing one training
  snapshot: `error` (loss), `valueLeft`, `valueRight` (predictions at two
  fixed inputs).
* `MeasuresOneDimRegressionNeuralEnum` — enumerates which measure
  (`LOSS`, `PRED1`, `PRED10`) to extract/plot from a snapshot.
* `NeuralOneDimRegressionRecorder` — accumulates a list of
  `MeasuresOneDimRegressionNeural` snapshots and exposes per-measure
  trajectories.
* `ErrorBandPlotterNeuralOneDimRegression` — turns a recorder's
  trajectories into filtered error-band charts, built on
  `core.plotting_core.plotting_2d.ErrorBandCreator` and
  `core.plotting_rl.progress_plotting.ErrorBandData`/
  `ErrorBandSaverAndPlotter`.

## Shared Code

Chapter 9 relies heavily on shared (`core`) building blocks — more so than
chapters that keep a self-contained `domain` package:

* `core.nextlevelrl.radial_basis` — the RBF network implementation used
  directly by three of the five runners (`RbfNetwork`, `Kernels`,
  `Kernel`, `Activations`, `Weights`, `WeightUpdater`, `TdErrorClipper`).
  This package is not specific to chapter 9: it is also used by chapter
  11's Lunar Lander memories (`chapters.ch11.domain.agent.memory.*`,
  `chapters.ch11.factory.RbfMemoryFactory`), by chapter 14's Pong
  long-term memory (`chapters.ch14.implem.pong_memory.LongMemoryRbf`),
  and by chapter 11's own kernel-plotting runner
  (`src/run/java/ch11/RunnerKernelsPlotter.java`).
* `core.nextlevelrl.neural` — Deeplearning4j helper code
  (`MultiLayerPrinter`, `Dl4JNetFitter`, `Dl4JUtil`, `NetSettings`); chapter
  9 only uses `MultiLayerPrinter` (to print learned weights), while the
  fuller neural-net fitting helpers here are used by chapter 12's inverted
  pendulum agent (`chapters.ch12.domain.inv_pendulum.agent.core.AgentPendulum`).
* `core.foundation.gadget.training` — `TrainData`, `TrainDataErr`,
  `Weights` (the plain vector-of-doubles used by the linear fitter), and
  `ValueCalculator`. These types are shared broadly, including with
  chapter 2's function fitters and chapter 4's grid agents.
* `core.plotting_core` — `ManyLinesChartCreator`, `ScatterWithLineChartCreator`,
  `ErrorBandCreator`, `HeatMapChartCreator`, `ChartSaver`, `ChartUtility`,
  and `PlotSettings` — the generic 2D/3D charting layer used by every
  runner in this chapter to render and save plots.
* `core.plotting_rl.progress_plotting` — `ErrorBandData` and
  `ErrorBandSaverAndPlotter`, used by
  `ErrorBandPlotterNeuralOneDimRegression` to build the loss/prediction
  error-band charts; the same package backs progress plotting in several
  other chapters (e.g. chapter 4's `PlotterProgressMeasures`).
* `core.foundation.config.ConfigFactory` — resolves chart output paths
  (`pathPicsConfig().ch9()`) and plot sizing (`plotConfig()`) used by all
  five runners.

## Code Flow

Chapter 9 has two slightly different flows depending on the example:

```
run (RunnerLinearFitter)
  -> factory (TrainDataLinearFitterFactory)
    -> gradient_descent (LinearFitter, PhiExtractor, WeightUpdaterLinear, OutPutCalculator)
  -> core.plotting_core (ScatterWithLineChartCreator, ManyLinesChartCreator, ChartSaver)
```

```
run (RunnerPlotSingleRbf / RunnerRadialBasis1dLineOnlyMiniBatch / RunnerRadialBasis3dFunction)
  -> factory (Radial3dFactory, for the 3D example) or built inline
    -> core.nextlevelrl.radial_basis (Kernels, RbfNetwork)
  -> core.plotting_core (ManyLinesChartCreator / HeatMapChartCreator, ChartSaver)
```

```
run (RunnerTrainerNeuralOneDimensional)
  -> factory (DataGeneratorNeuralFactory, NeuralNetBuilder)
    -> Deeplearning4j MultiLayerNetwork.fit(...)
  -> plotting (NeuralOneDimRegressionRecorder, ErrorBandPlotterNeuralOneDimRegression)
    -> core.plotting_rl / core.plotting_core
```

## Where to Start

Start with **`RunnerLinearFitter`**
(`src/run/java/ch9/RunnerLinearFitter.java`). It is the simplest example
and walks through the full chapter-9-specific gradient-descent stack
(`PhiExtractor` → `WeightUpdaterLinear` → `LinearFitter`) without needing
to understand RBF kernels or Deeplearning4j first. From there,
`RunnerPlotSingleRbf` is a good next step to build intuition for RBF
kernels before moving on to the fitting examples
(`RunnerRadialBasis1dLineOnlyMiniBatch`, `RunnerRadialBasis3dFunction`) and
finally the neural-network example.

## Notes on Repository Consistency

`RunnerLinearFitter` saves its charts using
`ConfigFactory.pathPicsConfig().ch11()` instead of `.ch9()`, even though
the class lives in the `ch9` package and both a `ch9` and a `ch11` picture
path are configured (`core.foundation.config.PathPicsConfig`). This looks
like a copy-paste leftover rather than an intentional cross-chapter link;
running the example will save its output images into the chapter-11
pictures folder instead of chapter 9's.
