# Study Guide — Data Structures & Algorithms in This Project

A plain-language companion for the team, built around what we actually
implemented — not a generic DSA textbook chapter. Every entry follows
the same pattern: **what it is**, **why we needed it here**, **how it
works** (simple pseudocode), **how we actually built it** (real file,
real design choices), and **complexity**. Use this to prepare for
oral defense — especially the structure/algorithm you're personally
credited with in `DOCUMENTATION.md` §11.

> Every code reference below is a real path you can open right now,
> e.g. `src/main/java/gh/edu/ug/dsaoptimizer/structures/Stack.java`.

## Contents

**Part 1 — Data Structures**
1. [Dynamic Array](#1-dynamic-array)
2. [Doubly Linked List](#2-doubly-linked-list)
3. [Stack](#3-stack)
4. [Queue & Circular Queue](#4-queue--circular-queue)
5. [Deque](#5-deque)
6. [Priority Queue / Heap](#6-priority-queue--heap)
7. [Hash Table](#7-hash-table)
8. [Binary Search Tree (BST)](#8-binary-search-tree-bst)
9. [Red-Black Tree](#9-red-black-tree)
10. [B-Tree](#10-b-tree)
11. [Disjoint Set (Union-Find)](#11-disjoint-set-union-find)
12. [Set / Map](#12-set--map)
13. [Graph](#13-graph)

**Part 2 — Algorithms**
14. [Linear Search](#14-linear-search)
15. [Binary Search](#15-binary-search)
16. [Selection Sort](#16-selection-sort)
17. [Insertion Sort](#17-insertion-sort)
18. [Merge Sort](#18-merge-sort)
19. [Quicksort](#19-quicksort)
20. [Greedy Algorithm](#20-greedy-algorithm)
21. [Dynamic Programming (0/1 Knapsack)](#21-dynamic-programming-01-knapsack)
22. [Breadth-First Search (BFS)](#22-breadth-first-search-bfs)
23. [Depth-First Search (DFS)](#23-depth-first-search-dfs)
24. [Dijkstra's Algorithm](#24-dijkstras-algorithm)
25. [Prim's & Kruskal's Algorithms (MST)](#25-prims--kruskals-algorithms-mst)

---

# Part 1 — Data Structures

## 1. Dynamic Array

**What it is:** an array that grows automatically when it runs out of
room, instead of forcing you to pick a fixed size upfront.

**Why we needed it:** it's the backbone structure everything else in
the project builds on — `HashTable`'s buckets, `Graph`'s node lists,
`Knapsack`'s DP table, and generally anywhere we need a growable list
without reaching for `java.util.ArrayList` (which is banned for
assessed core logic).

**How it works:**
```
add(value):
    if size == capacity:
        create a new array twice the size
        copy every element across
        replace the old array with the new one
    store value at index [size]
    size = size + 1
```

**Our implementation** (`structures/DynamicArray.java`): backed by a
plain `Object[]`, doubles capacity on overflow, and **shrinks back to
half** once usage drops below a quarter full after a `remove()` — most
textbook versions skip the shrink step, we added it to avoid holding
onto a huge array after a bunch of deletions.

**Complexity:** `add` is O(1) *amortized* (occasional O(n) resize,
averaged out over many calls), `get`/`set` are O(1), `remove` is O(n)
(has to shift everything after the removed slot left by one).

---

## 2. Doubly Linked List

**What it is:** a chain of nodes where each node points to both its
next *and* previous neighbour — unlike an array, nodes don't need to
sit next to each other in memory.

**Why we needed it:** it's what `Stack`, `Queue`, and `Deque` are all
secretly built on top of in this project — see below.

**How it works:**
```
Node: { value, prev pointer, next pointer }

addFirst(value):
    new node's next = current head
    current head's prev = new node
    head = new node

addLast(value):  # mirror image, at the tail end
```

**Our implementation** (`structures/DoublyLinkedList.java`): keeps
both `head` and `tail` references so adding/removing at *either* end
is O(1) — that's the entire reason `Stack`/`Queue`/`Deque` delegate to
this instead of a singly-linked list. Also has a `toArray()` method
(see the note under Hash Table about why it returns `Object[]`).

**Complexity:** add/remove at either end: O(1). Access by index:
O(min(index, size-index)) — it walks from whichever end is closer.

---

## 3. Stack

**What it is:** Last-In-First-Out (LIFO) — the last thing you put in
is the first thing you take out. Think of a stack of plates.

**Why we needed it:** `algorithms/DFS.java` uses it to do depth-first
graph traversal *iteratively* instead of recursively (avoids stack
overflow on a big graph).

**How it works:**
```
push(value): put value on top
pop(): remove and return the top value
peek(): look at the top value without removing it
```

**Our implementation** (`structures/Stack.java`): a thin wrapper
around `DoublyLinkedList` — `push` = `addFirst`, `pop` =
`removeFirst`. We didn't reinvent linked-node logic twice; we reused
what already worked.

**Complexity:** push/pop/peek are all O(1).

---

## 4. Queue & Circular Queue

**What it is:** First-In-First-Out (FIFO) — the first thing in is the
first thing out, like a line of people waiting.

A **circular queue** is the same idea but with a *fixed* size, where
the "front" and "rear" positions wrap back around to index 0 once they
reach the end of the array — like a clock face instead of a straight
line.

**Why we needed it:** the plain `Queue` models **FIFO dispatch** (brief
M5) — the simplest possible service rule: handle requests in the order
they arrived. The `CircularQueue` models a **fixed number of
concurrent dispatch slots** in `service/SchedulingEngine.java` — e.g.
"we only have 5 technicians active at once."

**How it works (circular queue):**
```
enqueue(value):
    if full: reject
    data[rear] = value
    rear = (rear + 1) mod capacity
    size += 1

dequeue():
    if empty: reject
    value = data[front]
    front = (front + 1) mod capacity
    size -= 1
    return value
```

**Our implementation:**
- `structures/Queue.java` — another thin wrapper over
  `DoublyLinkedList` (`enqueue` = `addLast`, `dequeue` = `removeFirst`).
- `structures/CircularQueue.java` — a **fixed-size array**, not a
  linked list, using the modulo wrap-around trick above so `front`
  and `rear` cycle back to 0 instead of needing to shift elements.

**Complexity:** enqueue/dequeue are O(1) for both versions.

---

## 5. Deque

**What it is:** "double-ended queue" — you can add or remove from
*either* end, front or back.

**Why we needed it:** models **urgent requests jumping the queue** in
`SchedulingEngine.java` — a CRITICAL request can be pushed to the
*front* (`addFirst`) so it dispatches next, while normal requests join
the back (`addLast`) like an ordinary queue.

**How it works:**
```
addFirst(value), addLast(value)
removeFirst(), removeLast()
```

**Our implementation** (`structures/Deque.java`): same pattern as
Stack/Queue — wraps `DoublyLinkedList`, since a doubly linked list
*already* supports O(1) operations at both ends for free.

**Complexity:** all four operations are O(1).

---

## 6. Priority Queue / Heap

**What it is:** not FIFO or LIFO — instead, whatever has the
**smallest** (or largest) priority value always comes out first,
regardless of insertion order. Implemented as a **binary heap**: a
tree squeezed into an array, where every parent is ≤ (or ≥) its
children.

**Why we needed it:** this is the engine behind **priority dispatch**
(most urgent request first) and Dijkstra's/Prim's algorithms (always
process the closest/cheapest thing next).

**How it works (min-heap):**
```
offer(value):
    put value at the end of the array
    "sift up": while value < its parent, swap with parent

poll():
    save the root (index 0) — this is the minimum
    move the last element to the root
    "sift down": swap with the smaller child until heap order is restored
    return the saved minimum
```

**Our implementation** (`structures/PriorityQueueHeap.java`):
array-backed (`Object[]`), takes an optional `Comparator` so it can
order by anything — urgency+time for dispatch, distance for Dijkstra,
edge weight for Prim. `SchedulingEngine` orders by
`(urgency descending, submission time ascending)` — most urgent first,
ties broken by whoever arrived earlier.

**Complexity:** offer/poll are O(log n) — each sift walks the height
of the tree. Peek is O(1).

---

## 7. Hash Table

**What it is:** stores key→value pairs so you can look something up by
key in roughly constant time, by converting the key into an array
index via a **hash function**.

**Why we needed it:** it's the foundation for `Set`/`Map`,
`DisjointSet`'s internal parent/rank tracking, and `Graph`'s adjacency
lists (each node maps to its list of neighbours).

**How it works (separate chaining):**
```
indexFor(key) = hash(key) mod capacity

put(key, value):
    idx = indexFor(key)
    if key already exists in bucket[idx]'s chain: update it
    else: insert a new node at the FRONT of bucket[idx]'s chain

get(key):
    idx = indexFor(key)
    walk bucket[idx]'s chain looking for a matching key
```

**Our implementation** (`structures/HashTable.java`): separate
chaining (a linked chain of entries per bucket) rather than open
addressing. Auto-resizes (doubles capacity) once load factor exceeds
0.75 — but we also added a `setAutoResize(false)` escape hatch and a
`collisionCount()`/`loadFactor()` pair specifically to run the
hash-load-factor performance experiment (`evidence/benchmarks/hash_load_factor.csv`),
where we needed to *hold* the table at a high load factor on purpose
to observe collisions instead of letting it grow itself out of the
problem.

> **A recurring gotcha worth understanding**: several methods here
> (and in `Graph`, `BST`, `BTreeMap`) return `Object[]` instead of a
> generic `K[]`. That's not sloppiness — Java cannot safely create an
> array of a generic type at runtime without a `Class<K>` token, so
> `(K[]) new Object[n]` *compiles* but throws `ClassCastException` the
> first time a caller assigns it to a concretely-typed array variable.
> We hit this exact bug while building `Graph.shortestPath()` and
> fixed it by switching to `Object[]` — see the commit history if
> you're curious, it's a genuinely common Java trap worth knowing.

**Complexity:** put/get/remove are O(1) *average case* (assuming
few collisions), but degrade to O(n) worst case if everything hashes
into the same bucket. See `evidence/benchmarks/hash_load_factor.csv`
for how collision count actually grows with load factor.

---

## 8. Binary Search Tree (BST)

**What it is:** a tree where every node's left subtree holds only
smaller values and its right subtree holds only larger values — so
searching means repeatedly picking left or right, halving your search
space each time... *if* the tree is balanced.

**Why we needed it:** it's the simplest ordered/searchable tree, and
it's also the deliberate **counterexample** in our BST-vs-balanced-tree
performance experiment — see the warning below.

**How it works:**
```
insert(value):
    if tree is empty: value becomes the root
    else: compare value to current node
        go left if smaller, right if larger (recurse)
        insert as a new leaf when you hit an empty spot

search(value): same left/right comparison walk, until found or hit empty
```

**Our implementation** (`structures/BSTMap.java`): a plain,
*unbalanced* key-value BST — no rebalancing logic at all. That's
deliberate: it's the "before" picture in our BST-vs-red-black-tree
comparison.

**⚠️ The catch — worst case is real and we measured it**: if you
insert already-sorted data (1, 2, 3, 4, ...), every new node becomes
the rightmost leaf and the tree degenerates into what's basically a
linked list. We proved this isn't just theory —
`evidence/benchmarks/bst_vs_balanced.csv` shows a BST height of **9,999**
at n=10,000 (should be ~14 if balanced). See
[Red-Black Tree](#9-red-black-tree) for the fix.

**Complexity:** O(log n) average case, **O(n) worst case** (sorted
input) — this gap is the entire point of the next section.

---

## 9. Red-Black Tree

**What it is:** a *self-balancing* BST. Every node is colored red or
black, and the tree enforces rules (no red node has a red child, every
path from root to an empty leaf passes through the same number of
black nodes) that mathematically guarantee the tree never gets more
than about `2 × log₂(n)` tall — no matter what order you insert in.

**Why we needed it:** it's the fix for the BST's worst case above —
same interface, but insert order can no longer break it.

**How it works (simplified):** insert like a normal BST, color the new
node red, then **fix violations on the way back up**:
```
after inserting (as red):
    while parent is also red (a violation):
        if uncle is red: recolor parent, uncle, grandparent, move up
        else: rotate (left or right) to restore balance, recolor
    make the root black (always)
```
A **rotation** re-parents a small cluster of nodes to reduce height
without breaking BST ordering — see Figure 5.2 in the diagrams
artifact for the exact before/after picture.

**Our implementation** (`structures/RedBlackTreeMap.java`): a real,
from-scratch implementation with actual `rotateLeft`/`rotateRight` and
recoloring logic — not a "simplified" stand-in. We wrote a structural
invariant checker (`isValidRedBlackTree()` in the original design,
verified across ascending/descending/shuffled insert orders in tests)
specifically to *prove* the balancing logic is correct, not just
assume it.

**Proof it works**: same `evidence/benchmarks/bst_vs_balanced.csv` —
at n=10,000, height stays **under 14**, vs. the BST's 9,999. Same
data, wildly different outcome, purely because of the rotation logic.

**Complexity:** O(log n) guaranteed, insert and search both — no
worst case that depends on input order.

---

## 10. B-Tree

**What it is:** a tree where each node holds *multiple* keys (not just
one) and has *multiple* children (not just two) — designed around how
databases actually read data: one node = one disk page, and you want
to minimize the number of pages you touch.

**Why we needed it:** the brief specifically asks for this to model
"database index pages" — one B-tree node is a stand-in for one page a
real database would read off disk.

**How it works (minimum degree t):** each node holds up to `2t-1` keys.
```
insert(key):
    if root is full: split it first, growing the tree's height by one
    walk down from the root, choosing which child to descend into
    if that child is full: split it BEFORE descending into it
    once you reach a non-full leaf, insert the key in sorted position
```
The "split before descending" part is the key trick — it guarantees a
single top-to-bottom pass is always enough; no separate fix-up walk
back up is needed.

**Our implementation** (`structures/BTreeMap.java`): minimum degree is
configurable via the constructor. One implementation detail worth
knowing for defense: a node's `children` array is declared `Object[]`
rather than `BNode[]`, because `BNode` is a *non-static inner class* of
the generic `BTreeMap<K,V>` — trying to create `new BNode[n]` directly
triggers a "generic array creation" compile error in Java. We added
`child(i)`/`setChild(i, ...)` helper methods that do the cast at the
boundary instead.

**Complexity:** O(log n) for search/insert, but with a much smaller
constant than a binary tree — each node comparison can rule out up to
`2t-1` keys at once instead of just 1.

---

## 11. Disjoint Set (Union-Find)

**What it is:** tracks a collection of items partitioned into
non-overlapping groups, and answers two questions fast: "are these two
items in the same group?" and "merge these two groups into one."

**Why we needed it:** it's exactly what Kruskal's MST algorithm needs
to detect cycles — see [§25](#25-prims--kruskals-algorithms-mst).

**How it works:** each item starts as its own group (pointing to
itself). Groups are tracked as trees where every node points to its
parent; the "representative" of a group is whatever's at the root.
```
find(item):
    follow parent pointers until you reach an item that is its own parent (the root)
    "path compression": point every visited item directly at that root,
        so future find() calls on them are instant

union(a, b):
    rootA = find(a), rootB = find(b)
    if already the same root: nothing to do (would create a cycle!)
    else: attach the smaller/shallower tree under the larger one ("union by rank")
```

**Our implementation** (`structures/DisjointSet.java`): backed by two
`HashTable`s (parent pointers and ranks) rather than arrays — since
our node type is generic (`T`, e.g. a campus location), not
necessarily small sequential integers.

**Complexity:** both `find` and `union` are essentially O(1) — more
precisely O(α(n)), where α is the *inverse Ackermann function*, which
grows so slowly it's under 5 for any input size that could ever exist
in practice. That's why this beats checking connectivity with a full
graph traversal every time.

---

## 12. Set / Map

**What it is:** a **Set** stores unique items with no particular
order — just "is this in the collection or not?" A **Map** stores
key→value pairs (this is what `HashTable` already *is*).

**Why we needed it:** the brief asks for a "custom set/map built on
top of hash table or BST" as its own deliverable, separate from the
raw hash table.

**How it works:** a Set is really just a Map where you only care about
the keys — `add(x)` is `map.put(x, SOME_PLACEHOLDER)`, `contains(x)`
is `map.containsKey(x)`.

**Our implementation**: `structures/HashSetWrapper.java` /
`TreeSetWrapper.java` wrap `HashTable`/`RedBlackTreeMap` respectively
this exact way — a `PRESENT` placeholder object as the value, real
logic delegated straight through. `HashMapWrapper.java` /
`TreeMapWrapper.java` do the equivalent for full key-value maps. This
is intentional composition, not duplicated logic — `HashTable` and
`RedBlackTreeMap` already do the real work.

**Complexity:** whatever the underlying structure gives you — O(1)
average for the hash-backed versions, O(log n) guaranteed for the
tree-backed versions.

---

## 13. Graph

**What it is:** a set of nodes ("vertices") connected by edges, which
can be weighted (a number attached, like distance) and directed (only
usable one way) or undirected (usable both ways).

**Why we needed it:** it's the entire model of the campus road
network — locations are nodes, roads are edges, and every routing
question (shortest path, reachability, minimum spanning tree) is a
graph algorithm running on top of it.

**How it's represented — two ways, both required by the brief:**
- **Adjacency list**: each node keeps a list (in our case, a
  `HashTable`) of which nodes it connects to and at what weight. Space
  efficient for sparse graphs (few edges relative to nodes).
- **Adjacency matrix**: an `n × n` grid where cell `[i][j]` holds the
  weight of the edge from node i to node j (or "no edge"). Simple, but
  wastes space if most pairs of nodes *aren't* connected.

**Our implementation:**
- `structures/Graph.java` — adjacency list, using
  `HashTable<T, HashTable<T, Double>>` (node → its neighbours →
  their weights). Includes Dijkstra directly on this representation.
- `structures/GraphMatrix.java` — adjacency matrix, using a plain
  `double[][]` plus a `HashTable<T, Integer>` mapping each node to its
  row/column index. Also includes its own Dijkstra.

**A real bug we found and fixed here, worth knowing about:**
`RouteService` builds the graph from `roads.csv`, where each road is
stored *once* (e.g. "1 → 2, distance 100"). Roads are physically
walkable in both directions, but the code originally only added the
edge one way — silently making the graph *directed* instead of
*undirected*. This didn't break Dijkstra or Kruskal (Kruskal happens
to still work because it scans every node's outgoing edges into one
flat list regardless of direction), but it broke **Prim**, which can
only expand via outgoing edges from nodes already in the tree — it
reported a real, wrong, higher-cost MST (15,300 instead of the correct
9,250) because it couldn't "walk backward" along one-way edges. Fixed
by adding every road edge in both directions. **Lesson**: always add
edges symmetrically when your graph represents something that's
physically bidirectional — and verify algorithms that should agree
(like two different MST algorithms) actually *do* agree, don't just
trust that a program that runs without crashing is correct.

**Complexity:** adjacency list uses O(V + E) space (V = vertices, E =
edges) and is fast to iterate a node's neighbours; adjacency matrix
uses O(V²) space regardless of how many edges actually exist, but
answers "is there an edge between i and j?" in O(1).

---

# Part 2 — Algorithms

## 14. Linear Search

**What it is:** check every element one at a time until you find the
target (or run out of elements).

**Why we needed it:** the baseline every other search algorithm gets
compared against — no precondition on the input at all, unlike binary
search.

**Pseudocode:**
```
for i from 0 to n-1:
    if array[i] == target: return i
return -1 (not found)
```

**Our implementation** (`algorithms/LinearSearch.java`): generic over
any type via a `Comparator`/equality check, plus a `searchByKey`
overload so you can search an array of `ServiceRequest` objects by
just their `requestId` field without needing full object equality.

**Complexity:** O(n) always — best case O(1) if you get lucky and the
target is first, worst case O(n) if it's last or absent.

---

## 15. Binary Search

**What it is:** repeatedly cut the search range in half by checking
the middle element — but this **only works if the array is sorted**.

**Why we needed it:** the fast alternative to linear search, once you
can guarantee sorted input — see
`evidence/benchmarks/search_comparison.csv` for exactly how much
faster.

**Pseudocode:**
```
low = 0, high = n-1
while low <= high:
    mid = low + (high - low) / 2
    if array[mid] == target: return mid
    else if array[mid] < target: low = mid + 1
    else: high = mid - 1
return -1
```

**Our implementation** (`algorithms/BinarySearch.java`): note the
`low + (high - low) / 2` instead of the more obvious
`(low + high) / 2` — the second form can silently overflow on very
large arrays; the first form can't.

**⚠️ The precondition is not optional**: run this on unsorted data and
it will *silently give you a wrong answer* instead of crashing — see
`BinarySearchTest.unsortedInputViolatesPreconditionAndGivesWrongAnswer`
and the full trace in `evidence/trace-tables/binary-search-trace.md`
for a worked example of exactly how it fails.

**Complexity:** O(log n) always — every comparison halves the
remaining search space.

---

## 16. Selection Sort

**What it is:** repeatedly find the smallest remaining element and
swap it into its correct position.

**Pseudocode:**
```
for i from 0 to n-2:
    find the index of the minimum value in array[i..n-1]
    swap array[i] with that minimum
```

**Our implementation** (`algorithms/SelectionSort.java`): generic +
`Comparator`, plus a primitive `int[]` overload for benchmarking
without boxing overhead.

**Complexity:** O(n²) **always**, no matter the input order — it
always scans the entire remaining unsorted region every single pass.
Not stable (a swap can jump an element past another equal-keyed one).
At most n-1 swaps, though — cheap if writes are expensive.

---

## 17. Insertion Sort

**What it is:** build up a sorted region one element at a time, by
taking the next element and sliding it backward into its correct
place among what's already sorted — like sorting a hand of playing
cards.

**Pseudocode:**
```
for i from 1 to n-1:
    key = array[i]
    j = i - 1
    while j >= 0 and array[j] > key:
        array[j+1] = array[j]   # shift right
        j = j - 1
    array[j+1] = key
```

**Our implementation** (`algorithms/InsertionSort.java`): same
generic + primitive-overload pattern as the others.

**Complexity:** O(n²) worst case (reverse-sorted input), but **O(n)
best case** if the input is already sorted (the inner while loop exits
immediately every time). This data-dependence is the key difference
from selection sort — see
`evidence/trace-tables/insertion-sort-trace.md` for a side-by-side
comparison count against selection sort on the same input. Stable.

---

## 18. Merge Sort

**What it is:** split the array in half, recursively sort each half,
then merge the two sorted halves back together.

**Pseudocode:**
```
mergeSort(array, left, right):
    if left < right:
        mid = left + (right - left) / 2
        mergeSort(array, left, mid)
        mergeSort(array, mid+1, right)
        merge(array, left, mid, right)

merge(array, left, mid, right):
    copy the two halves into temporary arrays L and R
    repeatedly take whichever front element (L's or R's) is smaller
    append any leftovers from whichever side still has elements
```

**Our implementation** (`algorithms/MergeSort.java`): generic +
primitive overload. See
`evidence/proof-sketches.md` for the full induction proof of why this
is correct, and `evidence/trace-tables/merge-sort-trace.md` for a
step-by-step split/merge trace.

**Complexity:** O(n log n) **always** — this never changes no matter
how the input is ordered, unlike quicksort below. Costs O(n) extra
space for the temporary arrays during merging. Not in-place.

---

## 19. Quicksort

**What it is:** pick a "pivot" element, rearrange the array so
everything smaller than the pivot ends up on its left and everything
bigger ends up on its right (this step is called **partitioning**),
then recursively sort each side.

**Pseudocode (Lomuto partition, last element as pivot):**
```
quickSort(array, low, high):
    if low < high:
        pivotIndex = partition(array, low, high)
        quickSort(array, low, pivotIndex - 1)
        quickSort(array, pivotIndex + 1, high)

partition(array, low, high):
    pivot = array[high]
    i = low - 1
    for j from low to high-1:
        if array[j] <= pivot:
            i += 1
            swap array[i], array[j]
    swap array[i+1], array[high]
    return i + 1
```

**Our implementation** (`algorithms/QuickSort.java`): the pattern
above exactly, plus a primitive overload.

**Complexity:** O(n log n) *average* case, but **O(n²) worst case** —
specifically, already-sorted (or reverse-sorted) input with
last-element pivoting causes the partition to split as unevenly as
possible every single time. This is the tradeoff against merge sort:
faster in practice (in-place, smaller constant factor), but no
worst-case guarantee.

---

## 20. Greedy Algorithm

**What it is:** at every step, make whatever choice looks best *right
now*, and never go back and reconsider it — no matter what happens
later.

**Why it's risky:** greedy is fast, but only gives the *true* best
overall answer for problems with special structure (like MST — see
Kruskal below). For general problems, an early "obviously good" choice
can lock you into a bad overall outcome.

**Our implementation, and why we deliberately built a failure case**
(`algorithms/GreedyAssignment.java`): assigns each service request, in
order, to whichever *currently available* resource is cheapest for
it — then marks that resource unavailable.

```
for each request i (in order):
    assign it to whichever available resource has the lowest cost[i][resource]
    mark that resource unavailable
```

**The counterexample** (`GreedyAssignmentTest.greedyFailsToFindTheOptimalAssignment`):
Request 1 costs 1 to Resource X, 2 to Resource Y. Request 2 costs 2 to
X, 100 to Y. Greedy grabs X for Request 1 (cheapest for it, cost 1),
forcing Request 2 onto Y at cost 100 — **total 101**. The true optimum
is Request 1→Y (2), Request 2→X (2) — **total 4**. Greedy could never
find this because it never reconsiders Request 1's choice once made.

**Contrast with Kruskal** (a greedy algorithm that *does* work,
[§25](#25-prims--kruskals-algorithms-mst)): the difference is that MST
has the *cut property* backing every greedy choice — see
`evidence/proof-sketches.md` for exactly why that guarantee exists for
MST but not for assignment problems.

**Complexity:** O(requests × resources) — one scan of available
resources per request.

---

## 21. Dynamic Programming (0/1 Knapsack)

**What it is:** solve a problem by breaking it into overlapping
subproblems, solving each subproblem *once*, and storing the answer in
a table so you never recompute it — this is what guarantees the true
optimum where greedy can't.

**Why we needed it:** models selecting the best subset of requests
under a budget/capacity constraint — the DP counterpart to greedy's
failure above.

**Pseudocode (bottom-up tabulation):**
```
table[0][w] = 0 for every capacity w   # no items = no value, always

for i from 1 to n:
    for w from 0 to capacity:
        table[i][w] = table[i-1][w]                    # option: skip item i
        if weight[i-1] <= w:
            table[i][w] = max(table[i][w],
                               table[i-1][w - weight[i-1]] + value[i-1])  # option: take it

answer = table[n][capacity]
```
Then **reconstruct** which items were actually chosen by walking the
table backward: at `table[i][w]`, if it differs from `table[i-1][w]`,
item `i-1` was included — subtract its weight from `w` and move to row
`i-1`; otherwise it wasn't, just move to row `i-1` with `w` unchanged.

**Our implementation** (`algorithms/Knapsack.java`): exactly the
pattern above, verified for real — we didn't just trust the hand-derived
example, we ran the actual code and diffed its real table output
against the hand-traced version (see
`evidence/trace-tables/knapsack-dp-trace.md`).

**Complexity:** O(n × capacity) time *and* space to build the table —
guaranteed optimal, but only practical while `capacity` stays
reasonably bounded (a capacity of 10 million would need a
10-million-column table).

---

## 22. Breadth-First Search (BFS)

**What it is:** explore a graph **level by level** — visit everything
1 step away first, then everything 2 steps away, then 3, and so on.
Uses a **queue**.

**Why we needed it:** answers "which locations are reachable from this
dispatch point?" — and because it explores level-by-level, it also
happens to find the *shortest path in terms of number of edges* (not
weight) between two nodes.

**Pseudocode:**
```
visited = {source}
queue.enqueue(source)
while queue not empty:
    current = queue.dequeue()
    record current as visited (in order)
    for each neighbor of current:
        if not visited:
            mark visited
            queue.enqueue(neighbor)
```

**Our implementation** (`algorithms/BFS.java`): uses our own `Queue`
and `HashSetWrapper` (not `java.util.ArrayDeque`/`HashSet`). Tested
explicitly against a **disconnected graph** (confirms it only visits
the reachable component, not the whole graph) and an **unreachable
target** (confirms `isReachable` correctly returns false).

**Complexity:** O(V + E) — every node is visited once, every edge is
examined once (when expanding its source node).

---

## 23. Depth-First Search (DFS)

**What it is:** explore as far as possible down one path before
backtracking — go deep first, not wide. Uses a **stack** (or
recursion, which is really the same thing using the call stack).

**Why we needed it:** an alternative traversal order to BFS — same
reachability answer, different order, useful for different kinds of
graph analysis (e.g. detecting cycles, topological ordering in other
contexts).

**Pseudocode (iterative, using an explicit stack):**
```
stack.push(source)
while stack not empty:
    current = stack.pop()
    if current already visited: skip (reached via a different path already)
    mark current visited, record it
    for each neighbor of current:
        if not visited: stack.push(neighbor)
```

**Our implementation** (`algorithms/DFS.java`): deliberately
**iterative**, using our own `Stack`, rather than recursive — avoids
risking a real stack overflow on a large graph, and gives us a chance
to actually exercise the custom `Stack` structure. Tested against
disconnected graphs, unreachable targets, and graphs *with cycles*
(confirms it terminates correctly instead of looping forever).

**Complexity:** O(V + E), same as BFS.

---

## 24. Dijkstra's Algorithm

**What it is:** finds the shortest **weighted** path from a source
node to every other node — unlike BFS, which only counts *edges*,
Dijkstra adds up actual edge *weights* (e.g. real road distances).
Requires all weights to be non-negative.

**Why we needed it:** answers "what's the fastest route between two
campus locations?" — the actual routing engine of the whole project.

**Pseudocode:**
```
dist[source] = 0, dist[everything else] = infinity
priorityQueue.offer(source, 0)

while priorityQueue not empty:
    (u, d) = priorityQueue.poll()   # smallest tentative distance
    if d > dist[u]: continue         # stale entry, skip it
    for each neighbor v of u, edge weight w:
        if dist[u] + w < dist[v]:
            dist[v] = dist[u] + w
            predecessor[v] = u
            priorityQueue.offer(v, dist[v])

return dist, predecessor
```
To get the actual path (not just the distance), walk `predecessor`
backward from the target to the source, then reverse the list.

**Our implementation** (`structures/Graph.java`, methods `dijkstra`/
`shortestPath`): uses our own `PriorityQueueHeap`, not
`java.util.PriorityQueue`. The "stale entry" check matters — since our
heap doesn't support efficiently decreasing a key already inside it,
we just push a *new* entry every time we find a better distance and
skip old, now-outdated entries when they're popped later. See
`evidence/trace-tables/dijkstra-trace.md` for a full numeric trace
including exactly this stale-entry skip happening.

**Complexity:** O(E log V) using a binary heap — each edge can trigger
one heap insertion, and heap operations are O(log V).

---

## 25. Prim's & Kruskal's Algorithms (MST)

**What they are:** both find a **Minimum Spanning Tree** — the
cheapest possible set of edges that connects every node in a weighted
graph with no cycles. They take completely different approaches but
must always agree on the *total* weight (though not necessarily on
which exact edges, if there are ties).

- **Prim's** grows one connected tree outward from a starting node,
  always adding the cheapest edge that connects the tree to a new
  node.
- **Kruskal's** ignores connectivity entirely at first — it sorts
  *all* edges by weight, then greedily adds each one **unless it would
  create a cycle** (checked via Disjoint Set).

**Prim's pseudocode:**
```
inTree = {start}
frontier = all edges leaving `start`, in a min-heap by weight

while frontier not empty and inTree doesn't cover every node:
    (u, v, w) = frontier.poll()   # cheapest crossing edge
    if v already in inTree: continue   # stale, skip
    add v to inTree, keep edge (u, v, w)
    add all edges leaving v to frontier
```

**Kruskal's pseudocode:**
```
edges = all edges, sorted ascending by weight
disjointSet.makeSet(v) for every node v
mst = []

for each edge (u, v, w) in sorted order:
    if disjointSet.find(u) != disjointSet.find(v):   # doesn't close a cycle
        disjointSet.union(u, v)
        mst.append((u, v, w))

return mst
```

**Our implementation:**
- `algorithms/Prim.java` — uses our `PriorityQueueHeap` as the
  frontier, same stale-entry-skip pattern as Dijkstra.
- `algorithms/Kruskal.java` — **reuses `MergeSort`** to sort the edges
  and **reuses `DisjointSet`** for cycle detection, rather than
  reimplementing either. This is genuine code reuse, not just two
  independent implementations sitting side by side.

**Why greedy actually works here** (unlike `GreedyAssignment` above):
MST has the *cut property* — for any way you split the graph's nodes
into two groups, the cheapest edge crossing that split is guaranteed
to belong to *some* minimum spanning tree. Every edge Kruskal accepts
is, at that moment, exactly the cheapest edge crossing the cut between
"already connected" and "not yet connected" — so accepting it is
always provably safe. Full exchange-argument proof in
`evidence/proof-sketches.md`. See
`evidence/trace-tables/kruskal-trace.md` for the connectivity trace
matching this exact logic.

**A real bug this caught**: Prim and Kruskal disagreeing on total cost
(15,300 vs 9,250) on our actual campus road network was the signal
that exposed the directed-vs-undirected graph bug described in
[§13 Graph](#13-graph). If two independent, correct algorithms
computing the same thing ever disagree, don't shrug it off — one of
your inputs is wrong. After the fix, both report exactly **9,250**.

**Complexity:** Kruskal is O(E log E) (dominated by the sort). Prim is
O(E log V) using a binary heap — same shape as Dijkstra, for the same
reason (a heap-driven frontier).

---

## How to use this for oral defense

1. Find your name in `DOCUMENTATION.md` §11 ("Individual Contribution
   Statement").
2. Read the matching section(s) above for whatever you're credited
   with.
3. Open the real file it points to and read the actual code alongside
   the pseudocode here — the pseudocode is deliberately simplified,
   the code has the real edge-case handling (null checks, empty
   inputs, etc.) that an examiner might ask you to explain.
4. Check `evidence/trace-tables/` for a worked numeric example of your
   algorithm, if one exists — being able to walk through a concrete
   trace live is exactly the kind of thing the brief says an examiner
   may ask for.
