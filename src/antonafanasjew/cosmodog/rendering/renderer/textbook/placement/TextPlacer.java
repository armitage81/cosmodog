package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;

import java.util.ArrayList;
import java.util.List;

public class TextPlacer {

	private LineAndPageBreakIntervalCalculator lineAndPageBreakCalc = new LineAndPageBreakIntervalCalculator(false);
	private LineAndPageBreakIntervalCalculator pageBreakCalc = new LineAndPageBreakIntervalCalculator(true);
	private LengthBasedIntervalsCalculator additionalLineBreakCalc = new LengthBasedIntervalsCalculator();
	private PageBasedIntervalsCalculator additionalPageBreakCalc = new PageBasedIntervalsCalculator();
	
	public List<Word> wordsWithNeededControlInstructions(List<Word> words, float canvasWidth, float canvasHeight) {
		
		List<Word> wordsBrokenByLines = new ArrayList<>();
		
		List<Interval> lineAndPageBreakIntervals = lineAndPageBreakCalc.intervals(words);
		
		for (Interval interval : lineAndPageBreakIntervals) {
			if (interval.withinBreak) {
				wordsBrokenByLines.addAll(words.subList(interval.beginIndex, interval.endIndex));
			} else {
				List<Word> wordsBetweenBreaks = words.subList(interval.beginIndex, interval.endIndex);
				List<Interval> lineIntervals = additionalLineBreakCalc.intervals(wordsBetweenBreaks, canvasWidth);
				for (int i = 0; i < lineIntervals.size(); i++) {
					Interval lineInterval = lineIntervals.get(i);
					List<Word> line = wordsBetweenBreaks.subList(lineInterval.beginIndex, lineInterval.endIndex);
					wordsBrokenByLines.addAll(line);
					if (i < lineIntervals.size() - 1) {
						wordsBrokenByLines.add(Word.lineBreak());
					}
				}
			}
		}
		
		List<Word> wordsBrokenByPages = new ArrayList<>();
		
		List<Interval> pageBreakIntervals = pageBreakCalc.intervals(wordsBrokenByLines);
		
		for (Interval interval : pageBreakIntervals) {
			if (interval.withinBreak) {
				wordsBrokenByPages.addAll(wordsBrokenByLines.subList(interval.beginIndex, interval.endIndex));
			} else {
				List<Word> wordsBetweenBreaks = wordsBrokenByLines.subList(interval.beginIndex, interval.endIndex);
				List<Interval> pageIntervals = additionalPageBreakCalc.intervals(wordsBetweenBreaks, canvasHeight);
				for (int i = 0; i < pageIntervals.size(); i++) {
					Interval pageInterval = pageIntervals.get(i);
					List<Word> page = wordsBetweenBreaks.subList(pageInterval.beginIndex, pageInterval.endIndex);
					wordsBrokenByPages.addAll(page);
					if (i < pageIntervals.size() - 1) {
						wordsBrokenByPages.add(Word.pageBreak());
					}
				}
			}
		}
		
		return wordsBrokenByPages;
		
	}

	
	
}
