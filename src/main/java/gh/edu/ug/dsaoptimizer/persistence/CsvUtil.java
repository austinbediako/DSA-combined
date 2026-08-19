package gh.edu.ug.dsaoptimizer.persistence;

import gh.edu.ug.dsaoptimizer.structures.DynamicArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Minimal CSV reader for this project's seed files: no quoted fields,
 * no embedded commas -- a plain comma split per line is sufficient.
 * Built-in file I/O is allowed by the brief (section 8.2); this class
 * is not assessed core logic, just plumbing to get rows into the
 * repositories below. Uses this project's own DynamicArray rather than
 * java.util.ArrayList for the line buffer regardless, for consistency
 * with the rest of the codebase.
 */
final class CsvUtil {

    private CsvUtil() {
    }

    /** Reads a CSV file and returns each data row (header skipped) as a String[]. */
    static String[][] readRows(Path csvPath) throws IOException {
        DynamicArray<String> lines = new DynamicArray<>();
        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue; // skip header
                }
                if (line.isBlank()) continue;
                lines.add(line);
            }
        }
        String[][] rows = new String[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            rows[i] = lines.get(i).split(",", -1);
        }
        return rows;
    }

    static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
