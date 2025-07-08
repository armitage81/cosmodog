package profiling;

public class ProfilingEntry {

    private String id;
    private int count = 0;
    private double avgNanos = 0.0;
    private long minNanos = 0;
    private long maxNanos = 0;
    private long totalTime = 0;

    public ProfilingEntry(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void addProbe(long nanos) {
        count++;
        minNanos = Math.min(minNanos, nanos);
        maxNanos = Math.max(maxNanos, nanos);
        avgNanos = avgNanos + (nanos - avgNanos) / (count + 1);
        count++;
        totalTime = (long)(avgNanos * count);
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s;%s", id, count, (long)avgNanos, minNanos, maxNanos, totalTime);
    }

    public long getTotalTime() {
        return totalTime;
    }
}
