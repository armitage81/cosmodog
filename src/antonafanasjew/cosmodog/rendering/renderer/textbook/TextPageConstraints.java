package antonafanasjew.cosmodog.rendering.renderer.textbook;

import java.util.List;

import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.TextPlacer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.TextToWordsMapper;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Word;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.WordsToBookMapper;

public class TextPageConstraints {
	
	private DrawingContext drawingContext;
	
	private TextToWordsMapper textToWordsMapper = new TextToWordsMapper();
	private TextPlacer textPlacer = new TextPlacer();
	private WordsToBookMapper wordsToBookMapper = new WordsToBookMapper();

	public static TextPageConstraints fromDc(DrawingContext dc) {
		return new TextPageConstraints(dc);
	}
	
	public TextPageConstraints(DrawingContext drawingContext) {
		this.drawingContext = drawingContext;
	}
	
	public float getWidth() {
		return drawingContext.w();
	}
	
	public float getHeight() {
		return drawingContext.h();
	}
	
	public Book textToBook(String text, FontRefToFontTypeMap fontRefToFontTypeMap) {
		return textToBook(text, fontRefToFontTypeMap, 0);
	}
	
	public Book textToBook(String text, FontRefToFontTypeMap fontRefToFontTypeMap, int timeBetweenWords) {
		List<Word> unprocessedWords = textToWordsMapper.map(text, fontRefToFontTypeMap);
		List<Word> wordsWithControls = textPlacer.wordsWithNeededControlInstructions(unprocessedWords, drawingContext.w(), drawingContext.h());
		Book book = wordsToBookMapper.convert(drawingContext, wordsWithControls, fontRefToFontTypeMap, timeBetweenWords);
		return book;
	}
}
