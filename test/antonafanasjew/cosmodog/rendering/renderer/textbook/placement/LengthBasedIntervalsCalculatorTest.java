package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

class LengthBasedIntervalsCalculatorTest {

	private LengthBasedIntervalsCalculator calc;
	private List<Word> words;
	
	@BeforeEach
	private void init() {
		calc = new LengthBasedIntervalsCalculator();
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
	void testOneWordFitting() {
		words.add(Word.textBased("A", glyphDescrForWidth(2)));
		List<Interval> intervals = calc.intervals(words, 2);
		assertEquals(1, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
	}
	
	@Test
	void testOneWordAndOneSpaceFitting() {
		words.add(Word.textBased("A", glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(1)));
		List<Interval> intervals = calc.intervals(words, 3);
		assertEquals(1, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(2, intervals.get(0).endIndex);
	}
	
	@Test
	void testOneWordAndOneSpaceNotFitting() {
		words.add(Word.textBased("A", glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		List<Interval> intervals = calc.intervals(words, 3);
		assertEquals(1, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
	}
	
	@Test
	void testOneWordAndThreeSpacesNotFitting() {
		words.add(Word.textBased("A", glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		List<Interval> intervals = calc.intervals(words, 5);
		assertEquals(1, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
	}
	
	@Test
	void testTwoWordsSecondNotFitting() {
		words.add(Word.textBased("A", glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.textBased("B", glyphDescrForWidth(2)));
		List<Interval> intervals = calc.intervals(words, 9);
		assertEquals(2, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
		assertEquals(4, intervals.get(1).beginIndex);
		assertEquals(5, intervals.get(1).endIndex);
	}
	
	@Test
	void testTwoWordsSpacesNotFitting() {
		words.add(Word.textBased("A", glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.textBased("B", glyphDescrForWidth(2)));
		List<Interval> intervals = calc.intervals(words, 5);
		assertEquals(2, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
		assertEquals(4, intervals.get(1).beginIndex);
		assertEquals(5, intervals.get(1).endIndex);
	}
	
	@Test
	void testThreeLinesSpacesNotFitting() {
		words.add(Word.textBased("A", glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.textBased("B", glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.textBased("C", glyphDescrForWidth(2)));
		List<Interval> intervals = calc.intervals(words, 5);
		assertEquals(3, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
		assertEquals(4, intervals.get(1).beginIndex);
		assertEquals(5, intervals.get(1).endIndex);
		assertEquals(8, intervals.get(2).beginIndex);
		assertEquals(9, intervals.get(2).endIndex);
	}
	
	@Test
	void testThreeLinesWordsNotFitting() {
		words.add(Word.textBased("A", glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.textBased("B", glyphDescrForWidth(2)));
		words.add(Word.space(glyphDescrForWidth(2)));
		words.add(Word.textBased("C", glyphDescrForWidth(2)));
		List<Interval> intervals = calc.intervals(words, 5);
		assertEquals(3, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(1, intervals.get(0).endIndex);
		assertEquals(2, intervals.get(1).beginIndex);
		assertEquals(3, intervals.get(1).endIndex);
		assertEquals(4, intervals.get(2).beginIndex);
		assertEquals(5, intervals.get(2).endIndex);
	}
	
	@Test
	void testRealExample() {
		
		String s = "Ein Alphabet (frühneuhochdeutsch von kirchenlateinisch alphabetum) ist die Gesamtheit der kleinsten Schriftzeichen bzw. Buchstaben einer Sprache oder mehrerer Sprachen in einer festgelegten Reihenfolge. Die Buchstaben können über orthographische Regeln zu Wörtern verknüpft werden und damit die Sprache schriftlich darstellen. Die im Alphabet festgelegte Reihenfolge der Buchstaben erlaubt die alphabetische Sortierung von Wörtern und Namen beispielsweise in Wörterbüchern. Nach einigen Definitionen ist mit Alphabet nicht der Buchstabenbestand in seiner festgelegten Reihenfolge gemeint, sondern die Reihenfolge selbst.";
		
		String[] parts = s.split(" ");
		
		for (String part : parts) {
			words.add(Word.textBased(part, glyphDescrForWidth(part.length())));			
			words.add(Word.space(glyphDescrForWidth(1)));
		}
		
		List<Interval> intervals = calc.intervals(words, 20);
		
		for (Interval interval : intervals) {
			assertTrue(interval.endIndex > interval.beginIndex);
			assertTrue(interval.endIndex - interval.beginIndex <= 20);
		}
	}

}
