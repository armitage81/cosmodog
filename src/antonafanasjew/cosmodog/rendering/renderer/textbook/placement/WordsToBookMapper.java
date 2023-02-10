package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;

import java.util.List;

import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;

public class WordsToBookMapper {

	public Book convert(DrawingContext drawingContext, List<Word> words, FontRefToFontTypeMap fontRefToFontTypeMap, int timeBetweenWords) {
		Book book = new Book(drawingContext, fontRefToFontTypeMap, timeBetweenWords);
		Page page = new Page();
		book.add(page);
		Line line = new Line();
		book.get(book.size() - 1).add(line);
		for (Word word: words) {
			if (word.space || word.textBased) {
				line.add(word);
			} else if (word.lineBreak) {
				line = new Line();
				book.get(book.size() - 1).add(line);
			} else if (word.pageBreak) {
				page = new Page();
				book.add(page);
				line = new Line();
				book.get(book.size() - 1).add(line);
			}
		}
		return book;
	}

}
