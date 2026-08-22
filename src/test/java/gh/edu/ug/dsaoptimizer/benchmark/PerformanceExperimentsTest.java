package gh.edu.ug.dsaoptimizer.benchmark;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Fast smoke tests with tiny input sizes -- verifies each experiment
 * actually runs and produces valid CSV/PNG output, without paying the
 * cost of the brief's full required input sizes on every build. The
 * real full-scale runs (100..20,000 etc.) were executed manually and
 * their output committed under evidence/benchmarks and evidence/graphs.
 */
class PerformanceExperimentsTest {

    @Test
    void sortingComparisonProducesValidCsvAndPng(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("sorting.csv");
        Path png = tempDir.resolve("sorting.png");
        PerformanceExperiments.runSortingComparison(new int[]{5, 10}, csv, png);

        assertTrue(Files.exists(csv));
        assertTrue(Files.exists(png));
        long lineCount = Files.lines(csv).count();
        // header + 2 sizes * 3 runs * 4 algorithms
        assertEquals(1 + 2 * 3 * 4, lineCount);
        assertTrue(Files.size(png) > 0);
    }

    @Test
    void hashLoadFactorProducesValidCsvAndPng(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("hash.csv");
        Path png = tempDir.resolve("hash.png");
        PerformanceExperiments.runHashLoadFactor(new int[]{20, 50}, csv, png);

        assertTrue(Files.exists(csv));
        assertTrue(Files.exists(png));
        // header + 2 sizes * 5 load factors * 3 runs
        long lineCount = Files.lines(csv).count();
        assertEquals(1 + 2 * 5 * 3, lineCount);
    }

    @Test
    void bstVsBalancedTreeProducesValidCsvAndPngAndBstIsTallerOnAscendingInsert(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("bst.csv");
        Path png = tempDir.resolve("bst.png");
        PerformanceExperiments.runBstVsBalancedTree(new int[]{20, 50}, csv, png);

        assertTrue(Files.exists(csv));
        assertTrue(Files.exists(png));
        String content = Files.readString(csv);
        // BST height for ascending 1..50 insert must be n-1 = 49 at some point
        assertTrue(content.contains(",49"));
    }

    @Test
    void heapDispatchProducesValidCsvAndPng(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("heap.csv");
        Path png = tempDir.resolve("heap.png");
        PerformanceExperiments.runHeapDispatch(new int[]{10, 20}, csv, png);

        assertTrue(Files.exists(csv));
        assertTrue(Files.exists(png));
        long lineCount = Files.lines(csv).count();
        // header + 2 sizes * 3 runs * 2 operations
        assertEquals(1 + 2 * 3 * 2, lineCount);
    }

    @Test
    void graphAlgorithmsProducesValidCsvAndPng(@TempDir Path tempDir) throws Exception {
        Path csv = tempDir.resolve("graph.csv");
        Path png = tempDir.resolve("graph.png");
        PerformanceExperiments.runGraphAlgorithms(new int[]{10, 20}, csv, png);

        assertTrue(Files.exists(csv));
        assertTrue(Files.exists(png));
        long lineCount = Files.lines(csv).count();
        // header + 2 sizes * 3 runs * 4 algorithms
        assertEquals(1 + 2 * 3 * 4, lineCount);
    }
}
