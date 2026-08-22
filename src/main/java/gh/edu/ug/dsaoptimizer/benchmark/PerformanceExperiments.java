package gh.edu.ug.dsaoptimizer.benchmark;

import gh.edu.ug.dsaoptimizer.algorithms.BFS;
import gh.edu.ug.dsaoptimizer.algorithms.BinarySearch;
import gh.edu.ug.dsaoptimizer.algorithms.DFS;
import gh.edu.ug.dsaoptimizer.algorithms.InsertionSort;
import gh.edu.ug.dsaoptimizer.algorithms.Kruskal;
import gh.edu.ug.dsaoptimizer.algorithms.LinearSearch;
import gh.edu.ug.dsaoptimizer.algorithms.MergeSort;
import gh.edu.ug.dsaoptimizer.algorithms.QuickSort;
import gh.edu.ug.dsaoptimizer.algorithms.SelectionSort;
import gh.edu.ug.dsaoptimizer.structures.BSTMap;
import gh.edu.ug.dsaoptimizer.structures.Graph;
import gh.edu.ug.dsaoptimizer.structures.HashTable;
import gh.edu.ug.dsaoptimizer.structures.PriorityQueueHeap;
import gh.edu.ug.dsaoptimizer.structures.RedBlackTreeMap;
import gh.edu.ug.dsaoptimizer.util.LineChart;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Random;

/**
 * Implements all 6 required performance experiments (brief section 9):
 * search comparison, sorting comparison, hash table load factor, BST
 * vs balanced tree, heap priority dispatch, and graph algorithms.
 *
 * <p>Every experiment runs each measurement 3 times and averages, per
 * the brief's rule, and writes both a raw CSV (evidence/benchmarks/)
 * and a line graph (evidence/graphs/) using {@link LineChart} -- no
 * external plotting dependency needed.
 */
public final class PerformanceExperiments {

    private static final int RUNS_PER_SIZE = 3;
    private static final Random RANDOM = new Random(42); // fixed seed: reproducible experiment runs

    private PerformanceExperiments() {
        // utility class -- no instances
    }

    // ---- 1. Search comparison ----

    public static void runSearchComparison(int[] sizes, Path csvPath, Path pngPath) throws IOException {
        double[] linearAverages = new double[sizes.length];
        double[] binaryAverages = new double[sizes.length];

        try (BufferedWriter writer = Files.newBufferedWriter(csvPath)) {
            writer.write("algorithm_name,input_size,run_number,time_ns,memory_kb,date_run");
            writer.newLine();

            for (int s = 0; s < sizes.length; s++) {
                int n = sizes[s];
                int[] sorted = randomArray(n);
                java.util.Arrays.sort(sorted); // fixture setup only, not assessed search/sort logic
                int target = sorted[n / 2];
                double linearTotal = 0;
                double binaryTotal = 0;

                for (int run = 1; run <= RUNS_PER_SIZE; run++) {
                    long linearTime = timeSearch(() -> LinearSearch.search(sorted, target));
                    long binaryTime = timeSearch(() -> BinarySearch.search(sorted, target));

                    writeRow(writer, "linear_search", n, run, linearTime);
                    writeRow(writer, "binary_search", n, run, binaryTime);
                    linearTotal += linearTime;
                    binaryTotal += binaryTime;
                }
                linearAverages[s] = linearTotal / RUNS_PER_SIZE;
                binaryAverages[s] = binaryTotal / RUNS_PER_SIZE;
            }
        }

        LineChart chart = new LineChart("Linear vs Binary Search", "input size (n)", "average time (ns)");
        double[] xs = toDoubleArray(sizes);
        chart.addSeries("linear_search", xs, linearAverages);
        chart.addSeries("binary_search", xs, binaryAverages);
        chart.saveTo(pngPath);
    }

    // ---- 2. Sorting comparison ----

