# Claude Code Setup Instructions

Copy the prompt below into Claude Code while its working directory is this repository.

---

You are preparing the repository for the **DCIT 204/308 Joint DSA Semester Project: Ghana Smart Service Operations Optimizer**.

Read `Joint_DSA_Project_Brief.md` completely before making changes. It is the source of truth. This is a repository setup task only: create the scaffold, documentation, build configuration, directories, and safe starter files. **Do not implement the data structures or algorithms themselves yet. Do not substitute assessed structures with Java collection equivalents.** The team will implement those components themselves.

## Goal

Set up a clean Java project that a 14-member team can begin working on immediately. The selected local context is:

> **University of Ghana Smart Campus Service Operations Optimizer**

The future system will manage local campus locations, road connections, service requests, resources, algorithm experiment runs, and audit events. It must eventually use a database, custom data structures, search/sort algorithms, graph algorithms, optimisation, testing, and performance evidence.

## Safety and repository rules

1. Inspect the existing repository before changing anything.
2. Do not delete, rename, or overwrite `Joint_DSA_Project_Brief.docx` or `Joint_DSA_Project_Brief.md`.
3. Do not create fake completed implementations of required data structures or algorithms. Empty source directories, package documentation, interfaces only where genuinely useful, and a minimal console entry point are acceptable.
4. Do not use `HashMap`, `TreeMap`, `PriorityQueue`, `Stack`, `ArrayDeque`, or similar built-in collections as replacements for assessed core logic.
5. Keep the project simple and console-based. The brief states that this is not a UI-design project.
6. Use Java 17 and Maven. Add only essential dependencies: JUnit 5 for tests and the SQLite JDBC driver for later database integration.

## Create this structure

```text
.
├── README.md
├── CONTRIBUTING.md
├── pom.xml
├── .gitignore
├── Joint_DSA_Project_Brief.docx
├── Joint_DSA_Project_Brief.md
├── data/
│   ├── raw/
│   ├── processed/
│   └── README.md
├── database/
│   ├── README.md
│   ├── schema.sql
│   └── seed/
├── docs/
│   ├── PROJECT_SCOPE.md
│   ├── ARCHITECTURE.md
│   ├── DATA_DICTIONARY.md
│   ├── TEAM_TASKS.md
│   ├── TESTING_AND_EVIDENCE.md
│   ├── PERFORMANCE_EXPERIMENTS.md
│   └── DEVELOPMENT_LOG.md
├── evidence/
│   ├── screenshots/
│   ├── trace-tables/
│   ├── benchmarks/
│   ├── graphs/
│   └── README.md
└── src/
    ├── main/java/gh/edu/ug/dsaoptimizer/
    │   ├── App.java
    │   ├── model/
    │   ├── structures/
    │   ├── algorithms/
    │   ├── service/
    │   ├── persistence/
    │   ├── ui/
    │   └── util/
    └── test/java/gh/edu/ug/dsaoptimizer/
        ├── structures/
        ├── algorithms/
        ├── service/
        └── persistence/
```

Use `.gitkeep` files only when Git needs to retain an empty directory.

## Required starter files

### 1. `README.md`

Write a concise project README containing:

- Project title and selected University of Ghana campus-service context.
- A one-paragraph problem description.
- The core workflow: load local data → store/reload through database → use custom structures → run algorithms → test → benchmark → save evidence.
- Technology: Java 17, Maven, SQLite, JUnit 5.
- Exact setup and run commands using Maven.
- A note that assessed data structures must be implemented by the students and not replaced with Java collection classes.
- A link to `Joint_DSA_Project_Brief.md` and the important team documents in `docs/`.

### 2. `App.java`

Create only a minimal executable console entry point. It should print the project name and a message that the system setup is complete. It must compile with Maven. Do not add menus or business logic yet.

### 3. `database/schema.sql`

Add a clearly labelled **draft schema only**, using SQLite syntax, for these tables from the brief:

- `locations`
- `roads`
- `service_requests`
- `resources`
- `algorithm_runs`
- `audit_events`

Include sensible primary keys and foreign keys. Add comments stating that the database team must review and finalise the schema before data is loaded. Do not generate fake production data.

### 4. Documentation in `docs/`

Create the following Markdown files with useful templates and checklists, not generic filler:

- `PROJECT_SCOPE.md`: selected campus-service context; in-scope user journey; out-of-scope items; minimum viable console demonstration.
- `ARCHITECTURE.md`: describe the package boundaries. UI calls services; services use algorithms and custom structures; persistence loads/saves data; model contains domain records. Include the five shared entities: `Location`, `Road`, `ServiceRequest`, `Resource`, and `AlgorithmRun`.
- `DATA_DICTIONARY.md`: provide blank/fill-in data dictionary tables for the five entities. Include the brief’s required minimum datasets: 50 locations, 100 roads, 300 service requests, 30 resources, and 30 algorithm runs.
- `TEAM_TASKS.md`: create an unassigned task board for 14 members. Include the five teams: Core Structures (3), Graph & Network (3), Algorithms & Scheduling (3), Data & Integration (2), Testing/Evidence/Presentation (3). List clear deliverables but leave member names blank.
- `TESTING_AND_EVIDENCE.md`: capture the minimum requirement of 40 unit tests, six trace tables, three proof sketches, two counterexamples, and the required edge cases.
- `PERFORMANCE_EXPERIMENTS.md`: list the required experiments, input sizes, three-run average rule, machine-specification note, CSV storage, and graph requirements from the brief.
- `DEVELOPMENT_LOG.md`: a dated weekly/daily log template with columns for completed work, decisions, blockers, evidence captured, and next action.

### 5. `CONTRIBUTING.md`

Create short, beginner-friendly Git guidance:

- Pull `main` before starting.
- Create a branch such as `feature/custom-queue`, `feature/dijkstra`, `docs/report-outline`, or `test/binary-search`.
- Make small commits using `feat:`, `test:`, `docs:`, or `fix:` prefixes.
- Open a pull request and require one teammate review before merge.
- No direct pushes to `main`.
- A task is not complete without code, relevant tests, a short complexity note where applicable, and saved evidence.

### 6. Evidence and data readmes

Explain naming conventions for CSVs, screenshots, trace tables, benchmark files, and graphs. State that local data must use Ghana/University of Ghana names, must avoid personal data, and must record how it was constructed from local knowledge.

## Required validation

1. Run `mvn test` and fix any setup errors.
2. Confirm `mvn exec:java` works, or provide an equally simple documented Maven command to run `App.java`.
3. Confirm `git status` shows only the intended scaffold files.
4. In your final response, give a compact list of created files, the command to run the starter application, and anything the team must decide before implementation begins.

Do not make a commit unless I explicitly request one.

---

After Claude Code completes this setup, the team should fill in the shared data dictionary, confirm the final schema, assign member names in `docs/TEAM_TASKS.md`, and begin implementation in small reviewed branches.
