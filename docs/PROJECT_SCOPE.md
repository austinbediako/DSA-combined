# Project Scope

## Selected local context

**University of Ghana Smart Campus Service Operations Optimizer**

The system models campus service operations at the University of Ghana,
Legon: hostels, lecture halls, labs, and shuttle stops as locations;
walkways/roads connecting them; maintenance and shuttle service requests;
and resources (technicians, porters, shuttles) that get dispatched to
handle those requests.

> Fill in: which specific campus areas/precincts will the team model
> (e.g. Legon main campus, City Campus, specific halls)?

## In-scope user journey

1. A service request is submitted (e.g. a maintenance issue in a hostel,
   or a shuttle request between two campus points) and stored in the
   database.
2. Requests are loaded into custom data structures (queues, priority
   queue/heap) and dispatched under FIFO, urgency, or priority rules.
3. The system computes routes between campus locations using the road
   network (BFS/DFS/Dijkstra) and determines reachability.
4. Resources are assigned to requests, respecting capacity/budget
   constraints (greedy/DP optimisation).
5. All actions are logged to `audit_events`; algorithm experiment
   results are recorded to `algorithm_runs`.
6. An examiner can run all of the above from a console menu, including
   search/sort demonstrations and performance experiments.

> Fill in: any campus-specific rules or constraints (e.g. shuttle
> operating hours, limited number of technicians, priority for
> emergency maintenance).

## Out of scope

- No graphical UI — console menu only, per the brief.
- No real-time GPS or live traffic data.
- No authentication/user-account system.
- No integration with actual University of Ghana IT systems.
- No use of personally identifiable student/staff data — all names and
  records must be fictional or generalised, per the brief's AI-resistance
  and localisation requirements.

## Minimum viable console demonstration

An examiner should be able to, without editing source code:

1. Load the database (creating it from `schema.sql` and seed CSVs if empty).
2. View/search locations and service requests.
3. Enqueue and dispatch a service request under at least two different
   scheduling rules (e.g. FIFO vs. priority).
4. Compute a shortest route between two locations (Dijkstra) and show
   reachable locations from a point (BFS/DFS).
5. Run a minimum spanning tree computation (Prim or Kruskal) over the
   road network.
6. Trigger at least one search/sort algorithm comparison and one
   performance experiment, with results written to `algorithm_runs` or
   exported CSV.
7. View the audit log (stack-based undo/audit trail) for recent actions.

> Fill in: exact menu structure once the console UI is implemented.
