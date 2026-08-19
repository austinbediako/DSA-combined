package gh.edu.ug.dsaoptimizer.model;

import java.time.Instant;
import java.util.Objects;

public class AlgorithmRun {

    private final Integer runId; // nullable until persisted (autoincrement)
    private final String algorithmName;
    private final int inputSize;
    private final long timeNs;
    private final Long memoryKb; // nullable
    private final Instant dateRun;

    public AlgorithmRun(Integer runId, String algorithmName, int inputSize,
                         long timeNs, Long memoryKb, Instant dateRun) {
        this.runId = runId;
        this.algorithmName = Objects.requireNonNull(algorithmName, "algorithmName");
        this.inputSize = inputSize;
        this.timeNs = timeNs;
        this.memoryKb = memoryKb;
        this.dateRun = Objects.requireNonNull(dateRun, "dateRun");
    }

    public Integer getRunId() {
        return runId;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getInputSize() {
        return inputSize;
    }

    public long getTimeNs() {
        return timeNs;
    }

    public Long getMemoryKb() {
        return memoryKb;
    }

    public Instant getDateRun() {
        return dateRun;
    }

    @Override
    public String toString() {
        return "AlgorithmRun{" +
                "runId=" + runId +
                ", algorithmName='" + algorithmName + '\'' +
                ", inputSize=" + inputSize +
                ", timeNs=" + timeNs +
                '}';
    }
}
