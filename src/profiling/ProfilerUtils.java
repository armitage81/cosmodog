package profiling;

import java.util.concurrent.Callable;
import java.util.function.Function;

public class ProfilerUtils {

    public static void runWithProfiling(String profilingEntryName, Runnable runnable) {
        long before = System.nanoTime();
        runnable.run();
        long after = System.nanoTime();
        ProfilingData.instance().updateProfilingEntry(profilingEntryName, after - before);
    }

    public static <R> R callWithProfiling(String profilingEntryName, Callable<R> callable) {
        long before = System.nanoTime();
        R retVal;
        try {
            retVal = callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long after = System.nanoTime();
        ProfilingData.instance().updateProfilingEntry(profilingEntryName, after - before);
        return retVal;
    }

}
