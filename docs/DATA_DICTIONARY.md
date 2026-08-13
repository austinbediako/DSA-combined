# Data Dictionary

Fill in every column marked TBD. Field names should match `model/` classes
and `database/schema.sql` exactly. Update `database/schema.sql` if the
team changes types here (or vice versa) so the two stay in sync.

Per the brief (§4), minimum record counts: 50 locations, 100 roads,
300 service requests, 30 resources, 30 algorithm runs.

## `locations` (minimum 50 records) — DONE, see `data/processed/locations.csv`

| Field | Type | Description | Example |
|---|---|---|---|
| location_id | INTEGER (PK) | Unique identifier | 1 |
| name | TEXT | Local place name | "Commonwealth Hall" |
| area | TEXT | Campus zone/precinct | "Legon Hill" |
| type | TEXT | Hostel / Lab / Lecture Hall / Shuttle Stop / Clinic / Admin / Library / Dining / Recreation / Sports | "Hostel" |
| latitude | REAL | Coordinate | 5.6521 |
| longitude | REAL | Coordinate | -0.1874 |

## `roads` (minimum 100 records) — DONE, see `data/processed/roads.csv`

| Field | Type | Description | Example |
|---|---|---|---|
| road_id | INTEGER (PK) | Unique identifier | 1 |
| from_location_id | INTEGER (FK -> locations) | Edge start | 40 |
| to_location_id | INTEGER (FK -> locations) | Edge end | 41 |
| distance | REAL | Distance in metres | 1300 |
| travel_time | REAL | Minutes | 4 |
| road_condition_weight | REAL | Penalty factor (1.0 = ideal, higher = worse) | 1.2 |

## `service_requests` (minimum 300 records) — DONE, see `data/processed/service_requests.csv`

| Field | Type | Description | Example |
|---|---|---|---|
| request_id | TEXT (PK) | Unique identifier, e.g. "REQ-001" | "REQ-001" |
| source_location_id | INTEGER (FK) | Request origin | 1 |
| destination_location_id | INTEGER (FK) | Request destination | 1 |
| category | TEXT | plumbing / electrical / carpentry / IT_network / lab_equipment / waste_management / shuttle_transit | "plumbing" |
| urgency | TEXT | LOW / MEDIUM / HIGH / CRITICAL | "MEDIUM" |
| time_submitted | TEXT (ISO-8601) | Submission timestamp | "2026-08-01T07:15:00Z" |
| deadline | TEXT (ISO-8601, nullable) | Deadline if any | "2026-08-02T07:15:00Z" |
| status | TEXT | PENDING / ASSIGNED / IN_PROGRESS / DONE / CANCELLED | "DONE" |

## `resources` (minimum 30 records) — DONE, see `data/processed/resources.csv`

| Field | Type | Description | Example |
|---|---|---|---|
| resource_id | TEXT (PK) | Unique identifier, e.g. "RES-001" | "RES-001" |
| type | TEXT | Shuttle-Bus / Shuttle-Van / Technician-Electrical / Technician-Plumbing / IT-Support-Agent / Carpenter / Lab-Equipment-Tech / Porter / Maintenance-Crew / Waste-Management-Truck | "Shuttle-Bus" |
| home_location_id | INTEGER (FK) | Home base | 40 |
| capacity | INTEGER | Capacity (seats, staff count, load, etc.) | 45 |
| availability_status | TEXT | AVAILABLE / BUSY / OFFLINE | "AVAILABLE" |

## `algorithm_runs` (minimum 30 records)

| Field | Type | Description | Example (fill in) |
|---|---|---|---|
| run_id | INTEGER (PK) | Unique identifier | |
| algorithm_name | TEXT | e.g. quicksort, dijkstra, kruskal | |
| input_size | INTEGER | n for this run | |
| time_ns | INTEGER | Measured runtime (nanoseconds) | |
| memory_kb | INTEGER | Measured memory (nullable if not captured) | |
| date_run | TEXT (ISO-8601) | When the experiment ran | |

## Index-number-derived parameters (brief §2, requirement 3)

At least three algorithm parameters must be derived from team members'
index numbers (e.g. priority weight, route penalty, hash-table size,
random seed, budget constraint). Record them here once decided:

| Parameter | Derived from | Formula | Value |
|---|---|---|---|
| | | | |
| | | | |
| | | | |
