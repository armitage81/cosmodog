package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;

class PageBasedIntervalsCalculatorTest {

	private PageBasedIntervalsCalculator calc;
	private List<Word> words;
	
	@BeforeEach
	public void init() {
		calc = new PageBasedIntervalsCalculator();
		words = new ArrayList<>();
	}

	@Test
	public void testOneLineFitting() {
		words.add(Word.textBased("A", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.space(GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.textBased("B", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.space(GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.textBased("C", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		List<Interval> intervals = calc.intervals(words, 2);
		assertEquals(1, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(5, intervals.get(0).endIndex);
	}
	
	@Test
	public void testTwoLinesFitting() {
		words.add(Word.textBased("A", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.textBased("B", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.lineBreak());
		words.add(Word.textBased("C", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		List<Interval> intervals = calc.intervals(words, 4);
		assertEquals(1, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(4, intervals.get(0).endIndex);
	}
	
	@Test
	public void testTwoLinesNotFitting() {
		words.add(Word.textBased("A", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.textBased("B", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.lineBreak());
		words.add(Word.textBased("C", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		List<Interval> intervals = calc.intervals(words, 3);
		assertEquals(2, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(2, intervals.get(0).endIndex);
		assertEquals(3, intervals.get(1).beginIndex);
		assertEquals(4, intervals.get(1).endIndex);
	}
	
	@Test
	public void testTwoLinesWithMultipleLineBreaksNotFitting() {
		words.add(Word.textBased("A", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.textBased("B", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.textBased("C", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		List<Interval> intervals = calc.intervals(words, 3);
		assertEquals(2, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(2, intervals.get(0).endIndex);
		assertEquals(5, intervals.get(1).beginIndex);
		assertEquals(6, intervals.get(1).endIndex);
	}
	
	@Test
	public void testLineBreaksInbetweenNotFitting() {
		words.add(Word.textBased("A", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.lineBreak());
		words.add(Word.textBased("B", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.textBased("C", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		List<Interval> intervals = calc.intervals(words, 5);
		assertEquals(2, intervals.size());
		assertEquals(0, intervals.get(0).beginIndex);
		assertEquals(3, intervals.get(0).endIndex);
		assertEquals(6, intervals.get(1).beginIndex);
		assertEquals(7, intervals.get(1).endIndex);
	}
	
	@Test
	public void testLineBreaksAtTheBeginningNotFitting() {
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.textBased("B", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.textBased("C", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		List<Interval> intervals = calc.intervals(words, 5);
		assertEquals(2, intervals.size());
		assertEquals(2, intervals.get(0).beginIndex);
		assertEquals(3, intervals.get(0).endIndex);
		assertEquals(6, intervals.get(1).beginIndex);
		assertEquals(7, intervals.get(1).endIndex);
	}
	
	@Test
	public void testOnlyLineBreaksNotFitting() {
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		words.add(Word.lineBreak());
		List<Interval> intervals = calc.intervals(words, 5);
		assertEquals(0, intervals.size());
	}
	
	@Test
	public void testBugWithHeight() {
		
		//When updating currently used height, canvas height was added for each line, not the line height.
		words.add(Word.textBased("A", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.lineBreak());
		words.add(Word.textBased("B", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.lineBreak());
		words.add(Word.textBased("C", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		List<Interval> intervals = calc.intervals(words, 6);
		assertEquals(1, intervals.size());
	}
	
	@Test
	public void testBugWithOneLineTooMuch() {
		//After breaking the page, the initial currently used height was set to 0 and not to line height.
		words.add(Word.textBased("A", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.lineBreak());
		words.add(Word.textBased("B", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.lineBreak());
		words.add(Word.textBased("C", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.lineBreak());
		words.add(Word.textBased("D", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		words.add(Word.lineBreak());
		words.add(Word.textBased("E", GlyphDescriptorImpl.of(1, 10, FontRefToFontTypeMap.FONT_REF_DEFAULT)));
		List<Interval> intervals = calc.intervals(words, 2);
		assertEquals(3, intervals.size());
	}
	
}
