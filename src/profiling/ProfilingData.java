package profiling;

import java.util.HashMap;
import java.util.Map;

public class ProfilingData {

    public static ProfilingData instance = new ProfilingData();

    public static ProfilingData instance() {
        return instance;
    }

    private final Map<String, ProfilingEntry> profilingEntries = new HashMap<>();

    public void updateProfilingEntry(String id, long nanos) {
        ProfilingEntry entry = profilingEntries.get(id);
        if (entry == null) {
            entry = new ProfilingEntry(id);
        }
        entry.addProbe(nanos);
        profilingEntries.put(id, entry);
    }


    private ProfilingData() {

    }

    public void print()  {
        System.out.println("ID;COUNT;AVERAGE;MIN;MAX;TOTAL");
        profilingEntries.values().stream().sorted((a, b) -> {
            long diff = a.getTotalTime() - b.getTotalTime();
            return diff > 0 ? -1 : (diff < 0 ? 1 : 0); //descending order.
        }).forEach(System.out::println);
    }

}
