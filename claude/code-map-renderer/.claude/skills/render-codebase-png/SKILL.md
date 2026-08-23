---
name: render-codebase-png
description: Render a codebase architecture DOT specification as a PNG diagram (results/code-overview.png) using Graphviz. Use after build-codebase-map to produce the final visual output.
---

# Purpose

Render the analyzed Java codebase architecture as a clear PNG image.

# Input

Use the architectural representation created from the codebase located at:

`C:\JavaCode\relexp-java`

# Output

Create:

`results/code-overview.png`

Also create:

`results/code-overview.dot`

when Graphviz is used.

The PNG is the primary deliverable.

# Rendering tool

Prefer Graphviz.

Check whether Graphviz is available locally.

If available, generate a DOT file and render it using Graphviz.

Example:

`dot -Tpng results/code-overview.dot -o results/code-overview.png`

If Graphviz is unavailable, use another local programmatic approach capable of creating a clear architecture diagram.

# Visual requirements

The diagram must be readable on a normal computer screen.

It should:

- have a clear title
- use readable text
- use short labels
- visually group related components
- clearly show relationship direction
- minimize crossing arrows
- avoid unnecessary decoration
- remain understandable without reading the source code

# Level of detail

Keep the diagram high-level.

Prefer approximately 5–15 major nodes.

Do not include:

- every Java class
- every method
- getters and setters
- trivial utility classes
- test classes unless they are important to understanding the architecture
- minor implementation dependencies

Group related classes into packages or subsystems where appropriate.

# Layout

Prefer:

`rankdir=LR`

when the codebase has a clear execution or processing flow.

Prefer:

`rankdir=TB`

when the system is more naturally represented as layers.

Choose whichever produces the clearest diagram.

Place the highest level of the hierarchy — application entry points (e.g. runners, `main` classes) — at the top of the image, with dependent/lower-level components ranked below them. In `rankdir=TB` this means entry-point nodes belong in the first rank; in `rankdir=LR` it means they belong in the leftmost rank. Use `rank=source` (or an explicit subgraph with `rank=same`) on the entry-point node(s) if Graphviz does not place them there automatically.

# Node content

Each major node should normally contain:

- component/package/class name
- optionally one short responsibility description

Example:

```text
QLearningAgent
Selects actions and updates Q-values
```

# Visual style

Before rendering, look at the example images in:

`figs/`

Use them only as *visual style* inspiration (colors, shapes, spacing, typography) — never as a source of structure or content. The diagram's structure must come only from `results/code-overview.dot` / the actual Java codebase.

Style cues to take from `figs/`:

- Rounded rectangle nodes (`shape=box, style="rounded,filled"`), not sharp corners and not ellipses.
- Soft, pastel/muted fill colors, with a color assigned consistently by node category or hierarchical level (e.g. entry points, core packages, utility/support packages each get their own color) rather than one flat color for every node. Give each node a border color that is a darker shade of its fill.
- A clean sans-serif font (e.g. Helvetica/Arial) for all text, with the node title bolder/larger than any secondary description line.
- Generous whitespace and consistent spacing between nodes (`nodesep`, `ranksep`) — avoid a cramped layout.
- Orthogonal / right-angle connector lines (`splines=ortho` or `splines=polyline`) rather than curved diagonal edges, matching the clean right-angle routing seen in `figs/UML-Diagram.png`.
- Plain white background, no drop shadows, no 3D effects, no unnecessary decoration.
- When a node groups a class's members (methods/fields), it's acceptable to divide the node into a header section (name) and a body section (short bullet list), similar to `figs/images.png`, but only when that level of detail is warranted — keep the overall diagram high-level per the "Level of detail" section above.

Apply these as Graphviz global/node/edge attributes (e.g. `graph [splines=ortho, nodesep=0.6, ranksep=0.8, bgcolor=white]`, `node [shape=box, style="rounded,filled", fontname="Helvetica"]`, `edge [color="#555555"]`) rather than trying to replicate the example images pixel-for-pixel.