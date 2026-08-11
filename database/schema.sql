-- =====================================================================
-- Ghana Smart Campus Service Operations Optimizer
-- DRAFT SCHEMA ONLY -- SQLite dialect
--
-- STATUS: DRAFT. The Data & Integration team must review, adjust field
-- types/constraints, and finalise this schema before any real data is
-- loaded. Do not treat this as production-ready.
-- =====================================================================

PRAGMA foreign_keys = ON;

-- Campus locations: hostels, lecture halls, labs, shuttle stops, etc.
CREATE TABLE IF NOT EXISTS locations (
    location_id     INTEGER PRIMARY KEY AUTOINCREMENT,
    name             TEXT NOT NULL,
    area             TEXT NOT NULL,          -- e.g. Legon campus zone/precinct
    type             TEXT NOT NULL,          -- e.g. hostel, lab, lecture hall, shuttle stop
    latitude         REAL,
    longitude        REAL
);

-- Weighted road/path edges between locations.
CREATE TABLE IF NOT EXISTS roads (
    road_id              INTEGER PRIMARY KEY AUTOINCREMENT,
    from_location_id     INTEGER NOT NULL,
    to_location_id       INTEGER NOT NULL,
    distance             REAL NOT NULL,       -- in metres or km, TBD by team
    travel_time          REAL NOT NULL,       -- in minutes
    road_condition_weight REAL NOT NULL,      -- penalty factor, e.g. 1.0 = ideal
    FOREIGN KEY (from_location_id) REFERENCES locations(location_id),
    FOREIGN KEY (to_location_id) REFERENCES locations(location_id)
);

-- Service requests: maintenance jobs, lab-resource movement, shuttle requests, etc.
CREATE TABLE IF NOT EXISTS service_requests (
    request_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    source_location_id      INTEGER NOT NULL,
    destination_location_id INTEGER NOT NULL,
    category         TEXT NOT NULL,          -- e.g. maintenance, shuttle, lab-resource
    urgency          TEXT NOT NULL,          -- e.g. LOW, MEDIUM, HIGH, CRITICAL
    time_submitted   TEXT NOT NULL,          -- ISO-8601 timestamp
    deadline          TEXT,                   -- ISO-8601 timestamp, nullable
    status            TEXT NOT NULL DEFAULT 'PENDING', -- PENDING, ASSIGNED, IN_PROGRESS, DONE, CANCELLED
    FOREIGN KEY (source_location_id) REFERENCES locations(location_id),
    FOREIGN KEY (destination_location_id) REFERENCES locations(location_id)
);

-- Assignable resources: shuttles, maintenance staff, porters, equipment, etc.
CREATE TABLE IF NOT EXISTS resources (
    resource_id       INTEGER PRIMARY KEY AUTOINCREMENT,
    type              TEXT NOT NULL,          -- e.g. shuttle, technician, porter
    home_location_id  INTEGER NOT NULL,
    capacity          INTEGER NOT NULL,
    availability_status TEXT NOT NULL DEFAULT 'AVAILABLE', -- AVAILABLE, BUSY, OFFLINE
    FOREIGN KEY (home_location_id) REFERENCES locations(location_id)
);

-- Empirical performance measurements from algorithm experiments.
CREATE TABLE IF NOT EXISTS algorithm_runs (
    run_id            INTEGER PRIMARY KEY AUTOINCREMENT,
    algorithm_name    TEXT NOT NULL,          -- e.g. quicksort, dijkstra, kruskal
    input_size        INTEGER NOT NULL,
    time_ns           INTEGER NOT NULL,
    memory_kb         INTEGER,
    date_run          TEXT NOT NULL           -- ISO-8601 timestamp
);

-- Stack-based undo/audit log of significant system events.
CREATE TABLE IF NOT EXISTS audit_events (
    event_id          INTEGER PRIMARY KEY AUTOINCREMENT,
    event_type        TEXT NOT NULL,          -- e.g. ASSIGN, UNDO_ASSIGN, STATUS_CHANGE
    related_request_id INTEGER,
    description       TEXT NOT NULL,
    event_time        TEXT NOT NULL,          -- ISO-8601 timestamp
    FOREIGN KEY (related_request_id) REFERENCES service_requests(request_id)
);