    public static void runSortingComparison(int[] sizes, Path csvPath, Path pngPath) throws IOException {
        double[][] averagesByAlgorithm = new double[4][sizes.length];
        String[] names = {"selection_sort", "insertion_sort", "merge_sort", "quick_sort"};

        try (BufferedWriter writer = Files.newBufferedWriter(csvPath)) {
            writer.write("algorithm_name,input_size,run_number,time_ns,memory_kb,date_run");
            writer.newLine();

            for (int s = 0; s < sizes.length; s++) {
                int n = sizes[s];
                int[] base = randomArray(n);
                double[] totals = new double[4];

                for (int run = 1; run <= RUNS_PER_SIZE; run++) {
                    long selection = timeIntSort(base.clone(), SelectionSort::sort);
                    long insertion = timeIntSort(base.clone(), InsertionSort::sort);
                    long merge = timeIntSort(base.clone(), MergeSort::sort);
                    long quick = timeIntSort(base.clone(), QuickSort::sort);

                    writeRow(writer, names[0], n, run, selection);
                    writeRow(writer, names[1], n, run, insertion);
                    writeRow(writer, names[2], n, run, merge);
                    writeRow(writer, names[3], n, run, quick);

                    totals[0] += selection;
                    totals[1] += insertion;
                    totals[2] += merge;
                    totals[3] += quick;
                }

                for (int a = 0; a < 4; a++) {
                    averagesByAlgorithm[a][s] = totals[a] / RUNS_PER_SIZE;
                }
            }
        }

        LineChart chart = new LineChart("Sorting Comparison", "input size (n)", "average time (ns)");
        double[] xs = toDoubleArray(sizes);
        for (int a = 0; a < 4; a++) {
            chart.addSeries(names[a], xs, averagesByAlgorithm[a]);
        }
        chart.saveTo(pngPath);
    }

    @FunctionalInterface
    private interface IntSortFn {
        void sort(int[] array);
    }

    private static long timeIntSort(int[] array, IntSortFn sortFn) {
        long start = System.nanoTime();
        sortFn.sort(array);
        return System.nanoTime() - start;
    }

    // ---- 3. Hash table load factor ----

    public static void runHashLoadFactor(int[] sizes, Path csvPath, Path pngPath) throws IOException {
        double[] loadFactors = {0.5, 0.75, 1.0, 1.5, 2.0};
        double[][] collisionsByLoadFactor = new double[loadFactors.length][sizes.length];

        try (BufferedWriter writer = Files.newBufferedWriter(csvPath)) {
            writer.write("algorithm_name,input_size,run_number,time_ns,memory_kb,date_run,load_factor,collision_count");
            writer.newLine();

            for (int s = 0; s < sizes.length; s++) {
                int n = sizes[s];
                for (int lf = 0; lf < loadFactors.length; lf++) {
                    double targetLoadFactor = loadFactors[lf];
                    int capacity = Math.max(1, (int) (n / targetLoadFactor));

                    int collisions = 0;
                    for (int run = 1; run <= RUNS_PER_SIZE; run++) {
                        HashTable<Integer, Integer> table = new HashTable<>(capacity);
                        table.setAutoResize(false); // hold the table at this exact size to observe the target load factor
                        long start = System.nanoTime();
                        for (int i = 0; i < n; i++) {
                            table.put(i, i);
                        }
                        long time = System.nanoTime() - start;
                        collisions = table.collisionCount(); // same every run (deterministic insert order)

                        writer.write(String.format("hash_table,%d,%d,%d,,%s,%.2f,%d",
                                n, run, time, Instant.now(), targetLoadFactor, collisions));
                        writer.newLine();
                    }
                    collisionsByLoadFactor[lf][s] = collisions;
                }
            }
        }

        LineChart chart = new LineChart("Hash Table Load Factor vs Collisions", "input size (n)", "collision count");
        double[] xs = toDoubleArray(sizes);
        for (int lf = 0; lf < loadFactors.length; lf++) {
            chart.addSeries("load factor " + loadFactors[lf], xs, collisionsByLoadFactor[lf]);
        }
        chart.saveTo(pngPath);
    }

    // ---- 4. BST vs balanced tree ----

