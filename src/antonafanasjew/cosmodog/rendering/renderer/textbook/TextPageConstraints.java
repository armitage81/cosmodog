package antonafanasjew.cosmodog.rendering.renderer.textbook;

import java.util.List;
import java.util.regex.Pattern;

import org.newdawn.slick.UnicodeFont;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class TextPageConstraints {
	
	private float width;
	private float height;

	public TextPageConstraints(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
		
	private static Pattern SEPARATOR = Pattern.compile("[\t\\u0020]+");
	
	public List<List<String>> textSplitByLinesAndPages(String text, UnicodeFont font) {
		List<String> lines = textSplitByLines(text, font);
		return textSplitByPages(lines, font);
	}
	
	public List<String> textSplitByLines(String text, UnicodeFont font) {
		Iterable<String> wordsIterable = Splitter.on(SEPARATOR).split(text);
		List<String> words = Lists.newArrayList(wordsIterable);
		Integer lastWordIndex = words.size() - 1;
		float lineWidth = 0;
		String thisLine = "";
		List<String> allLines = Lists.newArrayList();
		
		for (int i = 0; i < words.size(); i++) {
			String word = words.get(i);
			if (!word.equals("<br>") && !word.equals("<p>") && !word.equals("<div>")) {
				thisLine = thisLine + word + " ";
			}
			lineWidth = font.getWidth(thisLine);
			if (i < lastWordIndex) {
				String nextWord = words.get(i + 1);
				if (word.equals("<br>") && thisLine.isEmpty() == false) {
					allLines.add(thisLine);
					thisLine = "";
					lineWidth = 0;
				} else if (word.equals("<p>") && thisLine.isEmpty() == false) {
					allLines.add(thisLine);
					allLines.add("");
					thisLine = "";
					lineWidth = 0;
				} else if (word.equals("<div>")) {
					allLines.add(thisLine);
					allLines.add("<div>");
					thisLine = "";
					lineWidth = 0;
				} else if (lineWidth + font.getWidth(nextWord) > this.width) {
					allLines.add(thisLine);
					thisLine = "";
					lineWidth = 0;
				}
			} else {
				allLines.add(thisLine);
				thisLine = "";
				lineWidth = 0;
			}
		}
		return allLines;
		
	}
	
	public List<List<String>> textSplitByPages(List<String> lines, UnicodeFont font) {
		List<List<String>> pages = Lists.newArrayList();
		
		List<String> currentPage = Lists.newArrayList();
		
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if ((currentPage.size() + 1) * font.getLineHeight() > this.height) {
				pages.add(currentPage);
				currentPage = Lists.newArrayList();
			} else if (line.equals("<div>")) {
				pages.add(currentPage);
				currentPage = Lists.newArrayList();
			}
			
			if (!line.equals("<div>")) {
				currentPage.add(line);
			}
		}
		
		if (!currentPage.isEmpty()) {
			pages.add(currentPage);
		}
		
		return pages;
		
		
	}
}
