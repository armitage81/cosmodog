package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;

import java.util.ArrayList;
import java.util.List;

public class PageBasedIntervalsCalculator {

	private IntervalInverter intervalInverter = new IntervalInverter();	
	
	public List<Interval> intervals(List<Word> words, float height) {
		
		List<Interval> intervalsForLineBreaks = new ArrayList<>();
		
		//This must be changed later. Not the first word counts but the highest one.
		float lineHeight = words.get(0).glyphDescriptor.height();
		
		float currentlyUsedHeight = lineHeight;
		int wordIndex = 0;
		int begin = 0;
		int end = 0;
		
		while (wordIndex < words.size()) {
			
			Word word = words.get(wordIndex);
			
			if (word.lineBreak) {
			
				if (currentlyUsedHeight + lineHeight <= height) {
					currentlyUsedHeight += lineHeight;
					wordIndex++;
				} else {
					begin = wordIndex;
					end = wordIndex;
					boolean withinBreak = word.lineBreak;
					
					if (!withinBreak && begin > 0) {
						begin--;
					}
					while (begin > 0 && words.get(begin).lineBreak) {
						begin--;
					}
					
					if (!words.get(begin).lineBreak) {
						begin++;
					}
					
					if (!withinBreak && end > 0) {
						end--;
					}
					
					while (end < words.size() && words.get(end).lineBreak) {
						end++;
					}
					
					intervalsForLineBreaks.add(Interval.of(begin, end, true));
					wordIndex = end;
					currentlyUsedHeight = lineHeight;
						
				}
			
			} else {
				wordIndex++;
			}
		}
		
		List<Interval> invertedIntervals = intervalInverter.invertIntervals(words.size(), intervalsForLineBreaks);
		
		return invertedIntervals;
	}
	
}
