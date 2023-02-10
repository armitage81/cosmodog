package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;

import java.util.ArrayList;
import java.util.List;

public class LengthBasedIntervalsCalculator {

	private IntervalInverter intervalInverter = new IntervalInverter();
	
	public List<Interval> intervals(List<Word> words, float width) {

		List<Interval> intervalsForSpaces = new ArrayList<>();
		
		float currentlyUsedWidth = 0;
		int wordIndex = 0;
		int begin = 0;
		int end = 0;
		
		while (wordIndex < words.size()) {
			
			Word word = words.get(wordIndex);
			
			if (currentlyUsedWidth + word.glyphDescriptor.width() <= width) {
				currentlyUsedWidth += word.glyphDescriptor.width();
				wordIndex++;
			} else {
				begin = wordIndex;
				end = wordIndex;
				boolean withinBreak = word.space;
				
				if (!withinBreak && begin > 0) {
					begin--;
				}
				while (begin > 0 && words.get(begin).space) {
					begin--;
				}
				
				if (words.get(begin).textBased) {
					begin++;
				}
				
				if (!withinBreak && end > 0) {
					end--;
				}
				
				while (end < words.size() && words.get(end).space) {
					end++;
				}
				
				intervalsForSpaces.add(Interval.of(begin, end, true));
				wordIndex = end;
				currentlyUsedWidth = 0;
					
			}
		}
		
		List<Interval> invertedIntervals = intervalInverter.invertIntervals(words.size(), intervalsForSpaces);
		
		return invertedIntervals;
	}
	
}
