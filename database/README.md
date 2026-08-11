# Database

This directory holds the SQLite schema and seed data for the Smart Campus
Service Operations Optimizer.

- `schema.sql` — draft DDL for `locations`, `roads`, `service_requests`,
  `resources`, `algorithm_runs`, and `audit_events`. **Draft only** — the
  Data & Integration team must review and finalise it (types, constraints,
  indexes) before real data is loaded. See `docs/DATA_DICTIONARY.md`.
- `seed/` — CSV seed files used to populate the database for development
  and testing. Do not commit generated `.db`/`.sqlite` files (see
  `.gitignore`); only the SQL and CSV source-of-truth files belong here.

## Applying the schema (SQLite CLI)

```bash
sqlite3 dsa_optimizer.db < database/schema.sql
```

The Java application also creates/uses the schema via JDBC
(`org.xerial:sqlite-jdbc`, already declared in `pom.xml`) — see
`gh.edu.ug.dsaoptimizer.persistence`.
