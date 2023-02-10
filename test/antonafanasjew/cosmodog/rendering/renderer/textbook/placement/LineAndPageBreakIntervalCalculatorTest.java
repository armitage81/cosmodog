package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LineAndPageBreakIntervalCalculatorTest {

	
	private LineAndPageBreakIntervalCalculator calc;
	private List<Word> words;
	
	@BeforeEach
	private void init() {
		calc = new LineAndPageBreakIntervalCalculator(false);
		words = new ArrayList<>();

	}
	
	private GlyphDescriptor glyphDescrForWidth(float width) {
		return new GlyphDescriptor() {
			
			@Override
			public float width() {
				return width;
			}
			
			@Override
			public float height() {
				return 1;
			}
			
			@Override
			public String fontRef() {
				return "irrelevant";
			}
		};
	}
	
	@Test
	void testEmptyList() {
		LineAndPageBreakIntervalCalculator calc = new LineAndPageBreakIntervalCalculator(false);
		List<Interval> intervals = calc.intervals(words);
		assertEquals(0, intervals.size());
	
	}
	
	@Test
	void testOneWord() {
		words.add(Word.textBased("A", glyphDescrForWidth(10)));
		List<Interval> intervals = calc.intervals(words);
		assertEquals(1, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
		assertEquals(false, intervals.get(0).withinBreak);
		
	}
	
	@Test
	void testOneSpace() {
		words.add(Word.space(glyphDescrForWidth(10)));
		List<Interval> intervals = calc.intervals(words);
		assertEquals(1, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
		assertEquals(false, intervals.get(0).withinBreak);
		
	}
	
	@Test
	void testWordEnclosedBySpaces() {
		words.add(Word.space(glyphDescrForWidth(10)));
		words.add(Word.textBased("A", glyphDescrForWidth(10)));
		words.add(Word.space(glyphDescrForWidth(10)));
		List<Interval> intervals = calc.intervals(words);
		assertEquals(1, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(3, intervals.get(0).endIndex);
		assertEquals(false, intervals.get(0).withinBreak);
		
	}
	
	@Test
	void testLineBreakAndWord() {
		words.add(Word.lineBreak());
		words.add(Word.textBased("A", glyphDescrForWidth(10)));
		List<Interval> intervals = calc.intervals(words);
		assertEquals(2, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
		assertEquals(true, intervals.get(0).withinBreak);
		assertEquals(1, intervals.get(1).beginIndex);
		assertEquals(2, intervals.get(1).endIndex);
		assertEquals(false, intervals.get(1).withinBreak);
		
	}
	
	@Test
	void testMultipleLineBreaksAndWord() {
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.textBased("A", glyphDescrForWidth(10)));
		List<Interval> intervals = calc.intervals(words);
		assertEquals(2, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(4, intervals.get(0).endIndex);
		assertEquals(true, intervals.get(0).withinBreak);
		assertEquals(4, intervals.get(1).beginIndex);
		assertEquals(5, intervals.get(1).endIndex);
		assertEquals(false, intervals.get(1).withinBreak);
		
	}
	
	@Test
	void testWordAndLineBreak() {
		words.add(Word.textBased("A", glyphDescrForWidth(10)));
		words.add(Word.lineBreak());
		List<Interval> intervals = calc.intervals(words);
		assertEquals(2, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
		assertEquals(false, intervals.get(0).withinBreak);
		assertEquals(1, intervals.get(1).beginIndex);
		assertEquals(2, intervals.get(1).endIndex);
		assertEquals(true, intervals.get(1).withinBreak);
	}
	
	@Test
	void testWordAndMultipleLineBreaks() {
		words.add(Word.textBased("A", glyphDescrForWidth(10)));
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		List<Interval> intervals = calc.intervals(words);
		assertEquals(2, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
		assertEquals(false, intervals.get(0).withinBreak);
		assertEquals(1, intervals.get(1).beginIndex);
		assertEquals(5, intervals.get(1).endIndex);
		assertEquals(true, intervals.get(1).withinBreak);
	}
	
	@Test
	void testOneLineBreak() {
		words.add(Word.lineBreak());
		List<Interval> intervals = calc.intervals(words);
		assertEquals(1, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
		assertEquals(true, intervals.get(0).withinBreak);
	}
	
	@Test
	void testMultipleLineBreaks() {
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		List<Interval> intervals = calc.intervals(words);
		assertEquals(1, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(4, intervals.get(0).endIndex);
		assertEquals(true, intervals.get(0).withinBreak);
	}
	
	@Test
	void testTwoParts() {
		words.add(Word.textBased("A", glyphDescrForWidth(10)));
		words.add(Word.lineBreak());
		words.add(Word.textBased("B", glyphDescrForWidth(10)));
		List<Interval> intervals = calc.intervals(words);
		assertEquals(3, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
		assertEquals(false, intervals.get(0).withinBreak);
		assertEquals(1, intervals.get(1).beginIndex);
		assertEquals(2, intervals.get(1).endIndex);
		assertEquals(true, intervals.get(1).withinBreak);
		assertEquals(2, intervals.get(2).beginIndex);
		assertEquals(3, intervals.get(2).endIndex);
		assertEquals(false, intervals.get(2).withinBreak);
		
	}
	
	@Test
	void testTwoPartsWithPageBreak() {
		words.add(Word.textBased("A", glyphDescrForWidth(10)));
		words.add(Word.pageBreak());
		words.add(Word.textBased("B", glyphDescrForWidth(10)));
		List<Interval> intervals = calc.intervals(words);
		assertEquals(3, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
		assertEquals(false, intervals.get(0).withinBreak);
		assertEquals(1, intervals.get(1).beginIndex);
		assertEquals(2, intervals.get(1).endIndex);
		assertEquals(true, intervals.get(1).withinBreak);
		assertEquals(2, intervals.get(2).beginIndex);
		assertEquals(3, intervals.get(2).endIndex);
		assertEquals(false, intervals.get(2).withinBreak);
		
	}
	
	@Test
	void testTwoPartsWithMultipleLineBreaksAndPageBreaks() {
		words.add(Word.textBased("A", glyphDescrForWidth(10)));
		words.add(Word.lineBreak());
		words.add(Word.pageBreak());
		words.add(Word.lineBreak());
		words.add(Word.pageBreak());
		words.add(Word.textBased("B", glyphDescrForWidth(10)));
		List<Interval> intervals = calc.intervals(words);
		assertEquals(3, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
		assertEquals(false, intervals.get(0).withinBreak);
		assertEquals(1, intervals.get(1).beginIndex);
		assertEquals(5, intervals.get(1).endIndex);
		assertEquals(true, intervals.get(1).withinBreak);
		assertEquals(5, intervals.get(2).beginIndex);
		assertEquals(6, intervals.get(2).endIndex);
		assertEquals(false, intervals.get(2).withinBreak);
		
	}

}