    public static void runBstVsBalancedTree(int[] sizes, Path csvPath, Path pngPath) throws IOException {
        double[] bstHeights = new double[sizes.length];
        double[] rbtHeights = new double[sizes.length];

        try (BufferedWriter writer = Files.newBufferedWriter(csvPath)) {
            writer.write("algorithm_name,input_size,run_number,time_ns,memory_kb,date_run,height");
            writer.newLine();

            for (int s = 0; s < sizes.length; s++) {
                int n = sizes[s];

                // Ascending insert order -- the worst case for a plain BST,
                // no better/worse than any other order for a red-black tree.
                BSTMap<Integer, Integer> bst = new BSTMap<>();
                RedBlackTreeMap<Integer, Integer> rbt = new RedBlackTreeMap<>();
                for (int i = 0; i < n; i++) {
                    bst.put(i, i);
                    rbt.put(i, i);
                }
                bstHeights[s] = bst.height();
                rbtHeights[s] = rbt.height();

                for (int run = 1; run <= RUNS_PER_SIZE; run++) {
                    int target = n / 2;
                    long bstTime = timeSearch(() -> bst.get(target));
                    long rbtTime = timeSearch(() -> rbt.get(target));

                    writer.write(String.format("bst,%d,%d,%d,,%s,%d", n, run, bstTime, Instant.now(), bst.height()));
                    writer.newLine();
                    writer.write(String.format("red_black_tree,%d,%d,%d,,%s,%d", n, run, rbtTime, Instant.now(), rbt.height()));
                    writer.newLine();
                }
            }
        }

        LineChart chart = new LineChart("BST vs Red-Black Tree Height", "input size (n)", "tree height");
        double[] xs = toDoubleArray(sizes);
        chart.addSeries("BST (unbalanced)", xs, bstHeights);
        chart.addSeries("Red-Black Tree (balanced)", xs, rbtHeights);
        chart.saveTo(pngPath);
    }

    @FunctionalInterface
    private interface SearchFn {
        Object search();
    }

    private static long timeSearch(SearchFn fn) {
        long start = System.nanoTime();
        fn.search();
        return System.nanoTime() - start;
    }

    // ---- 5. Heap priority dispatch ----

    public static void runHeapDispatch(int[] sizes, Path csvPath, Path pngPath) throws IOException {
        double[] insertAverages = new double[sizes.length];
        double[] extractAverages = new double[sizes.length];

        try (BufferedWriter writer = Files.newBufferedWriter(csvPath)) {
            writer.write("algorithm_name,input_size,run_number,time_ns,memory_kb,date_run");
            writer.newLine();

            for (int s = 0; s < sizes.length; s++) {
                int n = sizes[s];
                int[] values = randomArray(n);
                double insertTotal = 0;
                double extractTotal = 0;

                for (int run = 1; run <= RUNS_PER_SIZE; run++) {
                    PriorityQueueHeap<Integer> heap = new PriorityQueueHeap<>();
                    long insertStart = System.nanoTime();
                    for (int v : values) {
                        heap.offer(v);
                    }
                    long insertTime = System.nanoTime() - insertStart;

                    long extractStart = System.nanoTime();
                    while (!heap.isEmpty()) {
                        heap.poll();
                    }
                    long extractTime = System.nanoTime() - extractStart;

                    writeRow(writer, "heap_insert", n, run, insertTime);
                    writeRow(writer, "heap_extract", n, run, extractTime);
                    insertTotal += insertTime;
                    extractTotal += extractTime;
                }
                insertAverages[s] = insertTotal / RUNS_PER_SIZE;
                extractAverages[s] = extractTotal / RUNS_PER_SIZE;
            }
        }

        LineChart chart = new LineChart("Heap Priority Dispatch", "input size (n)", "average time (ns)");
        double[] xs = toDoubleArray(sizes);
        chart.addSeries("insert (offer)", xs, insertAverages);
        chart.addSeries("extract (poll)", xs, extractAverages);
        chart.saveTo(pngPath);
    }

