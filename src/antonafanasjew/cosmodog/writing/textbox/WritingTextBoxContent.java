package antonafanasjew.cosmodog.writing.textbox;

import java.io.Serializable;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.writing.model.TextBlock;
import antonafanasjew.cosmodog.writing.model.TextBlockBox;
import antonafanasjew.cosmodog.writing.model.TextBlockLine;

/**
 * This class provides the information which part of the text to render.
 * 
 */
public class WritingTextBoxContent implements Serializable {

	private static final long serialVersionUID = -1073079732798666511L;

	private WritingTextBox writingTextBox;
	private List<TextBlock> text;
	private List<TextBlockLine> textBlockLines;
	private List<TextBlockBox> textBlockBoxes;
	
	public WritingTextBoxContent(WritingTextBox writingTextBox, List<TextBlock> text) {
		this.writingTextBox = writingTextBox;
		this.text = text;
		recalculateCaches();
	}

	public List<TextBlockLine> getTextChunksForLines() {
		return this.textBlockLines;
	}
	
	private void calculateTextChunksForLines() {
		
		Queue<TextBlock> textQueue = new ArrayBlockingQueue<TextBlock>(100000);
		textQueue.addAll(getText());
		
		List<TextBlockLine> retVal = Lists.newArrayList();
		
		TextBlockLine chunk = new TextBlockLine();
		
		while (textQueue.isEmpty() == false) {
			
			boolean considerNewBlock = true;
			
			if (chunk.endsWithNewLine()) {
				considerNewBlock = false;
			} else if (chunk.endsWithParagraph()) {
				considerNewBlock = false;
			} else {
				TextBlock textBlock = textQueue.peek();
				int newTextSize = chunk.aggregatedText().length() + textBlock.getText().length() + 1; //1 for whitespace
				if (newTextSize > getWritingTextBox().getMaxLettersInLine()) {
					considerNewBlock = false;
				}
			}
			
			if (considerNewBlock) {
				TextBlock textBlock = textQueue.poll();
				chunk.add(textBlock);
			} else {
				retVal.add(chunk);
				chunk = new TextBlockLine();
			}
			
		}
		
		//Last chunk was not added because the while loop ended.
		if (chunk.isEmpty() == false) {
			retVal.add(chunk);
		}
		
		this.textBlockLines = retVal;
		
	}

	public List<TextBlockBox> getTextChunksForBoxes() {
		return this.textBlockBoxes;
	}
	
	private void calculateTextChunksForBoxes() {
		List<TextBlockLine> textBlockLines = getTextChunksForLines();
		
		Queue<TextBlockLine> textBlockLinesQueue = new ArrayBlockingQueue<TextBlockLine>(100000);
		textBlockLinesQueue.addAll(textBlockLines);
		
		List<TextBlockBox> retVal = Lists.newArrayList();
		
		TextBlockBox chunk = new TextBlockBox();
		
		while (textBlockLinesQueue.isEmpty() == false) {
			
			boolean considerNewLine = true;
			
			if (chunk.endsWithParagraph()) {
				considerNewLine = false;
			} else {
				
				int chunkSizeWithNewLine = chunk.size() + 1;
				
				if (chunkSizeWithNewLine > getWritingTextBox().getMaxLinesInBox()) {
					considerNewLine = false;
				}
			}
			
			if (considerNewLine) {
				TextBlockLine textBlockLine = textBlockLinesQueue.poll();
				chunk.add(textBlockLine);
			} else {
				retVal.add(chunk);
				chunk = new TextBlockBox();
			}
			
		}
		//Last chunk was not added because the while loop ended.
		if (chunk.isEmpty() == false) {
			retVal.add(chunk);
		}
		 
		this.textBlockBoxes = retVal;
		
	}

	/**
	 * While initializing the content, text block chunks (lines, boxes) are created and stored
	 * in local variables for later usage to avoid multiple recalculation.
	 * In some cases (e.g. when text content changes) the chunks need to be recalculated.
	 */
	public void recalculateCaches() {
		calculateTextChunksForLines();
		calculateTextChunksForBoxes();
	}
	
	public WritingTextBox getWritingTextBox() {
		return writingTextBox;
	}

	public List<TextBlock> getText() {
		return text;
	}

}
