# Reinforcement Learning Explained – Documentation Router

This repository contains the Java code accompanying the book
*Reinforcement Learning Explained*.

The repository root is the current working directory.

Main locations:

* `src/` contains Java source code.
* `run/` contains runnable Java examples.
* `docs/` contains generated documentation.
* `.claude/agents/` contains specialized Claude agents.

Use relative paths only.

Do not modify Java source code unless explicitly requested.

---

## start-use

Use the `usage-doc-writer` agent.

Create or update:

`docs/HOW_TO_USE.md`

The agent must inspect the repository before writing.

---

## start-org

Use the `architecture-doc-writer` agent.

Create or update:

`docs/CODE_ORGANIZATION.md`

The agent must inspect the repository structure under `src/` and `run/`
before writing.

---

## Chapter documentation

When the user enters:

`start-chN`

where `N` is a chapter number from 2 to 14:

1. Use the `chapter-doc-writer` agent.
2. Pass chapter number `N` to the agent.
3. Create or update `docs/chapter-NN.md`, where `NN` is the two-digit chapter number.

Examples:

* `start-ch2` → `docs/chapter-02.md`
* `start-ch7` → `docs/chapter-07.md`
* `start-ch10` → `docs/chapter-10.md`
* `start-ch14` → `docs/chapter-14.md`

The agent must inspect the relevant code under `run/` and `src/`
before writing.

---

## start-review

Use the `documentation-reviewer` agent.

Review:

* `docs/HOW_TO_USE.md`
* `docs/CODE_ORGANIZATION.md`
* `docs/chapter-02.md` through `docs/chapter-14.md`

Compare the documentation against the actual repository.

Correct:

* factual errors
* incorrect file or class names
* incorrect chapter mappings
* inconsistent terminology
* inconsistencies between documentation files

Do not modify Java source code.