    // ---- 6. Graph algorithms ----

    public static void runGraphAlgorithms(int[] nodeCounts, Path csvPath, Path pngPath) throws IOException {
        double[] bfsAverages = new double[nodeCounts.length];
        double[] dfsAverages = new double[nodeCounts.length];
        double[] dijkstraAverages = new double[nodeCounts.length];
        double[] mstAverages = new double[nodeCounts.length];

        try (BufferedWriter writer = Files.newBufferedWriter(csvPath)) {
            writer.write("algorithm_name,input_size,run_number,time_ns,memory_kb,date_run");
            writer.newLine();

            for (int s = 0; s < nodeCounts.length; s++) {
                int n = nodeCounts[s];
                double bfsTotal = 0, dfsTotal = 0, dijkstraTotal = 0, mstTotal = 0;

                for (int run = 1; run <= RUNS_PER_SIZE; run++) {
                    Graph<Integer> graph = randomConnectedGraph(n);

                    long bfsTime = timeSearch(() -> BFS.traverse(graph, 0));
                    long dfsTime = timeSearch(() -> DFS.traverse(graph, 0));
                    long dijkstraTime = timeSearch(() -> graph.dijkstra(0));
                    long mstTime = timeSearch(() -> Kruskal.mst(graph));

                    writeRow(writer, "bfs", n, run, bfsTime);
                    writeRow(writer, "dfs", n, run, dfsTime);
                    writeRow(writer, "dijkstra", n, run, dijkstraTime);
                    writeRow(writer, "kruskal_mst", n, run, mstTime);

                    bfsTotal += bfsTime;
                    dfsTotal += dfsTime;
                    dijkstraTotal += dijkstraTime;
                    mstTotal += mstTime;
                }
                bfsAverages[s] = bfsTotal / RUNS_PER_SIZE;
                dfsAverages[s] = dfsTotal / RUNS_PER_SIZE;
                dijkstraAverages[s] = dijkstraTotal / RUNS_PER_SIZE;
                mstAverages[s] = mstTotal / RUNS_PER_SIZE;
            }
        }

        LineChart chart = new LineChart("Graph Algorithms", "node count", "average time (ns)");
        double[] xs = toDoubleArray(nodeCounts);
        chart.addSeries("BFS", xs, bfsAverages);
        chart.addSeries("DFS", xs, dfsAverages);
        chart.addSeries("Dijkstra", xs, dijkstraAverages);
        chart.addSeries("Kruskal MST", xs, mstAverages);
        chart.saveTo(pngPath);
    }

    /** A random connected graph on n nodes: a random spanning path (guarantees connectivity) plus extra random edges. */
    private static Graph<Integer> randomConnectedGraph(int n) {
        Graph<Integer> graph = new Graph<>();
        for (int i = 0; i < n; i++) {
            graph.addNode(i);
        }
        // spanning path guarantees the whole graph is connected
        for (int i = 0; i < n - 1; i++) {
            double weight = 1 + RANDOM.nextInt(100);
            graph.addEdge(i, i + 1, weight);
            graph.addEdge(i + 1, i, weight);
        }
        // extra random edges, roughly doubling the edge count
        for (int i = 0; i < n; i++) {
            int j = RANDOM.nextInt(n);
            if (i != j) {
                double weight = 1 + RANDOM.nextInt(100);
                graph.addEdge(i, j, weight);
                graph.addEdge(j, i, weight);
            }
        }
        return graph;
    }

    // ---- shared helpers ----

    private static void writeRow(BufferedWriter writer, String algorithmName, int n, int run, long timeNs)
            throws IOException {
        writer.write(String.format("%s,%d,%d,%d,,%s", algorithmName, n, run, timeNs, Instant.now()));
        writer.newLine();
    }

    private static int[] randomArray(int n) {
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = RANDOM.nextInt(1_000_000);
        }
        return array;
    }

    private static double[] toDoubleArray(int[] values) {
        double[] result = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }
        return result;
    }
}
