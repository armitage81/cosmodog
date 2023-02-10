package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;


public class Interval {

	public int beginIndex;
	public int endIndex;
	public boolean withinBreak;

	public static Interval of(int begin, int end, boolean withinBreak) {
		Interval interval = new Interval();
		interval.beginIndex = begin;
		interval.endIndex = end;
		interval.withinBreak = withinBreak;
		return interval;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof Interval && ((Interval) obj).beginIndex == beginIndex
				&& ((Interval) obj).endIndex == endIndex && ((Interval) obj).withinBreak == withinBreak;
	}

	@Override
	public String toString() {
		return String.format("Begin: %s, End: %s", beginIndex, endIndex);
	}
}
