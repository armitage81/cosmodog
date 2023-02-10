package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;

import java.util.ArrayList;
import java.util.List;

public class IntervalInverter {

	public List<Interval> invertIntervals(int originalListSize, List<Interval> intervals) {
		
		List<Interval> invertedIntervals = new ArrayList<>();

		int n = originalListSize;

		if (intervals.isEmpty()) {
			if (n > 0) {
				invertedIntervals.add(Interval.of(0, n, false));
			}
		} else {
			Interval firstInterval = intervals.get(0);
			int beginIndexOfTheFirstInterval = firstInterval.beginIndex;
			if (beginIndexOfTheFirstInterval > 0) {
				invertedIntervals.add(Interval.of(0, beginIndexOfTheFirstInterval, false));
			}
			int m = intervals.size();
			for (int i = 1; i < m; i++) {
				Interval previousInterval = intervals.get(i - 1);
				Interval currentInterval = intervals.get(i);
				int lineBeginIndex = previousInterval.endIndex;
				int lineEndIndex = currentInterval.beginIndex;
				if (lineEndIndex > lineBeginIndex) {
					invertedIntervals.add(Interval.of(previousInterval.endIndex, currentInterval.beginIndex, false));
				}
			}
			Interval lastInterval = intervals.get(intervals.size() - 1);
			int lastIntervalEndIndex = lastInterval.endIndex;
			if (lastIntervalEndIndex < n) {
				invertedIntervals.add(Interval.of(lastIntervalEndIndex, n, false));
			}
		}
		
		return invertedIntervals;
	}

}
