```markdown
# CLAUDE.md

## start-review

Perform a pragmatic code review of the project.

Use these agents in order:

1. `code-correctness-reviewer`
2. `code-design-reviewer`
3. `code-review-prioritizer`

The first two agents should independently review the codebase and produce findings.

The `code-review-prioritizer` should then combine, verify, deduplicate, and rank the findings.

The final result must contain exactly 10 prioritized actions.

Priority must strongly consider BOTH:

- severity of the problem
- estimated time required to fix it

High-impact, quick fixes should rank highest.

Problems that require many hours of work should normally receive much lower priority, even if they are technically important.

Large refactorings should only rank highly if they address a critical problem.

Pay particular attention to:

- correctness bugs
- obvious implementation mistakes
- unused imports
- incorrect imports
- wrong dependencies
- incorrect dependencies on previous chapters
- code that should use shared/common functionality instead
- copy-paste mistakes between chapters
- stale names after refactoring
- dead or unused code
- misleading comments
- inconsistent chapter structure

Do not modify source code.

Save the final prioritized review to:

`review/results/code-review.md`
```
