# Code Review – Top 10 Actions

## 1. Delete the orphaned `chapters/ch2/deprecated` package — project does not compile

**Severity:** Critical
**Effort:** Tiny
**Priority:** 10.0

**Problem:**
`chapters/ch2/deprecated/FitterFunctionDifferences.java`, `MemoryFitterErrors.java`, and `RunnerFitterFunctionFromDifferences.java` import classes that no longer exist: `chapters.ch2.domain.FitterFunctionI`, `chapters.ch2.domain.MemoryFitterI`, `chapters.ch2.domain.TabularMemory`, `chapters.ch2.domain.FittingParameters`, `chapters.ch2.domain.MemoryFitterOutput`, and call `FitterFunctionFactory.produceDifferences(...)` and `ChartSaverAndPlotter.showChartSaveInFolderConcepts(...)`, none of which exist anymore. Chapter 2 was refactored (e.g. `chapters.ch2.domain` → `chapters.ch2.domain.fitting`) and these three files were left behind, referencing the pre-refactor API.

**Why fix it:**
This breaks `mvn compile` for the entire project — nobody can build, test, or run any chapter until it's fixed. Verified: no other file in `src/` references `chapters.ch2.deprecated` (confirmed via project-wide search), so removal is safe.

**Where:**
`src/main/java/chapters/ch2/deprecated/FitterFunctionDifferences.java`, `MemoryFitterErrors.java`, `RunnerFitterFunctionFromDifferences.java`.

**Action:**
Delete the `chapters/ch2/deprecated` package (all three files). Nothing else in the codebase depends on it.

---

## 2. Neural-network hidden layers are hardcoded off in `AgentMemory` (ch12)

**Severity:** High
**Effort:** Tiny
**Priority:** 7.0

**Problem:**
`addHiddenLayers` hardcodes `double nHiddenLayers = 0d; // ap.nHiddenLayers();` with the real call commented out. `HyperParametersPendulum.N_HIDDEN_LAYERS = 2` is threaded correctly through `AgentParametersFactory` → `AgentParameters.nHiddenLayers` all the way to `AgentMemory`, but is then silently discarded — the loop that would add hidden layers never executes, so the network built for the inverted-pendulum agent is effectively input→output only, regardless of configuration.

**Why fix it:**
This silently degrades the RL agent's network capacity for the entire pendulum chapter (ch12) — training will run without errors but the model can't represent what the hyperparameters imply, producing misleading results with no visible failure.

**Where:**
`src/main/java/chapters/ch12/domain/inv_pendulum/agent/memory/AgentMemory.java:100-108`; configured in `src/main/java/chapters/ch12/factory/HyperParametersPendulum.java`.

**Action:**
Replace `double nHiddenLayers = 0d; // ap.nHiddenLayers();` with `int nHiddenLayers = ap.nHiddenLayers();`.

---

## 3. `MathUtil.isZero`/`isNeg`/`isPos` use `Double.MIN_VALUE` as tolerance (effectively exact equality)

**Severity:** High
**Effort:** Tiny
**Priority:** 7.0

**Problem:**
`isZero(double)` checks `Math.abs(value) < 2*Double.MIN_VALUE`, and `isNeg`/`isPos` compare against `±Double.MIN_VALUE`. `Double.MIN_VALUE` is the smallest positive double (~4.9e-324), not a sane epsilon, so these methods behave as near-exact equality rather than "approximately zero." Confirmed real call sites: `ListCreatorUtil.createListFromStartToEndWithNItems` uses `MathUtil.isZero(step)` to special-case a zero step, and `ManyLinesChartCreator.setAxisTicksFormatting` uses the same family for axis-tick decisions.

**Why fix it:**
Any step/value that is mathematically "basically zero" due to floating-point arithmetic (but not exactly `0.0`) will fail these checks, silently skipping the intended zero-handling branch — e.g. list generation logic or chart axis-tick formatting picks the wrong path instead of the zero-step/zero-tick special case.

**Where:**
`src/main/java/core/foundation/util/math/MathUtil.java:35-53`; consumed by `src/main/java/core/foundation/util/collections/ListCreatorUtil.java:102` and `src/main/java/core/plotting_core/plotting_2d/ManyLinesChartCreator.java`.

**Action:**
Replace `Double.MIN_VALUE` with a real epsilon (e.g. `1e-9`) in `isZero`, `isNeg`, and `isPos`.

---

## 4. Shared `core.gridrl.TrainerGridI` illegally depends on chapter-specific `ch4` class

**Severity:** High
**Effort:** Small

**Priority:** 4.9

