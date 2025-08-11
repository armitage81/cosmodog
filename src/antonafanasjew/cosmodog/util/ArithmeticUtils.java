package antonafanasjew.cosmodog.util;

import java.util.List;

public class ArithmeticUtils {


    /**
     * The return value contains two elements: 1. current phase. 2. remaining time of the current phase.
     */
    public static int[] remainingPhaseDuration(List<Integer> phaseDurations, long time) {
        int periodDuration = phaseDurations.stream().mapToInt(Integer::intValue).sum();
        int timeInPeriod = (int)(time % periodDuration);
        int[] retVal = new int[]{0, 0};
        for (int i = 0; i < phaseDurations.size(); i++) {
            int phaseDuration = phaseDurations.get(i);
            if (timeInPeriod < phaseDuration) {
                retVal[0] = i;
                retVal[1] = phaseDuration - timeInPeriod;
                break;
            }
            timeInPeriod -= phaseDuration;
        }
        return retVal;
    }

}
