package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;

import java.util.ArrayList;
import java.util.List;

public class LineAndPageBreakIntervalCalculator {

	private boolean onlyPageBreaks;
	
	
	public LineAndPageBreakIntervalCalculator(boolean onlyPageBreaks) {
		this.onlyPageBreaks = onlyPageBreaks;
	}

	public List<Interval> intervals(List<Word> l) {

		List<Word> wordsCopy = new ArrayList<>();
		wordsCopy.addAll(l);

		if (wordsCopy.isEmpty()) {
			return new ArrayList<>();
		}

		boolean withinBreak = false;
		
		if (textbreak(wordsCopy, 0, !onlyPageBreaks)) {
			withinBreak = true;
		}
		
		List<Integer> intervalBegins = new ArrayList<>();
		List<Integer> intervalEnds = new ArrayList<>();
		List<Boolean> withinBreaks = new ArrayList<>();

		intervalBegins.add(0);
		withinBreaks.add(withinBreak);
		
		for (int i = 0; i < wordsCopy.size(); i++) {
			if (textbreak(wordsCopy, i, !onlyPageBreaks) && !withinBreak) {
				intervalEnds.add(i);
				intervalBegins.add(i);
				withinBreaks.add(true);
				withinBreak = true;
			}
			if (!textbreak(wordsCopy, i, !onlyPageBreaks) && withinBreak) {
				intervalEnds.add(i);
				intervalBegins.add(i);
				withinBreaks.add(false);
				withinBreak = false;
			}
		}
		intervalEnds.add(wordsCopy.size());
		
		List<Interval> intervals = new ArrayList<>();
		for (int i = 0; i < intervalBegins.size(); i++) {
			intervals.add(Interval.of(intervalBegins.get(i), intervalEnds.get(i), withinBreaks.get(i)));
		}
		
		return intervals;

	}

	private boolean textbreak(List<Word> words, int index, boolean lineBreaksCount) {
		Word word = words.get(index);
		return (lineBreaksCount && word.lineBreak) || word.pageBreak;
	}

}