**Problem:**
`core/gridrl/TrainerGridI.java` imports and uses `chapters.ch4.domain.animation.AnimationGridI` in its `train(AnimationGridI animation)` method signature. This violates the project's own ArchUnit rule `coreSubFoldersShouldNotUseChapterClasses` in `ArchitectureTest.java`, which asserts `core` sub-packages must not depend on chapter code. This is very likely why several later chapters (ch6/ch7) end up importing ch4 classes transitively.

**Why fix it:**
`core` is meant to be shared, chapter-independent infrastructure. A shared trainer interface depending on one specific chapter's animation type undermines that boundary and forces unrelated chapters to pull in ch4 just to implement a "shared" interface.

**Where:**
`src/main/java/core/gridrl/TrainerGridI.java:3,17`; rule defined in `src/test/java/archunit/ArchitectureTest.java` (`coreSubFoldersShouldNotUseChapterClasses`).

**Action:**
Move `AnimationGridI` (and its companion `AnimationDummy`) from `chapters.ch4.domain.animation` into `core.gridrl` (or another shared package), then update the ~8 call sites that reference the old location.

---

## 5. Byte-identical duplicate `MatrixListUtil` class, one copy unused

**Severity:** Medium
**Effort:** Tiny
**Priority:** 4.0

**Problem:**
`core/foundation/util/collections/MatrixListUtil.java` and `core/foundation/util/math/MatrixListUtil.java` are identical except for the package declaration. A project-wide search found zero references to `core.foundation.util.collections.MatrixListUtil` — only the `util.math` copy is actually used.

**Why fix it:**
Dead duplicate code is a maintenance trap: a future fix or change applied to one copy (e.g. the used one) can silently miss the other, and anyone editing the wrong copy wastes time on code that isn't called.

**Where:**
`src/main/java/core/foundation/util/collections/MatrixListUtil.java` (delete); `src/main/java/core/foundation/util/math/MatrixListUtil.java` (keep).

**Action:**
Delete `core/foundation/util/collections/MatrixListUtil.java`.

---

## 6. Unused imports, including stale cross-chapter (`ch11`→`ch12`) copy-paste leftovers

**Severity:** Medium
**Effort:** Small
**Priority:** 2.8

**Problem:**
Widespread unused imports across the codebase (dozens of instances). The most notable subset: `chapters/ch12/animation/AnimationPendulum.java` imports `AgentLunar`, `StateLunar`, and `MultiStepResult` from `chapters.ch11` (Lunar Lander), and `PendulumParams.java` imports `StateLunar` from `ch11` — confirmed these identifiers appear only in the import lines, never used in the file bodies. This is a clear copy-paste leftover from writing ch12 off of ch11. Also `ArchitectureTest.java` imports `org.junit.jupiter.api.Disabled` but never uses `@Disabled` anywhere in the file. Other representative files: `PendulumParams.java`, `chapters/ch10/cannon/domain/trainer/TrainerCannon.java`, `chapters/ch4/implem/cliff_walk/core/EnvironmentParametersCliff.java`, `core/animation/Renderer.java`, `core/foundation/config/ConfigReader.java`.

**Why fix it:**
Beyond IDE noise, the `ch11`-in-`ch12` imports are misleading: they suggest ch12 depends on ch11's Lunar Lander domain when it doesn't, which is confusing for readers trying to understand chapter dependencies (this codebase is structured as a book, where chapter isolation matters pedagogically).

**Where:**
`chapters/ch12/animation/AnimationPendulum.java:3-5`, `chapters/ch12/animation/PendulumParams.java:3`, `test/java/archunit/ArchitectureTest.java:12`, plus ~50 other files across chapters and core.

**Action:**
Run an IDE-wide "optimize imports" pass (or `mvn` import-cleanup tooling) across the project; specifically confirm the `ch11` imports are removed from the two `ch12` animation files and `@Disabled` is removed from `ArchitectureTest.java`.

---

## 7. `ReplayBuffer.isFull()` off-by-one lets the buffer exceed its configured max size

**Severity:** Low
**Effort:** Tiny
**Priority:** 1.5

**Problem:**
`isFull()` returns `buffer.size() > maxSizeReplayBuffer()` instead of `>=`. This means the buffer is allowed to grow to `maxSizeReplayBuffer() + 1` elements before the trimming logic (`maybeDeleteOldExperience`) kicks in. The same bug is duplicated in two separate chapters' replay buffers.

**Why fix it:**
It's a small but genuine logic bug (buffer size guarantee is violated by one), and being duplicated in two files means the fix must be applied twice — worth doing both while it's fresh.

**Where:**
`src/main/java/chapters/ch12/domain/inv_pendulum/trainer/core/ReplayBuffer.java:48-50`; `src/main/java/chapters/ch14/domain/trainer/ReplayBuffer.java:72-74`.

**Action:**
Change `>` to `>=` in both `isFull()` implementations.

