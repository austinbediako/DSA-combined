# Data Dictionary

Fill in every column marked TBD. Field names should match `model/` classes
and `database/schema.sql` exactly. Update `database/schema.sql` if the
team changes types here (or vice versa) so the two stay in sync.

Per the brief (§4), minimum record counts: 50 locations, 100 roads,
300 service requests, 30 resources, 30 algorithm runs.

## `locations` (minimum 50 records)

| Field | Type | Description | Example (fill in) |
|---|---|---|---|
| location_id | INTEGER (PK) | Unique identifier | |
| name | TEXT | Local place name | e.g. "Commonwealth Hall" |
| area | TEXT | Campus zone/precinct | |
| type | TEXT | hostel / lab / lecture hall / shuttle stop / ... | |
| latitude | REAL | Local coordinate (or synthetic) | |
| longitude | REAL | Local coordinate (or synthetic) | |

## `roads` (minimum 100 records)

| Field | Type | Description | Example (fill in) |
|---|---|---|---|
| road_id | INTEGER (PK) | Unique identifier | |
| from_location_id | INTEGER (FK -> locations) | Edge start | |
| to_location_id | INTEGER (FK -> locations) | Edge end | |
| distance | REAL | Distance (unit: TBD) | |
| travel_time | REAL | Minutes | |
| road_condition_weight | REAL | Penalty factor (1.0 = ideal) | |

## `service_requests` (minimum 300 records)

| Field | Type | Description | Example (fill in) |
|---|---|---|---|
| request_id | INTEGER (PK) | Unique identifier | |
| source_location_id | INTEGER (FK) | Request origin | |
| destination_location_id | INTEGER (FK) | Request destination | |
| category | TEXT | maintenance / shuttle / lab-resource / ... | |
| urgency | TEXT | LOW / MEDIUM / HIGH / CRITICAL (confirm scale) | |
| time_submitted | TEXT (ISO-8601) | Submission timestamp | |
| deadline | TEXT (ISO-8601, nullable) | Deadline if any | |
| status | TEXT | PENDING / ASSIGNED / IN_PROGRESS / DONE / CANCELLED | |

## `resources` (minimum 30 records)

| Field | Type | Description | Example (fill in) |
|---|---|---|---|
| resource_id | INTEGER (PK) | Unique identifier | |
| type | TEXT | shuttle / technician / porter / ... | |
| home_location_id | INTEGER (FK) | Home base | |
| capacity | INTEGER | Capacity (people, load, etc.) | |
| availability_status | TEXT | AVAILABLE / BUSY / OFFLINE | |

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
