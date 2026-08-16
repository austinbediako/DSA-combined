package gh.edu.ug.dsaoptimizer.performance;

import gh.edu.ug.dsaoptimizer.algorithms.BinarySearch;
import gh.edu.ug.dsaoptimizer.algorithms.LinearSearch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class SearchComparisonBenchmark {

    private static final int[] INPUT_SIZES = {
            100, 500, 1_000, 5_000, 10_000
    };

    private static final int RUNS = 3;
    private static final int ITERATIONS = 10_000;

    private SearchComparisonBenchmark() {
    }

    public static void main(String[] args) throws IOException {

        Path output = Path.of("evidence", "benchmarks",
                "search_comparison.csv");

        Files.createDirectories(output.getParent());

        StringBuilder csv = new StringBuilder();

        csv.append("input_size,linear_run_1_ns,linear_run_2_ns,")
           .append("linear_run_3_ns,linear_average_ns,")
           .append("binary_run_1_ns,binary_run_2_ns,")
           .append("binary_run_3_ns,binary_average_ns\n");

        System.out.println("Search Performance Comparison");
        System.out.println("--------------------------------");

        for (int size : INPUT_SIZES) {

            int[] data = createSortedArray(size);
            int target = size - 1;

            // Warm-up to reduce JVM startup/JIT effects.
            for (int i = 0; i < 2_000; i++) {
                LinearSearch.search(data, target);
                BinarySearch.search(data, target);
            }

            long[] linearTimes = new long[RUNS];
            long[] binaryTimes = new long[RUNS];

            for (int run = 0; run < RUNS; run++) {

                long start = System.nanoTime();

                for (int i = 0; i < ITERATIONS; i++) {
                    LinearSearch.search(data, target);
                }

                long end = System.nanoTime();
                linearTimes[run] = end - start;

                start = System.nanoTime();

                for (int i = 0; i < ITERATIONS; i++) {
                    BinarySearch.search(data, target);
                }

                end = System.nanoTime();
                binaryTimes[run] = end - start;
            }

            long linearAverage = average(linearTimes);
            long binaryAverage = average(binaryTimes);

            csv.append(size).append(',')
               .append(linearTimes[0]).append(',')
               .append(linearTimes[1]).append(',')
               .append(linearTimes[2]).append(',')
               .append(linearAverage).append(',')
               .append(binaryTimes[0]).append(',')
               .append(binaryTimes[1]).append(',')
               .append(binaryTimes[2]).append(',')
               .append(binaryAverage).append('\n');

            System.out.printf(
                    Locale.US,
                    "n=%d | Linear avg: %d ns | Binary avg: %d ns%n",
                    size,
                    linearAverage,
                    binaryAverage
            );
        }

        Files.writeString(output, csv.toString());

        System.out.println();
        System.out.println("CSV saved to:");
        System.out.println(output.toAbsolutePath());
    }

    private static int[] createSortedArray(int size) {
        int[] array = new int[size];

        for (int i = 0; i < size; i++) {
            array[i] = i;
        }

        return array;
    }

    private static long average(long[] values) {
        long sum = 0;

        for (long value : values) {
            sum += value;
        }

        return sum / values.length;
    }
}