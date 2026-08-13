# Data

Local CSV data used to seed the database, per `docs/DATA_DICTIONARY.md`
and `Joint_DSA_Project_Brief.md` §4.

- `raw/` — CSV files as first constructed/collected (locations, roads,
  service_requests, resources, algorithm_runs).
- `processed/` — cleaned/validated CSV files ready for import into
  SQLite, if a separate cleaning step is needed.

## Rules for this data

1. **Local names only.** Locations, routes, and service categories must
   use Ghana / University of Ghana names and terminology (e.g. actual
   hall names, campus areas), not generic placeholder data.
2. **No personal data.** Do not include real student/staff names, ID
   numbers, phone numbers, or any other personally identifiable
   information, per the brief's AI-resistance and localisation
   requirements (§2).
3. **Document provenance.** Each CSV (or this README) must record how
   the data was constructed from local knowledge — e.g. "hostel names
   sourced from public UG campus directory; travel times estimated by
   team members familiar with the routes."

## Naming convention

```
data/raw/<entity>_raw.csv          e.g. locations_raw.csv
data/processed/<entity>.csv        e.g. locations.csv
```

Column headers should match the field names in
`docs/DATA_DICTIONARY.md` and `database/schema.sql`.

## Provenance log

| File | Source | Notes |
|---|---|---|
| `locations.csv` (50 records) | AI-assisted web research prompted against public University of Ghana, Legon campus references (official site, campus maps), cross-checked for duplicate ids/names and plausible lat/long bounds (5.60–5.70 N, -0.22–-0.15 E) before acceptance. | No personal data. Coordinates for some minor buildings are the AI's best estimate rather than confirmed survey data — team should spot-check a sample against a real campus map before final submission. |

Update this table whenever a new CSV is added (roads, service_requests,
resources), noting how it was produced and any caveats.