---

## 8. `TrainerCannon.train` uses fragile `experiences.indexOf(exp)` under an unstated precondition

**Severity:** Low
**Effort:** Tiny
**Priority:** 1.2

**Problem:**
The loop index is computed via `experiences.indexOf(exp)`, which only returns the correct value under a `t==0`-only precondition (i.e. relies on no duplicate experiences and a specific calling context that isn't enforced). It's also an O(n²) anti-pattern for what should be a simple indexed loop.

**Why fix it:**
If the precondition is ever violated (e.g. a duplicate/equal experience object appears in the list), `indexOf` silently returns the wrong index, corrupting the training loop without any visible error — a subtle correctness risk masquerading as working code.

**Where:**
`src/main/java/chapters/ch10/cannon/domain/trainer/TrainerCannon.java:42-64`.

**Action:**
Replace `experiences.indexOf(exp)` with a standard indexed `for` loop (`for (int t = 0; t < experiences.size(); t++)`) so the index is always correct and O(1) per iteration.

---

## 9. Remove confirmed dead code (StateGrid.clip, NetSettings field, RbfNetwork resize, Discrete2DVector overload)

**Severity:** Low
**Effort:** Tiny
**Priority:** 1.0

**Problem:**
Several small pieces of dead code were found and confirmed to have zero callers:
- `StateGrid.clip(EnvironmentGridParametersI)` — explicitly marked `// TODO REMOVE METHOD`.
- `NetSettings.nofFitsPerEpoch` — unused field, marked with Swedish "remove this" comments.
- `RbfNetwork.fitFromErrors` performs a resize whose result is immediately discarded (dead work, not just a dead declaration).
- `Discrete2DVector.equals(Discrete2DVector)` — a same-type overload that is not a real `@Override` of `Object.equals`, and is never called anywhere.

**Why fix it:**
All four are confirmed unused; removing them reduces surface area and eliminates code that could mislead a reader into thinking it's load-bearing (especially the `equals` overload, which looks like it participates in equality checks but silently doesn't, since it never overrides `Object.equals(Object)`).

**Where:**
`src/main/java/core/gridrl/StateGrid.java`; `src/main/java/core/nextlevelrl/neural/NetSettings.java`; `src/main/java/core/nextlevelrl/radial_basis/RbfNetwork.java:123-130`; `src/main/java/core/foundation/gadget/vector_algebra/Discrete2DVector.java:19-21`.

**Action:**
Delete the four confirmed-dead pieces of code listed above.

---

## 10. `ProjectPropertiesReader` picture-path getters all return the wrong property key

**Severity:** Low
**Effort:** Tiny
**Priority:** 1.0

**Problem:**
`pathRbf`, `pathGradientLearning`, and `pathAdvConcepts` all read and return the `ch9_pics` property key instead of their own distinct keys. Currently this is dead code — none of these getters are called anywhere in the codebase — so it has no live impact today.

**Why fix it:**
It's a clear copy-paste mistake (three getters returning the same wrong value) that will silently produce wrong picture paths the moment any of these methods is actually wired up, unless caught first. Trivial to fix now while it's already flagged.

**Where:**
`src/main/java/core/foundation/configOld/ProjectPropertiesReader.java:82-108`.

**Action:**
Fix each getter to read its own corresponding property key instead of `ch9_pics`.

---

## Summary

| # | Action | Severity | Effort | Priority |
|---|---|---|---|---|
| 1 | Delete orphaned `chapters/ch2/deprecated` package (compile failure) | Critical | Tiny | 10.0 |
| 2 | Fix hardcoded-off hidden layers in `AgentMemory` (ch12) | High | Tiny | 7.0 |
| 3 | Fix `MathUtil.isZero/isNeg/isPos` tolerance (`Double.MIN_VALUE` → real epsilon) | High | Tiny | 7.0 |
| 4 | Fix `core.gridrl.TrainerGridI` illegal dependency on `ch4` (ArchUnit violation) | High | Small | 4.9 |
| 5 | Delete duplicate unused `MatrixListUtil` class | Medium | Tiny | 4.0 |
| 6 | Remove unused imports, incl. stale `ch11`→`ch12` copy-paste imports | Medium | Small | 2.8 |
| 7 | Fix `ReplayBuffer.isFull()` off-by-one (2 files) | Low | Tiny | 1.5 |
| 8 | Replace fragile `indexOf`-based loop index in `TrainerCannon` | Low | Tiny | 1.2 |
| 9 | Remove confirmed dead code (StateGrid, NetSettings, RbfNetwork, Discrete2DVector) | Low | Tiny | 1.0 |
| 10 | Fix `ProjectPropertiesReader` picture-path getters returning wrong key | Low | Tiny | 1.0 |
