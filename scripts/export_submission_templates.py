#!/usr/bin/env python3
"""
Translates the working dataset in data/processed/ into the column
names, ID style, and value formats shown in templates/*.csv, and
writes the result to data/submission_templates/.

This does NOT touch the working database, schema, models, or app --
it is a read-only export for submission alongside the real system.
Re-run any time data/processed/ changes:

    python3 scripts/export_submission_templates.py
"""
import csv
import os

SRC = "data/processed"
OUT = "data/submission_templates"

os.makedirs(OUT, exist_ok=True)


def read_rows(name):
    with open(os.path.join(SRC, name), newline="", encoding="utf-8") as f:
        return list(csv.DictReader(f))


def write_rows(name, header, rows):
    path = os.path.join(OUT, name)
    with open(path, "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(header)
        w.writerows(rows)
    print(f"wrote {path} ({len(rows)} rows)")


locations = read_rows("locations.csv")
roads = read_rows("roads.csv")
resources = read_rows("resources.csv")
requests = read_rows("service_requests.csv")

# Real location_id (int) -> template-style "L001" id.
loc_id_map = {
    row["location_id"]: f"L{i + 1:03d}"
    for i, row in enumerate(locations)
}

# --- locations_template.csv ---
loc_rows = []
for row in locations:
    loc_rows.append([
        loc_id_map[row["location_id"]],
        row["name"],
        row["area"],
        row["type"],           # type -> location_type
        row["latitude"],       # latitude -> x_coord
        row["longitude"],      # longitude -> y_coord
    ])
write_rows(
    "locations_template.csv",
    ["location_id", "name", "area", "location_type", "x_coord", "y_coord"],
    loc_rows,
)

# --- roads_template.csv ---
road_rows = []
for i, row in enumerate(roads):
    distance_km = float(row["distance"]) / 1000.0  # our distance is in metres
    road_rows.append([
        f"R{i + 1:03d}",
        loc_id_map[row["from_location_id"]],
        loc_id_map[row["to_location_id"]],
        f"{distance_km:.3f}",
        row["travel_time"],
        row["road_condition_weight"],
    ])
write_rows(
    "roads_template.csv",
    ["road_id", "from_location_id", "to_location_id", "distance_km",
     "travel_time_min", "condition_weight"],
    road_rows,
)

# --- resources_template.csv ---
# resource_id in the template is <first letter of type><seq>, e.g. V001, R001.
resource_rows = []
type_seq = {}
for row in resources:
    letter = row["type"][0].upper()
    type_seq[letter] = type_seq.get(letter, 0) + 1
    new_id = f"{letter}{type_seq[letter]:03d}"
    resource_rows.append([
        new_id,
        row["type"],                       # type -> resource_type
        loc_id_map[row["home_location_id"]],
        row["capacity"],
        row["availability_status"],
    ])
write_rows(
    "resources_template.csv",
    ["resource_id", "resource_type", "home_location_id", "capacity",
     "availability_status"],
    resource_rows,
)

# --- service_requests_template.csv ---
# Our urgency is a 4-level enum; the template uses a 1-5 numeric scale.
URGENCY_MAP = {"LOW": 1, "MEDIUM": 2, "HIGH": 4, "CRITICAL": 5}
# Our status enum matches the template's except PENDING -> NEW.
STATUS_MAP = {"PENDING": "NEW"}


def to_template_timestamp(ts):
    # "2026-08-01T07:15:00Z" -> "2026-08-01T07:15"
    if not ts:
        return ts
    return ts.replace("Z", "")[:16]


request_rows = []
for i, row in enumerate(requests):
    request_rows.append([
        f"Q{i + 1:03d}",
        loc_id_map[row["source_location_id"]],
        loc_id_map[row["destination_location_id"]],
        row["category"],
        URGENCY_MAP[row["urgency"]],
        to_template_timestamp(row["time_submitted"]),
        to_template_timestamp(row["deadline"]),
        STATUS_MAP.get(row["status"], row["status"]),
    ])
write_rows(
    "service_requests_template.csv",
    ["request_id", "source_location_id", "destination_location_id",
     "category", "urgency", "time_submitted", "deadline", "status"],
    request_rows,
)
