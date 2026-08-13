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
| `resources.csv` (30 records) | AI-generated synthetic operational data (shuttles, technicians, porters, etc.), grounded in `locations.csv`. | No personal data — resource labels only (e.g. "Technician-Electrical"), no named individuals. Programmatically validated: exact count, no duplicate ids, all `home_location_id` values resolve to a real location, valid `availability_status` values. IDs are `TEXT` (e.g. "RES-001"); `database/schema.sql` was updated to match. |
| `service_requests.csv` (300 records) | AI-generated synthetic operational data spread across a simulated 2-week window (Aug 1–13, 2026), grounded in `locations.csv`. Urgency/status weighted realistically (mostly LOW/MEDIUM urgency; older requests DONE, recent ones PENDING/ASSIGNED/IN_PROGRESS). | No personal data. Programmatically validated: exact count, no duplicate ids, all source/destination location ids resolve, valid urgency/status enums, no malformed or inverted (deadline-before-submission) dates. IDs are `TEXT` (e.g. "REQ-001"); `database/schema.sql` was updated to match. |
| `roads.csv` (101 records) | AI-generated synthetic road/walkway edges between the 50 real locations, grounded in their relative lat/long positions and area groupings. | No personal data. Programmatically validated: no duplicate road_ids, no invalid location references, no self-loops, graph is fully connected (every location reachable from every other), distance/travel_time pace is realistic (40-400 m/min, covering both walking and shuttle routes), road_condition_weight within [1.0, 2.0]. One duplicate edge (same location pair connected twice with different values, unexplained) was found and removed — source data had 102 rows, 101 kept. Distances/times/weights are explicitly synthetic estimates per the AI's own disclosure, not ground-measured. |

Update this table whenever a new CSV is added (algorithm_runs), noting
how it was produced and any caveats.
